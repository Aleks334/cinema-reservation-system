package com.cinema.domain.model;

import com.cinema.catalog.domain.model.Director;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.assertThat;

class DirectorTest {

    @Test
    void shouldCreateValidDirector() {
        Director director = new Director("John", "Doe");

        assertThat(director.getFullName()).isEqualTo("John Doe");
    }

    @ParameterizedTest
    @CsvSource({
            ", Doe",
            "John, ",
            "'', Doe",
            "'   ', Doe",
            "John, ''",
            "John, '   '"
    })
    void shouldRejectInvalidNames(String firstName, String lastName) {
        assertThatThrownBy(() -> new Director(firstName, lastName))
                .isInstanceOf(RuntimeException.class);
    }
}
