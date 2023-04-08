package com.gymbuddy.auth.dto.user;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
public class UserDto extends MinimalUserDto {
    private String fullName;
    private LocalDate registrationDate;
}
