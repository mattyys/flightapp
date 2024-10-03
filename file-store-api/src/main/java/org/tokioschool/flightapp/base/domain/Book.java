package org.tokioschool.flightapp.base.domain;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class Book {

    private Integer id;
    private String title;
    private String genre;
    private List<Author> authors;
}
