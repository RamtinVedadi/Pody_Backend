package com.pody.contoller.impl;

import com.pody.contoller.LoginController;
import com.pody.dto.requests.LoginRequestDto;
import com.pody.dto.requests.StringRequestDto;
import com.pody.model.User;
import com.pody.service.managers.LoginManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = {"*", "http://pody.ir", "http://www.pody.ir"}, maxAge = 3600)
public class LoginControllerImpl implements LoginController {

    @Autowired
    private LoginManager loginManager;

    @Override
    public ResponseEntity signupWithEmail(@RequestBody User u) {
        return loginManager.signupWithEmail(u);
    }

    @Override
    public ResponseEntity signupWithPhone(@RequestBody StringRequestDto dto) {
        return loginManager.signupWithPhone(dto.getStringValue());
    }

    @Override
    public ResponseEntity login(@RequestBody LoginRequestDto requestDto) {
        return loginManager.login(requestDto);
    }
}
