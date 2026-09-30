package com.cinema.infrastructure.bus;

import com.cinema.application.ports.in.Query;

public interface QueryBus {
    <R, Q extends Query<R>> R execute(Q query);
}
