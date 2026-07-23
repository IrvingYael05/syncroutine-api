package com.sync_routine.back_end.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "vinculacion_reloj")
@Data
public class VinculacionReloj {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(unique = true, nullable = false, length = 6)
    private String pin;

    @Column(name = "user_id")
    private UUID userId;

    @Column(length = 2048)
    private String token;

    @Column(name = "refresh_token", length = 2048)
    private String refreshToken;

    @Column(nullable = false)
    private String status;

    @Column(name = "expires_at", nullable = false)
    private LocalDateTime expiresAt;
}