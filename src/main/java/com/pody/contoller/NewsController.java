package com.pody.contoller;

import com.pody.model.News;
import com.pody.service.UrlStringMapping;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

import java.util.UUID;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

public interface NewsController {
    @PostMapping(value = UrlStringMapping.URL060, consumes = APPLICATION_JSON_UTF8_VALUE, produces = APPLICATION_JSON_UTF8_VALUE)
    ResponseEntity create(News news);

    @PutMapping(value = UrlStringMapping.URL061, consumes = APPLICATION_JSON_UTF8_VALUE, produces = APPLICATION_JSON_UTF8_VALUE)
    ResponseEntity update(News news, UUID id);

    @DeleteMapping(value = UrlStringMapping.URL062, produces = APPLICATION_JSON_UTF8_VALUE)
    ResponseEntity delete(UUID id);

    @GetMapping(value = UrlStringMapping.URL063, produces = APPLICATION_JSON_UTF8_VALUE)
    ResponseEntity listForHomePage();

    @GetMapping(value = UrlStringMapping.URL064, produces = APPLICATION_JSON_UTF8_VALUE)
    ResponseEntity fullList();
}
