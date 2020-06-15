package com.pody.contoller;

import com.pody.dto.requests.LoginRequestDto;
import com.pody.dto.requests.StringRequestDto;
import com.pody.model.User;
import com.pody.service.UrlStringMapping;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

public interface LoginController {
    UrlStringMapping url = new UrlStringMapping();

    @PostMapping(value = UrlStringMapping.URL0002, consumes = APPLICATION_JSON_UTF8_VALUE, produces = APPLICATION_JSON_UTF8_VALUE)
    ResponseEntity signupWithEmail(User u);

    @PostMapping(value = UrlStringMapping.URL0003, consumes = APPLICATION_JSON_UTF8_VALUE, produces = APPLICATION_JSON_UTF8_VALUE)
    ResponseEntity signupWithPhone(StringRequestDto dto);

    @PostMapping(value = UrlStringMapping.URL0001, consumes = APPLICATION_JSON_UTF8_VALUE, produces = APPLICATION_JSON_UTF8_VALUE)
    ResponseEntity login(LoginRequestDto requestDto);
}
