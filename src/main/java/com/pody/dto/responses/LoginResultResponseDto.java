package com.pody.dto.responses;

import lombok.Data;

import java.util.UUID;

@Data
public class LoginResultResponseDto {
    public UUID id;
    public String username;
    public String profileImageAddress;
}
