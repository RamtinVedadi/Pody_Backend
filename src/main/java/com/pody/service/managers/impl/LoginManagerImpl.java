package com.pody.service.managers.impl;

import com.pody.dto.requests.LoginRequestDto;
import com.pody.dto.responses.LoginResultResponseDto;
import com.pody.dto.responses.UserSignUpWithPhoneDto;
import com.pody.model.User;
import com.pody.model.ValidationError;
import com.pody.repository.UserRepository;
import com.pody.service.ErrorJsonHandler;
import com.pody.service.PasswordEncoding;
import com.pody.service.managers.LoginManager;
import org.apache.commons.codec.binary.Hex;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class LoginManagerImpl implements LoginManager {

    private ModelMapper modelMapper;
    private UserRepository userRepository;

    @Autowired
    public LoginManagerImpl(ModelMapper modelMapper, UserRepository userRepository) {
        this.modelMapper = modelMapper;
        this.userRepository = userRepository;
    }

    @Override
    public ResponseEntity signupWithEmail(User u) {
        try {
            if (u != null) {
                u.setDeleteFlag(false);
                u.setId(null);
                List<ValidationError> errors = u.validate();
                if (errors.size() > 0) {
                    return new ResponseEntity(errors, HttpStatus.BAD_REQUEST);
                }

                u.setFirstName(u.getFirstName());
                u.setLastName(u.getLastName());
                u.setCity(null);
                u.setProvince(null);
                u.setBirthDate(null);
                u.setAddress(null);
                u.setRssUrl(u.getRssUrl());
                if (u.getProfileImageAddress() == null || u.getProfileImageAddress() == "") {
                    u.setProfileImageAddress("http://pody.ir/defaultImages/profile.png");
                } else {
                    u.setProfileImageAddress(u.getProfileImageAddress());
                }
                u.setLanguage(u.getLanguage());
                u.setBio(u.getBio());
                User usernameCheck = userRepository.findOneByUsername(u.getUsername());
                User emailCheck = userRepository.findOneByEmail(u.getUsername());
                if (usernameCheck != null) {
                    return new ResponseEntity(ErrorJsonHandler.DUPLICATE_USERNAME, HttpStatus.FORBIDDEN);
                }
                if (emailCheck != null) {
                    return new ResponseEntity(ErrorJsonHandler.DUPLICATE_EMAIL, HttpStatus.FORBIDDEN);
                }

                PasswordEncoding encoding = new PasswordEncoding();
                String salt = "Pody123456789";

                int iterations = 10000;
                int keyLength = 512;
                char[] passwordChars = u.getPassword().toCharArray();
                byte[] saltBytes = salt.getBytes();

                byte[] hashedBytes = encoding.hashPassword(passwordChars, saltBytes, iterations, keyLength);
                String hashedPassword = Hex.encodeHexString(hashedBytes);

                u.setPassword(hashedPassword);

                User registeredUser = userRepository.save(u);
                LoginResultResponseDto result = modelMapper.map(registeredUser, LoginResultResponseDto.class);

                return ResponseEntity.ok(result);
            } else {
                return new ResponseEntity(ErrorJsonHandler.EMPTY_BODY, HttpStatus.FORBIDDEN);
            }
        } catch (NullPointerException ex) {
            return new ResponseEntity(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity signupWithPhone(String phoneNumber) {
        try {
            if (phoneNumber != null) {
                User u = new User();
                UserSignUpWithPhoneDto dto = new UserSignUpWithPhoneDto();
                u.setUsername(phoneNumber);

                User result = userRepository.save(u);
                dto.setId(result.getId());
                dto.setCode("");

                return new ResponseEntity(dto, HttpStatus.OK);
            } else {
                return new ResponseEntity(ErrorJsonHandler.EMPTY_BODY, HttpStatus.FORBIDDEN);
            }
        } catch (NullPointerException ex) {
            return new ResponseEntity(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity login(LoginRequestDto requestDto) {
        try {
            if (requestDto != null) {
                List<ValidationError> errors = requestDto.validate();
                if (errors.size() > 0) {
                    return new ResponseEntity(errors, HttpStatus.BAD_REQUEST);
                }
                PasswordEncoding encoding = new PasswordEncoding();
                String salt = "Pody123456789";

                int iterations = 10000;
                int keyLength = 512;
                char[] passwordChars = requestDto.getPassword().toCharArray();
                byte[] saltBytes = salt.getBytes();

                byte[] hashedBytes = encoding.hashPassword(passwordChars, saltBytes, iterations, keyLength);
                String hashedPassword = Hex.encodeHexString(hashedBytes);

                User u = userRepository.loginUser(requestDto.getUsername());
                LoginResultResponseDto result = null;
                if (hashedPassword.equals(u.getPassword())) {
                    result = modelMapper.map(u, LoginResultResponseDto.class);
                }
                if (result != null) {
                    return new ResponseEntity(result, HttpStatus.OK);
                } else {
                    return new ResponseEntity(ErrorJsonHandler.NULL_POINTER_EXCEPTION, HttpStatus.INTERNAL_SERVER_ERROR);
                }
            } else {
                return new ResponseEntity(ErrorJsonHandler.EMPTY_BODY, HttpStatus.FORBIDDEN);
            }
        } catch (NullPointerException ex) {
            return new ResponseEntity(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}