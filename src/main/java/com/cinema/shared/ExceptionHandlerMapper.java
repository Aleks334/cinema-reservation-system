package com.cinema.shared;

import io.javalin.Javalin;

public interface ExceptionHandlerMapper {
    void register(Javalin app);
}
