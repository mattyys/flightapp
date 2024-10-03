package org.tokioschool.flightapp.base.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Value
@Builder
@Jacksonized
public class BookRequestDTO {

    @NotBlank String title;

    @NotBlank String genre;

    @Positive @NotNull Integer authorId;

}
