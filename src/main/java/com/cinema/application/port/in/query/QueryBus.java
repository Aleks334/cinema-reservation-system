package com.cinema.application.port.in.query;

public interface QueryBus {
    <R, Q extends Query<R>> void register(Class<Q> queryClass, QueryHandler<Q, R> handler);
    <R, Q extends Query<R>> R execute(Q query);
}
