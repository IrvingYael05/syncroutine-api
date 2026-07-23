package com.sync_routine.back_end.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.ZonedDateTime;
import java.util.UUID;

@Entity
@Table(name = "actividades")
@Data
public class Actividad {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "bloque_id", nullable = false)
    private UUID bloqueId;

    @Column(nullable = false)
    private String nombre;

    @Column(name = "tiempo_objetivo_segundos", nullable = false)
    private Integer tiempoObjetivoSegundos;

    @Column(nullable = false)
    private Integer orden;

    @Column(name = "created_at", insertable = false, updatable = false)
    private ZonedDateTime createdAt;
}