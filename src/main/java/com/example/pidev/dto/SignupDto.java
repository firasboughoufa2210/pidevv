package com.example.pidev.dto;

import com.example.pidev.annotation.PasswordRepeatEqual;
import com.example.pidev.annotation.ValidEmail;
import com.example.pidev.annotation.ValidPassword;
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
