package com.cinema.application.port.in;

public interface QueryBus {
    <R, Q extends Query<R>> R execute(Q query);
}
