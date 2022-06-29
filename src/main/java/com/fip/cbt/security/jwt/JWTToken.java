package com.fip.cbt.security.jwt;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain=true)
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class JWTToken {
    private String accessToken;
    private String refreshToken;
}
