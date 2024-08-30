package org.tokioschool.flightapp.flight.store.domain;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class ResourceDescription {

    String contentType;
    String filename;
    String description;
    int size;
}
