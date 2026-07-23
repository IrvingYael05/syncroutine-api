package com.sync_routine.back_end.dto;

import lombok.Data;

@Data
public class VinculacionStatusDto {
    private String pin;
    private String status;
    private String token;
    private String refreshToken;
}