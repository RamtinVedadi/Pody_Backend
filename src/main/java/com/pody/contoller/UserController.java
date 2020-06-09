package com.pody.contoller;

import com.pody.dto.requests.IdStringRequestDto;
import com.pody.dto.requests.StringRequestDto;
import com.pody.dto.requests.TwoIDRequestDto;
import com.pody.dto.responses.IdResponseDto;
import com.pody.model.User;
import com.pody.service.UrlStringMapping;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

public interface UserController {

    @PostMapping(value = UrlStringMapping.URL003, produces = APPLICATION_JSON_UTF8_VALUE)
    ResponseEntity read(TwoIDRequestDto dto);

    @PutMapping(value = UrlStringMapping.URL004, consumes = APPLICATION_JSON_UTF8_VALUE, produces = APPLICATION_JSON_UTF8_VALUE)
    ResponseEntity update(User user, UUID id);

    @DeleteMapping(value = UrlStringMapping.URL005, produces = APPLICATION_JSON_UTF8_VALUE)
    ResponseEntity delete(UUID id);

    @GetMapping(value = UrlStringMapping.URL006, produces = APPLICATION_JSON_UTF8_VALUE)
    ResponseEntity updateIsPremium(UUID id);

    @PostMapping(value = UrlStringMapping.URL007, produces = APPLICATION_JSON_UTF8_VALUE)
    ResponseEntity uploadProfileImage(MultipartFile image, UUID id);

    @PostMapping(value = UrlStringMapping.URL0077, produces = APPLICATION_JSON_UTF8_VALUE)
    ResponseEntity uploadChannelImage(MultipartFile image, UUID id);

    @PostMapping(value = UrlStringMapping.URL008, consumes = APPLICATION_JSON_UTF8_VALUE, produces = APPLICATION_JSON_UTF8_VALUE)
    ResponseEntity checkEmail(StringRequestDto dto);

    @PostMapping(value = UrlStringMapping.URL009, consumes = APPLICATION_JSON_UTF8_VALUE, produces = APPLICATION_JSON_UTF8_VALUE)
    ResponseEntity checkUsername(StringRequestDto dto);

    @PostMapping(value = UrlStringMapping.URL073, consumes = APPLICATION_JSON_UTF8_VALUE, produces = APPLICATION_JSON_UTF8_VALUE)
    ResponseEntity resetPassword(IdStringRequestDto dto);

    @PostMapping(value = UrlStringMapping.URL074, consumes = APPLICATION_JSON_UTF8_VALUE, produces = APPLICATION_JSON_UTF8_VALUE)
    ResponseEntity findUserForResetPassword(StringRequestDto dto);

    @PostMapping(value = UrlStringMapping.URL076, consumes = APPLICATION_JSON_UTF8_VALUE, produces = APPLICATION_JSON_UTF8_VALUE)
    ResponseEntity listChannels(IdResponseDto dto);

    @PutMapping(value = UrlStringMapping.URL078, consumes = APPLICATION_JSON_UTF8_VALUE, produces = APPLICATION_JSON_UTF8_VALUE)
    ResponseEntity updateIsChannel(IdResponseDto dto);//input is user id

    @GetMapping(value = UrlStringMapping.URL081, produces = APPLICATION_JSON_UTF8_VALUE)
    ResponseEntity userListSubscriptions(UUID id);//input is logined user id

    @GetMapping(value = UrlStringMapping.URL090, produces = APPLICATION_JSON_UTF8_VALUE)
    ResponseEntity userListSubscriptionsSideNav(UUID id);//input is logined user id

    @PostMapping(value = UrlStringMapping.URL086, consumes = APPLICATION_JSON_UTF8_VALUE, produces = APPLICATION_JSON_UTF8_VALUE)
    ResponseEntity checkUserHasFollow(TwoIDRequestDto dto);
}
