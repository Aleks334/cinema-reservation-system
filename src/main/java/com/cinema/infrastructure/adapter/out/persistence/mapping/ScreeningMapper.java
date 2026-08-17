package com.cinema.infrastructure.adapter.out.persistence.mapping;

import com.cinema.domain.model.ticketing.*;
import com.cinema.domain.model.catalog.MovieId;
import com.cinema.domain.model.facility.RoomId;
import com.cinema.domain.model.facility.Seat;
import com.cinema.domain.model.facility.SeatType;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public final class ScreeningMapper {
    private ScreeningMapper() {}

    public static Optional<Screening> toScreening(ResultSet rs) throws SQLException {
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
                seats.add(ScreeningMapper.toScreeningSeat(rs));
            }
        } while (rs.next());

        return Optional.of(new Screening(screeningId, movieId, roomId, startDateTime, seats));
    }

    public static List<Screening> toScreenings(ResultSet rs) throws SQLException {
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
                currentSeats.add(ScreeningMapper.toScreeningSeat(rs));
            }
        }

        if (currentScreening != null) {
            screenings.add(currentScreening);
        }

        return screenings;
    }

    public static ScreeningSeat toScreeningSeat(ResultSet rs) throws SQLException {
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
                ScreeningSeatStatus.valueOf(rs.getString("status")),
                lockedAt,
                rs.getInt("version")
        );
    }
}
