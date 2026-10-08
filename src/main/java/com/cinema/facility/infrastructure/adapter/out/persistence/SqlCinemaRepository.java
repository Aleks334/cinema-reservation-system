package com.cinema.facility.infrastructure.adapter.out.persistence;

import com.cinema.facility.domain.port.CinemaRepository;
import com.cinema.facility.domain.model.Cinema;
import com.cinema.facility.domain.model.CinemaId;
import com.cinema.facility.domain.model.Room;
import com.cinema.facility.domain.model.RoomId;
import com.cinema.facility.domain.model.Seat;
import com.cinema.facility.domain.model.SeatType;
import com.google.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
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
    public SqlCinemaRepository(DataSource dataSource) {
        loadAllCinemasIntoCache(dataSource);
    }

    @Override
    public Optional<Cinema> findById(CinemaId cinemaId) {
        return Optional.ofNullable(cache.get(cinemaId.value()));
    }

    private void loadAllCinemasIntoCache(DataSource ds) {
        try(var conn = ds.getConnection()) {
            Map<UUID, Cinema> cinemas = new HashMap<>();
            Map<UUID, List<Room>> cinemaRooms = new HashMap<>();
            Map<UUID, List<Seat>> roomSeats = new HashMap<>();

            loadSeats(conn, roomSeats);
            loadRooms(conn, cinemaRooms, roomSeats);
            loadCinemas(conn, cinemas, cinemaRooms);

            cache.putAll(cinemas);
            LOGGER.debug("Loaded {} cinemas into cache", cache.size());
        } catch (SQLException e) {
            throw new RuntimeException("Failed to access database", e);
        }
    }

    private void loadSeats(Connection conn, Map<UUID, List<Seat>> roomSeats) {
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
        } catch(SQLException e) {
            throw new RuntimeException("Failed to load seats from database", e);
        }
    }

    private void loadRooms(Connection conn, Map<UUID, List<Room>> cinemaRooms,
                           Map<UUID, List<Seat>> roomSeats) {
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
        } catch(SQLException e) {
            throw new RuntimeException("Failed to load rooms from database", e);
        }
    }

    private void loadCinemas(Connection conn, Map<UUID, Cinema> cinemas,
                             Map<UUID, List<Room>> cinemaRooms) {
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
        } catch(SQLException e) {
            throw new RuntimeException("Failed to load cinemas from database", e);
        }
    }
}
