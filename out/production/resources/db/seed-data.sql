DELETE FROM screening_seats;
DELETE FROM screenings;
DELETE FROM seats;
DELETE FROM rooms;
DELETE FROM cinemas;
DELETE FROM movies;

INSERT INTO movies (id, title, director_first_name, director_last_name, description, genre, duration_minutes) VALUES
('550e8400-e29b-41d4-a716-446655440001', 'Inception', 'Christopher', 'Nolan', 'A thief who steals corporate secrets through dream-sharing technology is given the inverse task of planting an idea.', 'Science Fiction', 148),
('550e8400-e29b-41d4-a716-446655440002', 'The Matrix', 'Lana', 'Wachowski', 'A computer hacker learns from mysterious rebels about the true nature of his reality and his role in the war against its controllers.', 'Science Fiction', 136),
('550e8400-e29b-41d4-a716-446655440003', 'Interstellar', 'Christopher', 'Nolan', 'A team of explorers travel through a wormhole in space in an attempt to ensure humanity survival.', 'Science Fiction', 169);

INSERT INTO cinemas (id, name) VALUES
('650e8400-e29b-41d4-a716-446655440001', 'CinemaCity');

INSERT INTO rooms (id, cinema_id, number) VALUES
('750e8400-e29b-41d4-a716-446655440001', '650e8400-e29b-41d4-a716-446655440001', '1'),
('750e8400-e29b-41d4-a716-446655440002', '650e8400-e29b-41d4-a716-446655440001', '2');

INSERT INTO seats (room_id, row, number, seat_type) VALUES
('750e8400-e29b-41d4-a716-446655440001', 'A', '1', 'BASIC'),
('750e8400-e29b-41d4-a716-446655440001', 'A', '2', 'BASIC'),
('750e8400-e29b-41d4-a716-446655440001', 'A', '3', 'BASIC'),
('750e8400-e29b-41d4-a716-446655440001', 'A', '4', 'BASIC'),
('750e8400-e29b-41d4-a716-446655440001', 'B', '1', 'COMFORT'),
('750e8400-e29b-41d4-a716-446655440001', 'B', '2', 'COMFORT'),
('750e8400-e29b-41d4-a716-446655440001', 'B', '3', 'COMFORT'),
('750e8400-e29b-41d4-a716-446655440001', 'B', '4', 'COMFORT'),
('750e8400-e29b-41d4-a716-446655440001', 'C', '1', 'VIP'),
('750e8400-e29b-41d4-a716-446655440001', 'C', '2', 'VIP'),
('750e8400-e29b-41d4-a716-446655440002', 'A', '1', 'BASIC'),
('750e8400-e29b-41d4-a716-446655440002', 'A', '2', 'BASIC'),
('750e8400-e29b-41d4-a716-446655440002', 'A', '3', 'BASIC'),
('750e8400-e29b-41d4-a716-446655440002', 'B', '1', 'COMFORT'),
('750e8400-e29b-41d4-a716-446655440002', 'B', '2', 'COMFORT'),
('750e8400-e29b-41d4-a716-446655440002', 'B', '3', 'COMFORT');

INSERT INTO screenings (id, movie_id, room_id, start_date_time) VALUES
('850e8400-e29b-41d4-a716-446655440001', '550e8400-e29b-41d4-a716-446655440001', '750e8400-e29b-41d4-a716-446655440001', '2025-12-20T18:00:00+01:00'),
('850e8400-e29b-41d4-a716-446655440002', '550e8400-e29b-41d4-a716-446655440001', '750e8400-e29b-41d4-a716-446655440001', '2025-12-20T21:00:00+01:00'),
('850e8400-e29b-41d4-a716-446655440003', '550e8400-e29b-41d4-a716-446655440002', '750e8400-e29b-41d4-a716-446655440002', '2025-12-21T19:30:00+01:00'),
('850e8400-e29b-41d4-a716-446655440004', '550e8400-e29b-41d4-a716-446655440003', '750e8400-e29b-41d4-a716-446655440001', '2025-12-22T17:00:00+01:00');

INSERT INTO screening_seats (id, screening_id, seat_row, seat_number, seat_type, status, locked_at, version) VALUES
('950e8400-e29b-41d4-a716-446655440001', '850e8400-e29b-41d4-a716-446655440001', 'A', '1', 'BASIC', 'AVAILABLE', NULL, 1),
('950e8400-e29b-41d4-a716-446655440002', '850e8400-e29b-41d4-a716-446655440001', 'A', '2', 'BASIC', 'AVAILABLE', NULL, 1),
('950e8400-e29b-41d4-a716-446655440003', '850e8400-e29b-41d4-a716-446655440001', 'A', '3', 'BASIC', 'AVAILABLE', NULL, 1),
('950e8400-e29b-41d4-a716-446655440004', '850e8400-e29b-41d4-a716-446655440001', 'A', '4', 'BASIC', 'AVAILABLE', NULL, 1),
('950e8400-e29b-41d4-a716-446655440005', '850e8400-e29b-41d4-a716-446655440001', 'B', '1', 'COMFORT', 'AVAILABLE', NULL, 1),
('950e8400-e29b-41d4-a716-446655440006', '850e8400-e29b-41d4-a716-446655440001', 'B', '2', 'COMFORT', 'AVAILABLE', NULL, 1),
('950e8400-e29b-41d4-a716-446655440007', '850e8400-e29b-41d4-a716-446655440001', 'B', '3', 'COMFORT', 'AVAILABLE', NULL, 1),
('950e8400-e29b-41d4-a716-446655440008', '850e8400-e29b-41d4-a716-446655440001', 'B', '4', 'COMFORT', 'AVAILABLE', NULL, 1),
('950e8400-e29b-41d4-a716-446655440009', '850e8400-e29b-41d4-a716-446655440001', 'C', '1', 'VIP', 'AVAILABLE', NULL, 1),
('950e8400-e29b-41d4-a716-446655440010', '850e8400-e29b-41d4-a716-446655440001', 'C', '2', 'VIP', 'AVAILABLE', NULL, 1);
