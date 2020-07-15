package com.pody.contoller;

import com.pody.dto.requests.BlogCreateDto;
import com.pody.dto.requests.TwoIDRequestDto;
import com.pody.dto.responses.IdResponseDto;
import com.pody.model.Blog;
import com.pody.service.UrlStringMapping;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;
import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;

public interface BlogController {
    @PostMapping(value = UrlStringMapping.URL0250, consumes = APPLICATION_JSON_UTF8_VALUE, produces = APPLICATION_JSON_UTF8_VALUE)
    ResponseEntity create(BlogCreateDto dto);

    @PostMapping(value = UrlStringMapping.URL0258, consumes = APPLICATION_JSON_UTF8_VALUE, produces = APPLICATION_JSON_UTF8_VALUE)
    ResponseEntity read(TwoIDRequestDto dto);

    @PutMapping(value = UrlStringMapping.URL0257, consumes = APPLICATION_JSON_UTF8_VALUE, produces = APPLICATION_JSON_UTF8_VALUE)
    ResponseEntity update(Blog blog, UUID id);

    @PostMapping(value = UrlStringMapping.URL0251)
    ResponseEntity uploadImage(MultipartFile image, UUID blogId);

    @GetMapping(value = UrlStringMapping.URL0252, produces = APPLICATION_JSON_UTF8_VALUE)
    ResponseEntity updateIsPublish(UUID blogId);

    @PostMapping(value = UrlStringMapping.URL0253, consumes = APPLICATION_JSON_UTF8_VALUE, produces = APPLICATION_JSON_UTF8_VALUE)
    ResponseEntity blogList(IdResponseDto dto, int page, int size);

    @PostMapping(value = UrlStringMapping.URL0254, consumes = APPLICATION_JSON_UTF8_VALUE, produces = APPLICATION_JSON_UTF8_VALUE)
    ResponseEntity blogLikeUpdate(TwoIDRequestDto dto);

    @PostMapping(value = UrlStringMapping.URL0255, consumes = APPLICATION_JSON_UTF8_VALUE, produces = APPLICATION_JSON_UTF8_VALUE)
    ResponseEntity blogDisLikeUpdate(TwoIDRequestDto dto);

    @PostMapping(value = UrlStringMapping.URL0256, consumes = APPLICATION_JSON_UTF8_VALUE, produces = APPLICATION_JSON_UTF8_VALUE)
    ResponseEntity blogViewUpdate(TwoIDRequestDto dto);

    @GetMapping(value = UrlStringMapping.URL0259, produces = APPLICATION_JSON_UTF8_VALUE)
    ResponseEntity listLikedBlogEachUser(UUID userId);

    @PostMapping(value = UrlStringMapping.URL0260, consumes = APPLICATION_JSON_UTF8_VALUE, produces = APPLICATION_JSON_UTF8_VALUE)
    ResponseEntity makeBlogBookmark(TwoIDRequestDto dto);

    @PostMapping(value = UrlStringMapping.URL0261, consumes = APPLICATION_JSON_UTF8_VALUE, produces = APPLICATION_JSON_UTF8_VALUE)
    ResponseEntity makeBlogUnBookmark(TwoIDRequestDto dto);

    @PostMapping(value = UrlStringMapping.URL0262, consumes = APPLICATION_JSON_UTF8_VALUE, produces = APPLICATION_JSON_UTF8_VALUE)
    ResponseEntity blogBookmarkCheck(TwoIDRequestDto dto);
}
