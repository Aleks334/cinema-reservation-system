package com.cinema.infrastructure.adapter.in.web;

import io.javalin.Javalin;

public interface Controller {
    void register(Javalin app);
}
