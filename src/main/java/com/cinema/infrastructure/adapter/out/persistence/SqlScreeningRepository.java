package com.cinema.infrastructure.adapter.out.persistence;

import com.cinema.application.port.out.ScreeningRepository;
import com.cinema.infrastructure.adapter.out.persistence.exception.OptimisticLockException;
import com.cinema.domain.model.vo.MovieId;
import com.cinema.domain.model.vo.RoomId;
import com.cinema.domain.model.Screening;
import com.cinema.domain.model.vo.ScreeningId;
import com.cinema.domain.model.ScreeningSeat;
import com.cinema.domain.model.vo.ScreeningSeatId;
import com.cinema.domain.model.vo.Seat;
import com.cinema.domain.model.SeatStatus;
import com.cinema.domain.model.SeatType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public final class SqlScreeningRepository implements ScreeningRepository {
    private static final Logger LOGGER = LoggerFactory.getLogger(SqlScreeningRepository.class);
    private final Connection connection;

    public SqlScreeningRepository(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Optional<Screening> findById(ScreeningId screeningId) {
        String sql = """
                      SELECT s.id, s.movie_id, s.room_id, s.start_date_time,
                             ss.id as seat_id, ss.seat_row, ss.seat_number, ss.seat_type,
                             ss.status, ss.locked_at, ss.version
                      FROM screenings s
                      LEFT JOIN screening_seats ss ON s.id = ss.screening_id
                      WHERE s.id = ?;
                      """;

        try (var stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, screeningId.toString());

            try (var rs = stmt.executeQuery()) {
                return mapToScreening(rs);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to load screening: " + screeningId, e);
        }
    }

    @Override
    public List<Screening> findByMovieId(MovieId movieId) {
        String sql = """
                      SELECT s.id, s.movie_id, s.room_id, s.start_date_time,
                          ss.id as seat_id, ss.seat_row, ss.seat_number, ss.seat_type,
                          ss.status, ss.locked_at, ss.version
                      FROM screenings s
                      LEFT JOIN screening_seats ss ON s.id = ss.screening_id
                      WHERE s.movie_id = ?
                      ORDER BY s.start_date_time;
                      """;

        try (var stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, movieId.toString());

            try (var rs = stmt.executeQuery()) {
                return mapToScreenings(rs);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to load screenings for movie: " + movieId, e);
        }
    }

    @Override
    public void save(Screening screening) {
        try {
            connection.setAutoCommit(false);

            updateScreeningSeats(screening);

            connection.commit();
            LOGGER.debug("Saved screening: {}", screening.getId());
        } catch (SQLException e) {
            rollback();
            throw new RuntimeException("Failed to save screening: " + screening.getId(), e);
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                LOGGER.error("Failed to reset auto-commit", e);
            }
        }
    }

    private void updateScreeningSeats(Screening screening) throws SQLException {
        List<ScreeningSeat> modifiedSeats = screening.getModifiedSeats();

        if (modifiedSeats.isEmpty()) {
            LOGGER.debug("No seats to update for screening: {}", screening.getId());
            return;
        }

        String sql = """
                      UPDATE screening_seats
                      SET status = ?, locked_at = ?, version = version + 1
                      WHERE id = ? AND version = ?;
                      """;

        try (var stmt = connection.prepareStatement(sql)) {
            for (ScreeningSeat seat : modifiedSeats) {
                stmt.setString(1, seat.getStatus().name());

                if (seat.getLockedAt() != null) {
                    stmt.setString(2, seat.getLockedAt().toString());
                } else {
                    stmt.setString(2, null);
                }

                stmt.setString(3, seat.getId().toString());
                stmt.setInt(4, seat.getVersion());

                int updated = stmt.executeUpdate();
                if (updated == 0) {
                    throw new OptimisticLockException(
                            "Seat " + seat.getSeat().getSeatPosition()
                                    + " was modified by another transaction. Please refresh and retry."
                    );
                }

                seat.incrementVersion();
            }
            LOGGER.debug("Updated {} seats for screening: {}", modifiedSeats.size(), screening.getId());
        }
    }

    private void rollback() {
        try {
            connection.rollback();
        } catch (SQLException e) {
            LOGGER.error("Failed to rollback transaction", e);
        }
    }

    private Optional<Screening> mapToScreening(ResultSet rs) throws SQLException {
        if (!rs.next()) {
            return Optional.empty();
        }

        ScreeningId screeningId = ScreeningId.from(rs.getString("id"));
        MovieId movieId = MovieId.from(rs.getString("movie_id"));
        RoomId roomId = RoomId.from(rs.getString("room_id"));
        ZonedDateTime startDateTime = ZonedDateTime.parse(rs.getString("start_date_time"));

        List<ScreeningSeat> seats = new ArrayList<>();

        do {
            String seatId = rs.getString("seat_id");
            if (seatId != null) {
                seats.add(mapToScreeningSeat(rs));
            }
        } while (rs.next());

        return Optional.of(new Screening(screeningId, movieId, roomId, startDateTime, seats));
    }

    private List<Screening> mapToScreenings(ResultSet rs) throws SQLException {
        List<Screening> screenings = new ArrayList<>();
        Screening currentScreening = null;
        List<ScreeningSeat> currentSeats = new ArrayList<>();

        while (rs.next()) {
            String screeningIdStr = rs.getString("id");
            ScreeningId screeningId = ScreeningId.from(screeningIdStr);

            if (currentScreening == null || !currentScreening.getId().equals(screeningId)) {
                if (currentScreening != null) {
                    screenings.add(currentScreening);
                }

                currentSeats = new ArrayList<>();
                currentScreening = new Screening(
                        screeningId,
                        MovieId.from(rs.getString("movie_id")),
                        RoomId.from(rs.getString("room_id")),
                        ZonedDateTime.parse(rs.getString("start_date_time")),
                        currentSeats
                );
            }

            String seatId = rs.getString("seat_id");
            if (seatId != null) {
                currentSeats.add(mapToScreeningSeat(rs));
            }
        }

        if (currentScreening != null) {
            screenings.add(currentScreening);
        }

        return screenings;
    }

    private ScreeningSeat mapToScreeningSeat(ResultSet rs) throws SQLException {
        Seat seat = new Seat(
                rs.getString("seat_row"),
                rs.getString("seat_number"),
                SeatType.valueOf(rs.getString("seat_type"))
        );

        String lockedAtStr = rs.getString("locked_at");
        Instant lockedAt = lockedAtStr != null ? Instant.parse(lockedAtStr) : null;

        return new ScreeningSeat(
                ScreeningSeatId.from(rs.getString("seat_id")),
                seat,
                SeatStatus.valueOf(rs.getString("status")),
                lockedAt,
                rs.getInt("version")
        );
    }
}
