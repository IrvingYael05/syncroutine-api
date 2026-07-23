package com.sync_routine.back_end.dto;

import lombok.Data;
import java.time.ZonedDateTime;
import java.util.UUID;

@Data
public class HistorialDto {
    private UUID id;
    private UUID userId;
    private UUID actividadId;
    private Integer tiempoRealSegundos;
    private ZonedDateTime fechaCompletado;
}