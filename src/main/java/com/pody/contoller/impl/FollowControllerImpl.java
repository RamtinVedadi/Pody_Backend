package com.pody.contoller.impl;

import com.pody.contoller.FollowController;
import com.pody.dto.requests.TwoIDRequestDto;
import com.pody.model.CategoryFollow;
import com.pody.model.HashtagFollow;
import com.pody.model.UserFollow;
import com.pody.service.managers.FollowManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@CrossOrigin(origins = {"*", "http://pody.ir", "http://www.pody.ir"}, maxAge = 3600)
public class FollowControllerImpl implements FollowController {
    @Autowired
    private FollowManager followManager;

    @Override
    public ResponseEntity createUserFollow(@RequestBody UserFollow userFollow) {
        return followManager.createUserFollow(userFollow);
    }

    @Override
    public ResponseEntity deleteUserFollow(@RequestBody TwoIDRequestDto dto) {
        return followManager.deleteUserFollow(dto);
    }

    @Override
    public ResponseEntity createCategoryFollow(@RequestBody CategoryFollow categoryFollow) {
        return followManager.createCategoryFollow(categoryFollow);
    }

    @Override
    public ResponseEntity deleteCategoryFollow(@RequestBody TwoIDRequestDto dto) {
        return followManager.deleteCategoryFollow(dto);
    }

    @Override
    public ResponseEntity createHashtagFollow(@RequestBody HashtagFollow hashtagFollow) {
        return followManager.createHashtagFollow(hashtagFollow);
    }

    @Override
    public ResponseEntity deleteHashtagFollow(@RequestBody TwoIDRequestDto dto) {
        return followManager.deleteHashtagFollow(dto);
    }

    @Override
    public ResponseEntity listFollowings(@PathVariable UUID userId) {
        return followManager.listFollowings(userId);
    }

    @Override
    public ResponseEntity listFollowers(@PathVariable UUID userId) {
        return followManager.listFollowers(userId);
    }
}
