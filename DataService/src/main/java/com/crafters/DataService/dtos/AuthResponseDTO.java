package com.crafters.DataService.dtos;

import com.crafters.DataService.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthResponseDTO {
    private String userId;
    private String name;
    private String email;
    private Role role;
    private String accessToken;

    @Builder.Default
    private Date createdAt = new Date();
}
