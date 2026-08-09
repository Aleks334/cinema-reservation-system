package com.cinema.application.port.in.dto;

public record MovieDto(
        String id,
        String title,
        String directorFullName,
        String description,
        String genre,
        int durationMinutes
) {
}
