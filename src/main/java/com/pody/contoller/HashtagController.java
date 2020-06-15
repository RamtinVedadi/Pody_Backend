package com.pody.contoller;

import com.pody.model.Hashtag;
import com.pody.service.UrlStringMapping;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.UUID;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

public interface HashtagController {

    @PostMapping(value = UrlStringMapping.URL0180, consumes = APPLICATION_JSON_UTF8_VALUE, produces = APPLICATION_JSON_UTF8_VALUE)
    ResponseEntity create(Hashtag hashtag);

    @PostMapping(value = UrlStringMapping.URL0181, produces = APPLICATION_JSON_UTF8_VALUE)
    ResponseEntity delete(UUID id);
}
