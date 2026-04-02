package org.example.anisdoufback.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TokenResponse implements Serializable {
    private String accessToken;
    private String tokenType;
}
