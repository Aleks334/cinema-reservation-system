package com.cinema.application.port.out;

import com.cinema.domain.model.Screening;

public interface SaveScreeningPort {
    void save(Screening screening);
}
