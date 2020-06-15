package com.pody.contoller;

import com.pody.dto.requests.StringRequestDto;
import com.pody.service.UrlStringMapping;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

public interface SearchController {

    @PostMapping(value = UrlStringMapping.URL0100, consumes = APPLICATION_JSON_UTF8_VALUE, produces= APPLICATION_JSON_UTF8_VALUE)
    ResponseEntity search(StringRequestDto searchString);
}
