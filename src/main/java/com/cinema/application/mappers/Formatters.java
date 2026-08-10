package com.cinema.application.mappers;

import java.time.format.DateTimeFormatter;

public final class Formatters {

    private Formatters() {}

    public static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE;
    public static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ISO_LOCAL_TIME;
}

