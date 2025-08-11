package com.services.ServiceProvider.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.Instant;

@Entity
@Table(name="refresh_tokens")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Refresh token must not be blank")
    @Column(nullable = false, unique = true)
    private String refreshToken;

    @NotNull(message = "Expiry must not be null")
    @Column(nullable = false)
    private Instant expiry;

    @NotNull(message = "User must not be null")
    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private User user;

}