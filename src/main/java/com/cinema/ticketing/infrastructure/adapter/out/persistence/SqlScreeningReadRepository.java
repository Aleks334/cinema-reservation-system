package com.cinema.ticketing.infrastructure.adapter.out.persistence;

import com.cinema.ticketing.application.query.dto.ScreeningDto;
import com.cinema.ticketing.application.query.dto.ScreeningSeatDto;
import com.cinema.ticketing.application.query.port.ScreeningReadRepository;
import com.google.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.*;

public final class SqlScreeningReadRepository implements ScreeningReadRepository {

    private static final Logger LOGGER = LoggerFactory.getLogger(SqlScreeningReadRepository.class);
    private final DataSource dataSource;

    @Inject
    public SqlScreeningReadRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Optional<ScreeningDto> findById(String screeningId) {
        String sql = """
                      SELECT s.id, s.movie_id, s.room_id, s.start_date_time,
                             ss.id as seat_id, ss.seat_row, ss.seat_number, ss.seat_type,
                             ss.status, ss.locked_at, ss.version
                      FROM screenings s
                      LEFT JOIN screening_seats ss ON s.id = ss.screening_id
                      WHERE s.id = ?;
                      """;

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setObject(1, screeningId);

            try (ResultSet rs = stmt.executeQuery()) {
                List<ScreeningDto> screenings = mapToScreeningDtos(rs);
                return screenings.stream().findFirst();
            }
        } catch (SQLException e) {
            LOGGER.error("Failed to read screening with ID {}", screeningId, e);
            throw new RuntimeException("Failed to read screening from database: " + screeningId, e);
        }
    }

    @Override
    public List<ScreeningDto> findByMovieId(String movieId) {
        String sql = """
                      SELECT s.id, s.movie_id, s.room_id, s.start_date_time,
                             ss.id as seat_id, ss.seat_row, ss.seat_number, ss.seat_type,
                             ss.status, ss.locked_at, ss.version
                      FROM screenings s
                      LEFT JOIN screening_seats ss ON s.id = ss.screening_id
                      WHERE s.movie_id = ?
                      ORDER BY s.start_date_time;
                      """;

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setObject(1, movieId);

            try (ResultSet rs = stmt.executeQuery()) {
                return mapToScreeningDtos(rs);
            }
        } catch (SQLException e) {
            LOGGER.error("Failed to read screenings for movie ID {}", movieId, e);
            throw new RuntimeException("Failed to read screenings from database for movie: " + movieId, e);
        }
    }

    private List<ScreeningDto> mapToScreeningDtos(ResultSet rs) throws SQLException {
        Map<String, ScreeningDtoBuilder> builders = new LinkedHashMap<>();

        while (rs.next()) {
            String screeningId = rs.getString("id");

            ScreeningDtoBuilder builder = builders.computeIfAbsent(screeningId, id -> {
                try {
                    return new ScreeningDtoBuilder(
                            id,
                            rs.getString("movie_id"),
                            rs.getString("room_id"),
                            rs.getObject("start_date_time", LocalDateTime.class)
                    );
                } catch (SQLException e) {
                    throw new RuntimeException("Error reading screening row", e);
                }
            });

            String seatId = rs.getString("seat_id");
            if (seatId != null) {
                ScreeningSeatDto seatDto = new ScreeningSeatDto(
                        seatId,
                        rs.getString("seat_row"),
                        rs.getString("seat_number"),
                        rs.getString("seat_type"),
                        rs.getString("status")
                );
                builder.seats.add(seatDto);
            }
        }

        return builders.values().stream()
                .map(ScreeningDtoBuilder::build)
                .toList();
    }

    private static class ScreeningDtoBuilder {
        private final String id;
        private final String movieId;
        private final String roomId;
        private final LocalDateTime startDateTime;
        private final List<ScreeningSeatDto> seats = new ArrayList<>();

        public ScreeningDtoBuilder(String id, String movieId, String roomId, LocalDateTime startDateTime) {
            this.id = id;
            this.movieId = movieId;
            this.roomId = roomId;
            this.startDateTime = startDateTime;
        }

        public ScreeningDto build() {
            return new ScreeningDto(id, movieId, roomId, startDateTime.toString(), seats);
        }
    }
}