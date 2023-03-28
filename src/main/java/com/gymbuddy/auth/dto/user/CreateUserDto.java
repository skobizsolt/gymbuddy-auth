package com.gymbuddy.auth.dto.user;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class CreateUserDto {
    private String username;
    private String email;
    private String password;
    private String matchingPassword;
}
