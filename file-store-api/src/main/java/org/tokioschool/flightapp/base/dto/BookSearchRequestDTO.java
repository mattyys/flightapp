package org.tokioschool.flightapp.base.dto;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class BookSearchRequestDTO {

    String genre;
    int page;
    int pageSize;
}
