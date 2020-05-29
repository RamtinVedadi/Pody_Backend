package com.pody.dto.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.UUID;

@Data
public class LoginResultResponseDto {
    public UUID id;
    public String username;
    @JsonProperty("userTitle")
    public String title;
    public String profileImageAddress;
}
