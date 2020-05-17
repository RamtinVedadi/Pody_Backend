package com.pody.contoller;

import com.pody.dto.requests.TwoIDRequestDto;
import com.pody.model.CategoryFollow;
import com.pody.model.HashtagFollow;
import com.pody.model.UserFollow;
import com.pody.service.UrlStringMapping;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.UUID;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

public interface FollowController {
    @PostMapping(value = UrlStringMapping.URL041, consumes = APPLICATION_JSON_UTF8_VALUE, produces = APPLICATION_JSON_UTF8_VALUE)
    ResponseEntity createUserFollow(UserFollow userFollow);

    @DeleteMapping(value = UrlStringMapping.URL042, produces = APPLICATION_JSON_UTF8_VALUE)
    ResponseEntity deleteUserFollow(TwoIDRequestDto dto);

    @PostMapping(value = UrlStringMapping.URL043, consumes = APPLICATION_JSON_UTF8_VALUE, produces = APPLICATION_JSON_UTF8_VALUE)
    ResponseEntity createCategoryFollow(CategoryFollow categoryFollow);

    @DeleteMapping(value = UrlStringMapping.URL044, produces = APPLICATION_JSON_UTF8_VALUE)
    ResponseEntity deleteCategoryFollow(TwoIDRequestDto dto);

    @PostMapping(value = UrlStringMapping.URL045, consumes = APPLICATION_JSON_UTF8_VALUE, produces = APPLICATION_JSON_UTF8_VALUE)
    ResponseEntity createHashtagFollow(HashtagFollow hashtagFollow);

    @DeleteMapping(value = UrlStringMapping.URL046, produces = APPLICATION_JSON_UTF8_VALUE)
    ResponseEntity deleteHashtagFollow(TwoIDRequestDto dto);

    @PostMapping(value = UrlStringMapping.URL047, produces = APPLICATION_JSON_UTF8_VALUE)
    ResponseEntity listFollowings(UUID userId);

    @PostMapping(value = UrlStringMapping.URL048, produces = APPLICATION_JSON_UTF8_VALUE)
    ResponseEntity listFollowers(UUID userId);
}

