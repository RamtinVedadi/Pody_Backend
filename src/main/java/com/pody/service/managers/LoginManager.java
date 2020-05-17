package com.pody.service.managers;

import com.pody.dto.requests.LoginRequestDto;
import com.pody.model.User;
import org.springframework.http.ResponseEntity;

public interface LoginManager {
    ResponseEntity signupWithEmail(User u);

    ResponseEntity signupWithPhone(String phoneNumber);

    ResponseEntity login(LoginRequestDto requestDto);
}
