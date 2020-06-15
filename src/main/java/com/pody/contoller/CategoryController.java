package com.pody.contoller;

import com.pody.dto.requests.TwoIDRequestDto;
import com.pody.dto.responses.IdResponseDto;
import com.pody.model.Category;
import com.pody.service.UrlStringMapping;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

import java.util.UUID;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

public interface CategoryController {
    @PostMapping(value = UrlStringMapping.URL0110, consumes = APPLICATION_JSON_UTF8_VALUE, produces = APPLICATION_JSON_UTF8_VALUE)
    ResponseEntity create(Category category);

    @PutMapping(value = UrlStringMapping.URL0111, consumes = APPLICATION_JSON_UTF8_VALUE, produces = APPLICATION_JSON_UTF8_VALUE)
    ResponseEntity update(Category category, UUID id);

    @DeleteMapping(value = UrlStringMapping.URL0112, produces = APPLICATION_JSON_UTF8_VALUE)
    ResponseEntity delete(UUID id);

    @GetMapping(value = UrlStringMapping.URL0113, produces = APPLICATION_JSON_UTF8_VALUE)
    ResponseEntity tree();

    @GetMapping(value = UrlStringMapping.URL0114, produces = APPLICATION_JSON_UTF8_VALUE)
    ResponseEntity listParents();

    @GetMapping(value = UrlStringMapping.URL0115, produces = APPLICATION_JSON_UTF8_VALUE)
    ResponseEntity listChildren(UUID id);

    @PostMapping(value = UrlStringMapping.URL0116, consumes = APPLICATION_JSON_UTF8_VALUE, produces = APPLICATION_JSON_UTF8_VALUE)
    ResponseEntity read(TwoIDRequestDto dto);

    @PostMapping(value = UrlStringMapping.URL0117, consumes = APPLICATION_JSON_UTF8_VALUE, produces = APPLICATION_JSON_UTF8_VALUE)
    ResponseEntity listAllCategoryPage(IdResponseDto dto);

    @PostMapping(value = UrlStringMapping.URL0118, consumes = APPLICATION_JSON_UTF8_VALUE, produces = APPLICATION_JSON_UTF8_VALUE)
    ResponseEntity readInfinite(int till, int to, IdResponseDto dto);

}
