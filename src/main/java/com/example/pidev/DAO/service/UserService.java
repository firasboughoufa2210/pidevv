package com.example.pidev.DAO.service;

import com.example.pidev.DAO.dto.*;
import com.example.pidev.dto.*;
import com.pieya.dto.*;
import com.example.pidev.DAO.entity.User;
import org.springframework.web.multipart.MultipartFile;

public interface UserService {
    User getUserById(Long userId);
    User getUserByEmail(String email);
    User createNewUser(SignupDto signupDto);
    User updateUserInfo(UpdateUserInfoDto updateUserInfoDto);
    User updateEmail(UpdateEmailDto updateEmailDto);
    User updatePassword(UpdatePasswordDto updatePasswordDto);
    User updateProfilePhoto(MultipartFile photo);
    User updateCoverPhoto(MultipartFile photo);
    User verifyEmail(String token);
    void forgotPassword(String email);
    User resetPassword(String token, ResetPasswordDto resetPasswordDto);
    void deleteUserAccount();
    User getAuthenticatedUser();

}
