CREATE TABLE IF NOT EXISTS movies (
    id TEXT PRIMARY KEY,
    title TEXT NOT NULL,
    director_first_name TEXT NOT NULL,
    director_last_name TEXT NOT NULL,
    description TEXT NOT NULL,
    genre TEXT NOT NULL,
    duration_minutes INTEGER NOT NULL
);

CREATE TABLE IF NOT EXISTS cinemas (
    id TEXT PRIMARY KEY,
    name TEXT NOT NULL
);

CREATE TABLE IF NOT EXISTS rooms (
    id TEXT PRIMARY KEY,
    cinema_id TEXT NOT NULL,
    number TEXT NOT NULL,
    FOREIGN KEY (cinema_id) REFERENCES cinemas(id)
);

CREATE INDEX IF NOT EXISTS idx_rooms_cinema_id ON rooms(cinema_id);

CREATE TABLE IF NOT EXISTS seats (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    room_id TEXT NOT NULL,
    row TEXT NOT NULL,
    number TEXT NOT NULL,
    seat_type TEXT NOT NULL,
    FOREIGN KEY (room_id) REFERENCES rooms(id)
);

CREATE INDEX IF NOT EXISTS idx_seats_room_id ON seats(room_id);

CREATE TABLE IF NOT EXISTS screenings (
    id TEXT PRIMARY KEY,
    movie_id TEXT NOT NULL,
    room_id TEXT NOT NULL,
    start_date_time TEXT NOT NULL,
    FOREIGN KEY (movie_id) REFERENCES movies(id),
    FOREIGN KEY (room_id) REFERENCES rooms(id)
);

CREATE INDEX IF NOT EXISTS idx_screenings_movie_id ON screenings(movie_id);
CREATE INDEX IF NOT EXISTS idx_screenings_room_id ON screenings(room_id);

CREATE TABLE IF NOT EXISTS screening_seats (
    id TEXT PRIMARY KEY,
    screening_id TEXT NOT NULL,
    seat_row TEXT NOT NULL,
    seat_number TEXT NOT NULL,
    seat_type TEXT NOT NULL,
    status TEXT NOT NULL,
    locked_at TEXT,
    version INTEGER NOT NULL DEFAULT 1,
    FOREIGN KEY (screening_id) REFERENCES screenings(id)
);

CREATE INDEX IF NOT EXISTS idx_screening_seats_screening_id ON screening_seats(screening_id);
