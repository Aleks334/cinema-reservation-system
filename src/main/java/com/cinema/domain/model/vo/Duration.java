package com.cinema.domain.model.vo;

import com.cinema.domain.exception.InvalidDurationException;

public record Duration(int hours, int minutes) {
    private static final int MAX_HOURS = 12;
    private static final int MINUTES_IN_HOUR = 60;

    public Duration {
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

    public static Duration ofMinutes(int totalMinutes) {
        if (totalMinutes <= 0) {
            throw new InvalidDurationException("Total minutes must be greater than 0");
        }
        if(totalMinutes > MAX_HOURS * MINUTES_IN_HOUR) {
            throw new InvalidDurationException("Total minutes cannot exceed " + (MAX_HOURS * MINUTES_IN_HOUR));
        }

        return new Duration(
                totalMinutes / MINUTES_IN_HOUR,
                totalMinutes % MINUTES_IN_HOUR
        );
    }
}
