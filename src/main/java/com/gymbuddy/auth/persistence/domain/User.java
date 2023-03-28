package com.gymbuddy.auth.persistence.domain;

import com.gymbuddy.auth.dto.AuthRoles;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity(name = "users")
@Getter
@Setter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;
    private String fullName;
    @Column(unique = true)
    @NotNull
    private String username;
    @NotNull
    @Email
    private String email;
    @NotNull
    @Column(length = 60)
    @Size(min = 8, message = "Minimum of 8 characters required")
    private String password;
    @Enumerated(EnumType.STRING)
    private AuthRoles role;
    @NotNull
    @PastOrPresent
    private LocalDate registrationDate;
    private Boolean enabled = false;
}
