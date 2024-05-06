package com.example.pidev.DAO.dto;

import com.example.pidev.DAO.annotation.PasswordRepeatEqual;
import com.example.pidev.DAO.annotation.ValidEmail;
import com.example.pidev.DAO.annotation.ValidPassword;
import lombok.*;

import javax.validation.constraints.Size;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@PasswordRepeatEqual(
        passwordFieldFirst = "password",
        passwordFieldSecond = "passwordRepeat"
)
public class SignupDto {
    @ValidEmail
    private String email;

    @ValidPassword
    private String password;
    private String passwordRepeat;

    @Size(max = 64)
    private String firstName;

    @Size(max = 64)
    private String lastName;
    @Size(max= 256)
    private String profilePhoto;

    @Size(max = 256)
    private String coverPhoto;
}
