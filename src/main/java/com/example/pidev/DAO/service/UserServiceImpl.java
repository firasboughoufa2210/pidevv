package com.example.pidev.DAO.service;

import com.example.pidev.DAO.dto.*;
import com.example.pidev.DAO.common.AppConstants;
import com.example.pidev.DAO.common.UserPrincipal;
import com.example.pidev.dto.*;
import com.example.pidev.DAO.entity.User;
import com.example.pidev.DAO.enumeration.Role;
import com.example.pidev.DAO.exception.EmailExistsException;
import com.example.pidev.DAO.exception.InvalidOperationException;
import com.example.pidev.DAO.exception.SameEmailUpdateException;
import com.example.pidev.DAO.exception.UserNotFoundException;
import com.example.pidev.DAO.mapper.MapStructMapper;
import com.example.pidev.DAO.mapper.MapstructMapperUpdate;
import com.example.pidev.DAO.repository.UserRepository;
import com.pieya.dto.*;
import com.example.pidev.DAO.util.FileNamingUtil;
import com.example.pidev.DAO.util.FileUploadUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Date;

@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final EmailService emailService;
    private final JwtTokenService jwtTokenService;
    private final PasswordEncoder passwordEncoder;
    private final MapStructMapper mapStructMapper;
    private final MapstructMapperUpdate mapstructMapperUpdate;
    private final Environment environment;
    private final FileNamingUtil fileNamingUtil;
    private final FileUploadUtil fileUploadUtil;

    @Override
    public User getUserById(Long userId) {
        return userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
    }

    @Override
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(UserNotFoundException::new);
    }

    @Override
    public User createNewUser(SignupDto signupDto) {
        try {
            User user = getUserByEmail(signupDto.getEmail());
            if (user != null) {
                throw new EmailExistsException();
            }
        } catch (UserNotFoundException e) {
            User newUser = new User();
            newUser.setEmail(signupDto.getEmail());
            newUser.setPassword(passwordEncoder.encode(signupDto.getPassword()));
            newUser.setFirstName(signupDto.getFirstName());
            newUser.setLastName(signupDto.getLastName());
            newUser.setFollowerCount(0);
            newUser.setFollowingCount(0);
            newUser.setEnabled(true);
            newUser.setAccountVerified(false);
            newUser.setEmailVerified(false);
            newUser.setJoinDate(new Date());
            newUser.setDateLastModified(new Date());
            newUser.setRole(Role.ROLE_VISITOR.name());
            User savedUser = userRepository.save(newUser);
            UserPrincipal userPrincipal = new UserPrincipal(savedUser);
            String emailVerifyMail =
                    emailService.buildEmailVerifyMail(jwtTokenService.generateToken(userPrincipal));
            emailService.send(savedUser.getEmail(), AppConstants.VERIFY_EMAIL, emailVerifyMail);
            return savedUser;
        }
        return null;
    }

    @Override
    public User updateUserInfo(UpdateUserInfoDto updateUserInfoDto) {
        User authUser = getAuthenticatedUser();
        mapstructMapperUpdate.updateUserFromUserUpdateDto(updateUserInfoDto, authUser);
        return userRepository.save(authUser);
    }

    @Override
    public User updateEmail(UpdateEmailDto updateEmailDto) {
        User authUser = getAuthenticatedUser();
        String newEmail = updateEmailDto.getEmail();
        String password = updateEmailDto.getPassword();

        if (!newEmail.equalsIgnoreCase(authUser.getEmail())) {
            try {
                User duplicateUser = getUserByEmail(newEmail);
                if (duplicateUser != null) {
                    throw new EmailExistsException();
                }
            } catch (UserNotFoundException e) {
                if (passwordEncoder.matches(password, authUser.getPassword())) {
                    authUser.setEmail(newEmail);
                    authUser.setEmailVerified(false);
                    authUser.setDateLastModified(new Date());
                    User updatedUser = userRepository.save(authUser);
                    UserPrincipal userPrincipal = new UserPrincipal(updatedUser);
                    String emailVerifyMail =
                            emailService.buildEmailVerifyMail(jwtTokenService.generateToken(userPrincipal));
                    emailService.send(updatedUser.getEmail(), AppConstants.VERIFY_EMAIL, emailVerifyMail);
                    return updatedUser;
                } else {
                    throw new InvalidOperationException();
                }
            }
        } else {
            throw new SameEmailUpdateException();
        }
        return null;
    }

    @Override
    public User updatePassword(UpdatePasswordDto updatePasswordDto) {
        User authUser = getAuthenticatedUser();
        if (passwordEncoder.matches(updatePasswordDto.getOldPassword(), authUser.getPassword())) {
            authUser.setPassword(passwordEncoder.encode(updatePasswordDto.getPassword()));
            authUser.setDateLastModified(new Date());
            return userRepository.save(authUser);
        } else {
            throw new InvalidOperationException();
        }
    }

    @Override
    public User verifyEmail(String token) {
        String targetEmail = jwtTokenService.getSubjectFromToken(token);
        User targetUser = getUserByEmail(targetEmail);
        targetUser.setEmailVerified(true);
        targetUser.setAccountVerified(true);
        targetUser.setDateLastModified(new Date());
        return userRepository.save(targetUser);
    }

    @Override
    public User updateProfilePhoto(MultipartFile profilePhoto) {
        User targetUser = getAuthenticatedUser();
        if (!profilePhoto.isEmpty() && profilePhoto.getSize() > 0) {
            String uploadDir = environment.getProperty("upload.user.images");
            String oldPhotoName = targetUser.getProfilePhoto();
            String newPhotoName = fileNamingUtil.nameFile(profilePhoto);
            String newPhotoUrl = environment.getProperty("app.root.backend") + File.separator
                    + environment.getProperty("upload.user.images") + File.separator + newPhotoName;
            targetUser.setProfilePhoto(newPhotoUrl);
            try {
                if (oldPhotoName == null) {
                    fileUploadUtil.saveNewFile(uploadDir, newPhotoName, profilePhoto);
                } else {
                    fileUploadUtil.updateFile(uploadDir, oldPhotoName, newPhotoName, profilePhoto);
                }
            } catch (IOException e) {
                throw new RuntimeException();
            }
        }
        return userRepository.save(targetUser);
    }

    @Override
    public User updateCoverPhoto(MultipartFile coverPhoto) {
        User targetUser = getAuthenticatedUser();
        if (!coverPhoto.isEmpty() && coverPhoto.getSize() > 0) {
            String uploadDir = environment.getProperty("upload.user.images");
            String oldPhotoName = targetUser.getCoverPhoto();
            String newPhotoName = fileNamingUtil.nameFile(coverPhoto);
            String newPhotoUrl = environment.getProperty("app.root.backend") + File.separator
                    + environment.getProperty("upload.user.images") + File.separator + newPhotoName;
            targetUser.setCoverPhoto(newPhotoUrl);
            try {
                if (oldPhotoName == null) {
                    fileUploadUtil.saveNewFile(uploadDir, newPhotoName, coverPhoto);
                } else {
                    fileUploadUtil.updateFile(uploadDir, oldPhotoName, newPhotoName, coverPhoto);
                }
            } catch (IOException e) {
                throw new RuntimeException();
            }
        }
        return userRepository.save(targetUser);
    }

    @Override
    public void forgotPassword(String email) {
        try {
            User targetUser = getUserByEmail(email);
            UserPrincipal userPrincipal = new UserPrincipal(targetUser);
            String emailVerifyMail =
                    emailService.buildResetPasswordMail(jwtTokenService.generateToken(userPrincipal));
            emailService.send(targetUser.getEmail(), AppConstants.RESET_PASSWORD, emailVerifyMail);
        } catch (UserNotFoundException ignored) {}
    }

    @Override
    public User resetPassword(String token, ResetPasswordDto resetPasswordDto) {
        String targetUserEmail = jwtTokenService.getSubjectFromToken(token);
        User targetUser = getUserByEmail(targetUserEmail);
        targetUser.setPassword(passwordEncoder.encode(resetPasswordDto.getPassword()));
        return userRepository.save(targetUser);
    }

    @Override
    public void deleteUserAccount() {
        User authUser = getAuthenticatedUser();
        String profilePhoto = getPhotoNameFromPhotoUrl(authUser.getProfilePhoto());
        // delete user profile picture from filesystem if exists
        if (profilePhoto != null && profilePhoto.length() > 0) {
            String uploadDir = environment.getProperty("upload.user.images");
            try {
                fileUploadUtil.deleteFile(uploadDir, profilePhoto);
            } catch (IOException e) {
                throw new RuntimeException();
            }
        }
        userRepository.deleteByEmail(authUser.getEmail());
    }


    public final User getAuthenticatedUser() {
        String authUserEmail = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        return getUserByEmail(authUserEmail);
    }

    private String getPhotoNameFromPhotoUrl(String photoUrl) {
        if (photoUrl != null) {
            String stringToOmit = environment.getProperty("app.root.backend") + File.separator
                    + environment.getProperty("upload.user.images") + File.separator;
            return photoUrl.substring(stringToOmit.length());
        } else {
            return null;
        }
    }


}
