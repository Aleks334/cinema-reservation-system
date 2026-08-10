DELETE
FROM screening_seats;
DELETE
FROM screenings;
DELETE
FROM seats;
DELETE
FROM rooms;
DELETE
FROM cinemas;
DELETE
FROM movies;

INSERT INTO movies (id, title, director_first_name, director_last_name, description, genre, duration_minutes)
VALUES ('8b2f9f8e-d9a2-4c2d-9e1f-6a5b4c3d2e1f', 'Inception', 'Christopher', 'Nolan',
        'A thief who steals corporate secrets through dream-sharing technology is given the inverse task of planting an idea.',
        'Science Fiction', 148),
       ('a1d7f4b3-c9e2-4d3e-af20-7b6c5d4e3f20', 'The Matrix', 'Lana', 'Wachowski',
        'A computer hacker learns from mysterious rebels about the true nature of his reality and his role in the war against its controllers.',
        'Science Fiction', 136),
       ('f3e8c9d4-1a2b-4e4f-b031-8c7d6e5f4031', 'Interstellar', 'Christopher', 'Nolan',
        'A team of explorers travel through a wormhole in space in an attempt to ensure humanity survival.',
        'Science Fiction', 169);

INSERT INTO cinemas (id, name)
VALUES ('c1a2b3d4-e5f6-4a5b-8c7d-9e0f1a2b3c4d', 'CinemaCity');

INSERT INTO rooms (id, cinema_id, number)
VALUES ('b2c3d4e5-f6a7-4b6c-9d8e-0f1a2b3c4d5e', 'c1a2b3d4-e5f6-4a5b-8c7d-9e0f1a2b3c4d', '1'),
       ('c3d4e5f6-a7b8-4c7d-ae9f-1a2b3c4d5e6f', 'c1a2b3d4-e5f6-4a5b-8c7d-9e0f1a2b3c4d', '2');

INSERT INTO seats (room_id, row, number, seat_type)
VALUES ('b2c3d4e5-f6a7-4b6c-9d8e-0f1a2b3c4d5e', 'A', '1', 'BASIC'),
       ('b2c3d4e5-f6a7-4b6c-9d8e-0f1a2b3c4d5e', 'A', '2', 'BASIC'),
       ('b2c3d4e5-f6a7-4b6c-9d8e-0f1a2b3c4d5e', 'A', '3', 'BASIC'),
       ('b2c3d4e5-f6a7-4b6c-9d8e-0f1a2b3c4d5e', 'A', '4', 'BASIC'),
       ('b2c3d4e5-f6a7-4b6c-9d8e-0f1a2b3c4d5e', 'B', '1', 'COMFORT'),
       ('b2c3d4e5-f6a7-4b6c-9d8e-0f1a2b3c4d5e', 'B', '2', 'COMFORT'),
       ('b2c3d4e5-f6a7-4b6c-9d8e-0f1a2b3c4d5e', 'B', '3', 'COMFORT'),
       ('b2c3d4e5-f6a7-4b6c-9d8e-0f1a2b3c4d5e', 'B', '4', 'COMFORT'),
       ('b2c3d4e5-f6a7-4b6c-9d8e-0f1a2b3c4d5e', 'C', '1', 'VIP'),
       ('b2c3d4e5-f6a7-4b6c-9d8e-0f1a2b3c4d5e', 'C', '2', 'VIP'),
       ('c3d4e5f6-a7b8-4c7d-ae9f-1a2b3c4d5e6f', 'A', '1', 'BASIC'),
       ('c3d4e5f6-a7b8-4c7d-ae9f-1a2b3c4d5e6f', 'A', '2', 'BASIC'),
       ('c3d4e5f6-a7b8-4c7d-ae9f-1a2b3c4d5e6f', 'A', '3', 'BASIC'),
       ('c3d4e5f6-a7b8-4c7d-ae9f-1a2b3c4d5e6f', 'B', '1', 'COMFORT'),
       ('c3d4e5f6-a7b8-4c7d-ae9f-1a2b3c4d5e6f', 'B', '2', 'COMFORT'),
       ('c3d4e5f6-a7b8-4c7d-ae9f-1a2b3c4d5e6f', 'B', '3', 'COMFORT');

