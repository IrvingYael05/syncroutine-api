package com.sync_routine.back_end.dto;

import lombok.Data;
import java.util.UUID;

@Data
public class VincularRequestDto {
    private String pin;
    private UUID userId;
    private String token;
    private String refreshToken;
}