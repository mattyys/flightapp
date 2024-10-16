package org.tokioschool.flightapp.flight.store.config.service;

import org.springframework.lang.Nullable;

public interface AuthService {

    @Nullable
    String getAccessToken();
}
