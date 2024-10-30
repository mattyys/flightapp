package org.tokioschool.flightapp.domain;


import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class Main {

    private String name;
    private List<Ingredient> ingredients;
}
