package com.cinema.infrastructure.bus;

import com.cinema.application.ports.in.Query;
import com.cinema.application.ports.in.QueryHandler;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import java.util.Map;

@Singleton
public class InMemoryQueryBus implements QueryBus {
    private final Map<Class<? extends Query<?>>, QueryHandler<?, ?>> queryHandlers;

    @Inject
    public InMemoryQueryBus(Map<Class<? extends Query<?>>, QueryHandler<?, ?>> queryHandlers) {
        this.queryHandlers = queryHandlers;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <R, Q extends Query<R>> R execute(Q query) {
        QueryHandler<Q, R> handler = (QueryHandler<Q, R>) queryHandlers.get(query.getClass());
        if(handler == null) {
            throw new IllegalArgumentException("No handler registered for " + query.getClass().getName());
        }

        return handler.handle(query);
    }
}
