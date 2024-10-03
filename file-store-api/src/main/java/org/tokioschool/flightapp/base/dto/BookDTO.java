package org.tokioschool.flightapp.base.dto;

import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

import java.util.List;

@Builder
@Value
@Jacksonized
public class BookDTO {

    int id;
    String title;
    String genre;
    List<Integer> authorId;
}
