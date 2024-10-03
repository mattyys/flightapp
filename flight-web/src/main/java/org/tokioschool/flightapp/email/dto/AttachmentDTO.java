package org.tokioschool.flightapp.email.dto;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class AttachmentDTO {

    String filename;
    String contentType;
    byte[] content;

}
