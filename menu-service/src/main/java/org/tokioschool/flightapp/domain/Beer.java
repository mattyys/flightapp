package org.tokioschool.flightapp.domain;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;
import org.tokioschool.flightapp.config.UUIDDocument;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(collection = "beers")
public class Beer extends UUIDDocument {

    private String name;
    private String style;
}
