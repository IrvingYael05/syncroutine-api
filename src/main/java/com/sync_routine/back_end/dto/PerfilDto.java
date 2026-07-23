package com.sync_routine.back_end.dto;

import lombok.Data;
import java.util.UUID;

@Data
public class PerfilDto {
    private UUID id;
    private String nombre;
}