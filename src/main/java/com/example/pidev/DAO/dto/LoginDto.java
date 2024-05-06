package com.example.pidev.DAO.dto;

import com.example.pidev.DAO.annotation.ValidEmail;
import com.example.pidev.DAO.annotation.ValidPassword;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginDto {
    @ValidEmail
    private String email;

    @ValidPassword
    private String password;
}
