package com.crafters.DataService.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VerifyResponseDTO {
    private String email;
    private String accessToken;

    @Builder.Default
    private Date createdAt = new Date();
}
