package com.cinema.shared;

import jakarta.inject.Qualifier;
import java.lang.annotation.Retention;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Qualifier
@Retention(RUNTIME)
public @interface LockTimeout {}
