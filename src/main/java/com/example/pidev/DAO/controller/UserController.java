package com.example.pidev.DAO.controller;

import com.example.pidev.DAO.common.UserPrincipal;
import com.example.pidev.DAO.dto.*;
import com.example.pidev.dto.*;
import com.example.pidev.DAO.service.JwtTokenService;
import com.example.pidev.DAO.service.UserService;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.example.pidev.DAO.common.AppConstants;
import com.pieya.dto.*;
import com.example.pidev.DAO.entity.User;
import com.example.pidev.DAO.response.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.token.TokenService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.google.api.client.json.jackson2.JacksonFactory;

import javax.validation.Valid;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;


@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final JwtTokenService jwtTokenService;
    private final AuthenticationManager authenticationManager;
    private TokenService tokenService;
    private String email;
    @Value("${google.id}")
    private String googleClientId;



    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody @Valid SignupDto signupDto) {
        User savedUser = userService.createNewUser(signupDto);
        return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid LoginDto loginDto) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginDto.getEmail(), loginDto.getPassword())
        );
        User loginUser = userService.getUserByEmail(loginDto.getEmail());
        UserPrincipal userPrincipal = new UserPrincipal(loginUser);
        HttpHeaders newHttpHeaders = new HttpHeaders();
        newHttpHeaders.add(AppConstants.TOKEN_HEADER, jwtTokenService.generateToken(userPrincipal));
        return new ResponseEntity<>(loginUser, newHttpHeaders, HttpStatus.OK);
    }

    @GetMapping("/profile")
    public ResponseEntity<?> showUserProfile(Authentication authentication) {
        User user = userService.getUserByEmail(authentication.getPrincipal().toString());
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PostMapping("/account/update/info")
    public ResponseEntity<?> updateUserInfo(@RequestBody @Valid UpdateUserInfoDto updateUserInfoDto) {
        User updatedUser = userService.updateUserInfo(updateUserInfoDto);
        return new ResponseEntity<>(updatedUser, HttpStatus.OK);
    }

    @PostMapping("/account/update/email")
    public ResponseEntity<?> updateUserEmail(@RequestBody @Valid UpdateEmailDto updateEmailDto) {
        userService.updateEmail(updateEmailDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/account/update/password")
    public ResponseEntity<?> updateUserPassword(@RequestBody @Valid UpdatePasswordDto updatePasswordDto) {
        userService.updatePassword(updatePasswordDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/account/update/profile-photo")
    public ResponseEntity<?> updateProfilePhoto(@RequestParam("profilePhoto") MultipartFile profilePhoto) {
        User updatedUser = userService.updateProfilePhoto(profilePhoto);
        return new ResponseEntity<>(updatedUser, HttpStatus.OK);
    }

    @PostMapping("/account/update/cover-photo")
    public ResponseEntity<?> updateCoverPhoto(@RequestParam("coverPhoto") MultipartFile coverPhoto) {
        User updatedUser = userService.updateCoverPhoto(coverPhoto);
        return new ResponseEntity<>(updatedUser, HttpStatus.OK);
    }

    @PostMapping("/account/delete")
    public ResponseEntity<?> deleteUserAccount() {
        userService.deleteUserAccount();
        return new ResponseEntity<>(HttpStatus.OK);
    }


    @GetMapping("/users/{userId}")
    public ResponseEntity<?> getUserById(@PathVariable("userId") Long userId) {
        User authUser = userService.getAuthenticatedUser();
        User targetUser = userService.getUserById(userId);
        UserResponse userResponse = UserResponse.builder()
                .user(targetUser)
                .build();
        return new ResponseEntity<>(userResponse, HttpStatus.OK);
    }


    @PostMapping("/verify-email/{token}")
    public ResponseEntity<?> verifyEmail(@PathVariable("token") String token) {
        userService.verifyEmail(token);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestParam("email") String email) {
        userService.forgotPassword(email);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/reset-password/{token}")
    public ResponseEntity<?> resetPassword(@RequestBody @Valid ResetPasswordDto resetPasswordDto,
                                           @PathVariable("token") String token) {
        userService.resetPassword(token, resetPasswordDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/auth/google")
    public ResponseEntity<String> googleLogin(@RequestParam("token") String token) throws GeneralSecurityException, IOException {
        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), new JacksonFactory())
                .setAudience(Collections.singletonList(googleClientId))
                .build();

        GoogleIdToken idToken = verifier.verify(token);
        if (idToken != null) {
            GoogleIdToken.Payload payload = idToken.getPayload();
            String email = payload.getEmail();
            User user = userService.getUserByEmail(email);
            if (user == null) {
                // Create a new user account
                SignupDto signupDto = new SignupDto();
                signupDto.setEmail(email);
                signupDto.setPassword("googleUser"); // Temporary password for Google users
                user = userService.createNewUser(signupDto);
            }
            UserPrincipal userPrincipal = new UserPrincipal(user);
            HttpHeaders headers = new HttpHeaders();
            headers.set(AppConstants.TOKEN_HEADER, jwtTokenService.generateToken(userPrincipal));
            return new ResponseEntity<>(headers, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Invalid Google token", HttpStatus.BAD_REQUEST);
        }
    }

}
