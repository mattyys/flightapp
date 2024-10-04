package org.tokioschool.flightapp.store.dto;

import lombok.Builder;
import lombok.Value;

import java.util.UUID;

@Value
@Builder
public class ResourceIdDTO {

    UUID resourceId;

}
