package com.gymbuddy.auth.persistence.domain.auth;

import com.gymbuddy.auth.dto.TokenType;
import com.gymbuddy.auth.persistence.domain.User;
import com.gymbuddy.auth.util.TokenUtils;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@Entity(name = "auth_token")
@NoArgsConstructor
@AllArgsConstructor
public class AuthenticationToken {

    private static final int EXPIRATION = 60 * 24;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long tokenId;
    private String token;
    private Date expiryDate;
    @Enumerated(EnumType.STRING)
    private TokenType tokenType;
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "userId",
            referencedColumnName = "userId",
            nullable = false,
            foreignKey = @ForeignKey(name = "FK_USER_TOKEN"))
    private User user;

    public AuthenticationToken(final String token, final User user) {
        super();
        this.token = token;
        this.user = user;
        this.expiryDate = TokenUtils.calculateExpiryDate(EXPIRATION);
    }

    public AuthenticationToken(final String token) {
        super();
        this.token = token;
        this.expiryDate = TokenUtils.calculateExpiryDate(EXPIRATION);
    }
}
