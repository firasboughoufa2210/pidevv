package com.example.pidev.dto;

import com.example.pidev.annotation.ValidEmail;
import com.example.pidev.annotation.ValidPassword;
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
