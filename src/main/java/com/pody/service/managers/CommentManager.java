package com.pody.service.managers;

import com.pody.model.Comment;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

public interface CommentManager {
    ResponseEntity create(Comment comment);

    ResponseEntity update(Comment comment, UUID id);

    ResponseEntity delete(UUID commentId);

    ResponseEntity list(UUID podcastId);

    ResponseEntity like(UUID commentId);

    ResponseEntity approve(UUID commentId);
}
