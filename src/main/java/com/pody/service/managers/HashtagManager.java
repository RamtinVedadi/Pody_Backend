package com.pody.service.managers;

import com.pody.model.Hashtag;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

public interface HashtagManager {
    ResponseEntity create(Hashtag hashtag);

    ResponseEntity delete(UUID id);
}
