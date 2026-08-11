package com.cinema.infrastructure.adapter.out.persistence.mapping;

import com.cinema.domain.model.Movie;
import com.cinema.domain.model.MovieGenre;
import com.cinema.domain.model.vo.Director;
import com.cinema.domain.model.vo.MovieDuration;
import com.cinema.domain.model.vo.MovieId;

import java.sql.ResultSet;
import java.sql.SQLException;

public final class MovieMapper {
    private MovieMapper() {}

    public static Movie toMovie(ResultSet rs) throws SQLException {
        return new Movie(
                MovieId.from(rs.getString("id")),
                rs.getString("title"),
                new Director(
                        rs.getString("director_first_name"),
                        rs.getString("director_last_name")
                ),
                rs.getString("description"),
                MovieGenre.fromDisplayName(rs.getString("genre")),
                MovieDuration.ofMinutes(rs.getInt("duration_minutes"))
        );
    }
}
