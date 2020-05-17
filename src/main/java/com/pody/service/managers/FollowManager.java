package com.pody.service.managers;

import com.pody.dto.requests.TwoIDRequestDto;
import com.pody.model.CategoryFollow;
import com.pody.model.HashtagFollow;
import com.pody.model.UserFollow;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

public interface FollowManager {
    ResponseEntity createUserFollow(UserFollow userFollow);

    ResponseEntity deleteUserFollow(TwoIDRequestDto dto);

    ResponseEntity createCategoryFollow(CategoryFollow categoryFollow);

    ResponseEntity deleteCategoryFollow(TwoIDRequestDto dto);

    ResponseEntity createHashtagFollow(HashtagFollow hashtagFollow);

    ResponseEntity deleteHashtagFollow(TwoIDRequestDto dto);

    ResponseEntity listFollowings(UUID userId);

    ResponseEntity listFollowers(UUID userId);
}