INSERT INTO screenings (id, movie_id, room_id, start_date_time)
VALUES ('1a2b3c4d-5e6f-4a1b-8c2d-3e4f5a6b7c8d', '8b2f9f8e-d9a2-4c2d-9e1f-6a5b4c3d2e1f',
        'b2c3d4e5-f6a7-4b6c-9d8e-0f1a2b3c4d5e', '2025-12-20T18:00:00+01:00'),
       ('2b3c4d5e-6f7a-4b2c-9d3e-4f5a6b7c8d9e', '8b2f9f8e-d9a2-4c2d-9e1f-6a5b4c3d2e1f',
        'b2c3d4e5-f6a7-4b6c-9d8e-0f1a2b3c4d5e', '2025-12-20T21:00:00+01:00'),
       ('3c4d5e6f-7a8b-4c3d-ae4f-5a6b7c8d9e0f', 'a1d7f4b3-c9e2-4d3e-af20-7b6c5d4e3f20',
        'c3d4e5f6-a7b8-4c7d-ae9f-1a2b3c4d5e6f', '2025-12-21T19:30:00+01:00'),
       ('4d5e6f7a-8b9c-4d4e-bf50-6b7c8d9e0f1a', 'f3e8c9d4-1a2b-4e4f-b031-8c7d6e5f4031',
        'b2c3d4e5-f6a7-4b6c-9d8e-0f1a2b3c4d5e', '2025-12-22T17:00:00+01:00');

INSERT INTO screening_seats (id, screening_id, seat_row, seat_number, seat_type, status, locked_at, version)
VALUES ('5e6f7a8b-9c0d-4e5f-c061-7c8d9e0f1a2b', '1a2b3c4d-5e6f-4a1b-8c2d-3e4f5a6b7c8d', 'A', '1', 'BASIC', 'AVAILABLE',
        NULL, 1),
       ('6f7a8b9c-0d1e-4f60-d172-8d9e0f1a2b3c', '1a2b3c4d-5e6f-4a1b-8c2d-3e4f5a6b7c8d', 'A', '2', 'BASIC', 'AVAILABLE',
        NULL, 1),
       ('7a8b9c0d-1e2f-4071-e283-9e0f1a2b3c4d', '1a2b3c4d-5e6f-4a1b-8c2d-3e4f5a6b7c8d', 'A', '3', 'BASIC', 'AVAILABLE',
        NULL, 1),
       ('8b9c0d1e-2f30-4182-f394-0f1a2b3c4d5e', '1a2b3c4d-5e6f-4a1b-8c2d-3e4f5a6b7c8d', 'A', '4', 'BASIC', 'AVAILABLE',
        NULL, 1),
       ('9c0d1e2f-3041-4293-04a5-1a2b3c4d5e6f', '1a2b3c4d-5e6f-4a1b-8c2d-3e4f5a6b7c8d', 'B', '1', 'COMFORT',
        'AVAILABLE', NULL, 1),
       ('0d1e2f30-4152-43a4-15b6-2b3c4d5e6f7a', '1a2b3c4d-5e6f-4a1b-8c2d-3e4f5a6b7c8d', 'B', '2', 'COMFORT',
        'AVAILABLE', NULL, 1),
       ('1e2f3041-5263-44b5-26c7-3c4d5e6f7a8b', '1a2b3c4d-5e6f-4a1b-8c2d-3e4f5a6b7c8d', 'B', '3', 'COMFORT',
        'AVAILABLE', NULL, 1),
       ('2f304152-6374-45c6-37d8-4d5e6f7a8b9c', '1a2b3c4d-5e6f-4a1b-8c2d-3e4f5a6b7c8d', 'B', '4', 'COMFORT',
        'AVAILABLE', NULL, 1),
       ('30415263-7485-46d7-48e9-5e6f7a8b9c0d', '1a2b3c4d-5e6f-4a1b-8c2d-3e4f5a6b7c8d', 'C', '1', 'VIP', 'AVAILABLE',
        NULL, 1),
       ('41526374-8596-47e8-59fa-6f7a8b9c0d1e', '1a2b3c4d-5e6f-4a1b-8c2d-3e4f5a6b7c8d', 'C', '2', 'VIP', 'AVAILABLE',
        NULL, 1);