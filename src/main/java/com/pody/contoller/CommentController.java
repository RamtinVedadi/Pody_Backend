package com.pody.contoller;

import com.pody.model.Comment;
import com.pody.service.UrlStringMapping;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

import java.util.UUID;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

public interface CommentController {
    @PostMapping(value = UrlStringMapping.URL0140, consumes = APPLICATION_JSON_UTF8_VALUE, produces = APPLICATION_JSON_UTF8_VALUE)
    ResponseEntity create(Comment comment);

    @PutMapping(value = UrlStringMapping.URL0141, consumes = APPLICATION_JSON_UTF8_VALUE, produces = APPLICATION_JSON_UTF8_VALUE)
    ResponseEntity update(Comment comment, UUID id);

    @DeleteMapping(value = UrlStringMapping.URL0142, consumes = APPLICATION_JSON_UTF8_VALUE, produces = APPLICATION_JSON_UTF8_VALUE)
    ResponseEntity delete(UUID commentId);

    @PostMapping(value = UrlStringMapping.URL0143, consumes = APPLICATION_JSON_UTF8_VALUE, produces = APPLICATION_JSON_UTF8_VALUE)
    ResponseEntity list(UUID podcastId);

    @GetMapping(value = UrlStringMapping.URL0144, consumes = APPLICATION_JSON_UTF8_VALUE, produces = APPLICATION_JSON_UTF8_VALUE)
    ResponseEntity likeComment(UUID commentId);

    @GetMapping(value = UrlStringMapping.URL0145, consumes = APPLICATION_JSON_UTF8_VALUE, produces = APPLICATION_JSON_UTF8_VALUE)
    ResponseEntity approveComment(UUID commentId);
}
