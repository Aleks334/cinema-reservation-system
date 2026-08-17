package com.cinema.infrastructure.bus;

import com.cinema.application.port.in.query.Query;
import com.cinema.application.port.in.query.QueryBus;
import com.cinema.application.port.in.query.QueryHandler;

import java.util.HashMap;
import java.util.Map;

public class InMemoryQueryBus implements QueryBus {
    private final Map<Class<? extends Query<?>>, QueryHandler<?, ?>> registry = new HashMap<>();

    @Override
    public <R, Q extends Query<R>> void register(Class<Q> queryClass, QueryHandler<Q, R> handler) {
        registry.put(queryClass, handler);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <R, Q extends Query<R>> R execute(Q query) {
        QueryHandler<Q, R> handler = (QueryHandler<Q, R>) registry.get(query.getClass());
        if(handler == null) {
            throw new IllegalArgumentException("No handler registered for " + query.getClass().getName());
        }

        return handler.handle(query);
    }
}
