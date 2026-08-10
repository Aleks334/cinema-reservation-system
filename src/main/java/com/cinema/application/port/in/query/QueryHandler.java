package com.cinema.application.port.in.query;

@FunctionalInterface
public interface QueryHandler<Q extends Query<R>, R> {
    R handle(Q query);
}
