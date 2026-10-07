package com.cinema.catalog.domain.model;

import com.cinema.catalog.domain.exception.InvalidDurationException;

public record MovieDuration(int hours, int minutes) {
    private static final int MAX_HOURS = 12;
    private static final int MINUTES_IN_HOUR = 60;

    public MovieDuration {
        if (hours < 0 || minutes < 0) {
            throw new InvalidDurationException("Duration hours and minutes must not be negative");
        }
        if (hours == 0 && minutes == 0) {
            throw new InvalidDurationException("Duration cannot be zero");
        }

        if (hours > MAX_HOURS) {
            throw new InvalidDurationException("Duration hours cannot exceed " + MAX_HOURS);
        }

        if (minutes >= MINUTES_IN_HOUR) {
            throw new InvalidDurationException("Duration minutes must be less than " + MINUTES_IN_HOUR);
        }
    }

    public int totalMinutes() {
        return hours * MINUTES_IN_HOUR + minutes;
    }

    public static MovieDuration ofMinutes(int totalMinutes) {
        if (totalMinutes <= 0) {
            throw new InvalidDurationException("Total minutes must be greater than 0");
        }
        if(totalMinutes > MAX_HOURS * MINUTES_IN_HOUR) {
            throw new InvalidDurationException("Total minutes cannot exceed " + (MAX_HOURS * MINUTES_IN_HOUR));
        }

        return new MovieDuration(
                totalMinutes / MINUTES_IN_HOUR,
                totalMinutes % MINUTES_IN_HOUR
        );
    }
}
