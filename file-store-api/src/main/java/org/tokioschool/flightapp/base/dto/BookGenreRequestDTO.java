package org.tokioschool.flightapp.base.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Value
@Builder
@Jacksonized
public class BookGenreRequestDTO {


    @NotBlank String genre;
}
