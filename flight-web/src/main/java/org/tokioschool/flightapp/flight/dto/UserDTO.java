package org.tokioschool.flightapp.flight.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDTO {

    private String id;

    private String name;

    private String surname;

    private String email;

    private LocalDateTime lastLogin;

    private List<String> roles;

}
