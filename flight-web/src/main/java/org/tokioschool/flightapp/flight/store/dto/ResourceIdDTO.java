package org.tokioschool.flightapp.flight.store.dto;

import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

import java.util.UUID;

@Value
@Builder
@Jacksonized
public class ResourceIdDTO {

    UUID resourceId;
}
