package com.crafters.DataService.dtos;

import com.crafters.DataService.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SignUpRequestDTO {
    private String name;
    private String email;
    private String password;
    private Role role;
}
