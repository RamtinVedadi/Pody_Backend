package com.pody.contoller.impl;

import com.pody.contoller.HashtagController;
import com.pody.model.Hashtag;
import com.pody.service.managers.HashtagManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@CrossOrigin(origins = {"*", "http://pody.ir", "http://www.pody.ir"}, maxAge = 3600)
public class HahstagControllerImpl implements HashtagController {

    @Autowired
    private HashtagManager hashtagManager;

    @Override
    public ResponseEntity create(@RequestBody Hashtag hashtag) {
        return hashtagManager.create(hashtag);
    }

    @Override
    public ResponseEntity delete(@PathVariable UUID id) {
        return hashtagManager.delete(id);
    }
}
