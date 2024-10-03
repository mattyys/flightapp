package org.tokioschool.flightapp.flight.store.domain;

import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Value
@Builder
@Jacksonized
public class ResourceDescription {

    String contentType;
    String filename;
    String description;
    int size;
}
