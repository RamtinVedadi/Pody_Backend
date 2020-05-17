package com.pody.contoller.impl;

import com.pody.contoller.CommentController;
import com.pody.model.Comment;
import com.pody.service.managers.CommentManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
public class CommentControllerImpl implements CommentController {
    @Autowired
    private CommentManager commentManager;

    @Override
    public ResponseEntity create(@RequestBody Comment comment) {
        return commentManager.create(comment);
    }

    @Override
    public ResponseEntity update(@RequestBody Comment comment, @PathVariable UUID id) {
        return commentManager.update(comment, id);
    }

    @Override
    public ResponseEntity delete(@PathVariable UUID commentId) {
        return commentManager.delete(commentId);
    }

    @Override
    public ResponseEntity list(@PathVariable UUID podcastId) {
        return commentManager.list(podcastId);
    }

    @Override
    public ResponseEntity likeComment(@PathVariable UUID commentId) {
        return commentManager.like(commentId);
    }

    @Override
    public ResponseEntity approveComment(@PathVariable UUID commentId) {
        return commentManager.approve(commentId);
    }
}
