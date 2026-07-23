package com.sync_routine.back_end.dto;

import lombok.Data;
import java.util.UUID;

@Data
public class BloqueDto {
    private UUID id;
    private UUID userId;
    private String nombre;
    private Boolean esAleatorio;
}