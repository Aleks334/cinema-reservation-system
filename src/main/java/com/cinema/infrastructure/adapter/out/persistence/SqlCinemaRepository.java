package com.cinema.infrastructure.adapter.out.persistence;

import com.cinema.application.ports.out.CinemaRepository;
import com.cinema.domain.model.facility.Cinema;
import com.cinema.domain.model.facility.CinemaId;
import com.cinema.domain.model.facility.Room;
import com.cinema.domain.model.facility.RoomId;
import com.cinema.domain.model.facility.Seat;
import com.cinema.domain.model.facility.SeatType;
import com.google.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public final class SqlCinemaRepository implements CinemaRepository {
    private static final Logger LOGGER = LoggerFactory.getLogger(SqlCinemaRepository.class);
    private final Map<UUID, Cinema> cache = new ConcurrentHashMap<>();

    @Inject
    public SqlCinemaRepository(Connection connection) {
        loadAllCinemasIntoCache(connection);
    }

    @Override
    public Optional<Cinema> findById(CinemaId cinemaId) {
        return Optional.ofNullable(cache.get(cinemaId.value()));
    }

    private void loadAllCinemasIntoCache(Connection conn) {
        try {
            Map<UUID, Cinema> cinemas = new HashMap<>();
            Map<UUID, List<Room>> cinemaRooms = new HashMap<>();
            Map<UUID, List<Seat>> roomSeats = new HashMap<>();

            loadSeats(conn, roomSeats);
            loadRooms(conn, cinemaRooms, roomSeats);
            loadCinemas(conn, cinemas, cinemaRooms);

            cache.putAll(cinemas);
            LOGGER.debug("Loaded {} cinemas into cache", cache.size());
        } catch (SQLException e) {
            throw new RuntimeException("Failed to load cinemas from database", e);
        }
    }

    private void loadSeats(Connection conn, Map<UUID, List<Seat>> roomSeats) throws SQLException {
        String sql = """
                      SELECT room_id, row, number, seat_type
                      FROM seats;
                      """;

        try (var stmt = conn.createStatement();
             var rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                UUID roomId = UUID.fromString(rs.getString("room_id"));
                Seat seat = new Seat(
                        rs.getString("row"),
                        rs.getString("number"),
                        SeatType.valueOf(rs.getString("seat_type"))
                );

                roomSeats.computeIfAbsent(roomId, k -> new ArrayList<>()).add(seat);
            }
        }
    }

    private void loadRooms(Connection conn, Map<UUID, List<Room>> cinemaRooms,
                           Map<UUID, List<Seat>> roomSeats) throws SQLException {
        String sql = """
                      SELECT id, cinema_id, number
                      FROM rooms;
                      """;

        try (var stmt = conn.createStatement();
             var rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                UUID roomId = UUID.fromString(rs.getString("id"));
                UUID cinemaId = UUID.fromString(rs.getString("cinema_id"));

                Room room = new Room(
                        new RoomId(roomId),
                        rs.getString("number"),
                        roomSeats.getOrDefault(roomId, List.of())
                );

                cinemaRooms.computeIfAbsent(cinemaId, k -> new ArrayList<>()).add(room);
            }
        }
    }

    private void loadCinemas(Connection conn, Map<UUID, Cinema> cinemas,
                             Map<UUID, List<Room>> cinemaRooms) throws SQLException {
        String sql = """
                      SELECT id, name
                      FROM cinemas;
                      """;

        try (var stmt = conn.createStatement();
             var rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                UUID cinemaId = UUID.fromString(rs.getString("id"));

                Cinema cinema = new Cinema(
                        new CinemaId(cinemaId),
                        rs.getString("name"),
                        cinemaRooms.getOrDefault(cinemaId, List.of())
                );

                cinemas.put(cinemaId, cinema);
            }
        }
    }
}
