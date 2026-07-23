package com.sync_routine.back_end.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.ZonedDateTime;
import java.util.UUID;

@Entity
@Table(name = "historial")
@Data
public class Historial {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @Column(name = "actividad_id", nullable = false)
    private UUID actividadId;

    @Column(name = "tiempo_real_segundos", nullable = false)
    private Integer tiempoRealSegundos;

    @Column(name = "fecha_completado", insertable = false, updatable = false)
    private ZonedDateTime fechaCompletado;
}