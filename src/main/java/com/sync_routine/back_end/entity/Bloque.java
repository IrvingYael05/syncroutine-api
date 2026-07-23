package com.sync_routine.back_end.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.ZonedDateTime;
import java.util.UUID;

@Entity
@Table(name = "bloques")
@Data
public class Bloque {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    // Relación con el Perfil
    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @Column(nullable = false)
    private String nombre;

    @Column(name = "es_aleatorio")
    private Boolean esAleatorio = false;

    @Column(name = "created_at", insertable = false, updatable = false)
    private ZonedDateTime createdAt;
}