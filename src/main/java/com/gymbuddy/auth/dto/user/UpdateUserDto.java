package com.gymbuddy.auth.dto.user;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode
public class UpdateUserDto {
    private String fullName;
}
