package com.pody.service.managers;

import com.pody.dto.requests.BlogCreateDto;
import com.pody.dto.requests.TwoIDRequestDto;
import com.pody.dto.responses.IdResponseDto;
import com.pody.model.Blog;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

public interface BlogManager {
    ResponseEntity create(BlogCreateDto dto);

    ResponseEntity read(TwoIDRequestDto dto);

    ResponseEntity update(Blog blog, UUID id);

    ResponseEntity uploadImage(MultipartFile image, UUID blogId);

    ResponseEntity updateIsPublish(UUID blogId);

    ResponseEntity blogList(IdResponseDto dto, int page, int size);

    ResponseEntity likeUpdate(TwoIDRequestDto dto);

    ResponseEntity disLikeUpdate(TwoIDRequestDto dto);

    ResponseEntity viewUpdate(TwoIDRequestDto dto);

    ResponseEntity listLikedBlogEachUser(UUID userId);

    ResponseEntity makeBlogBookmark(TwoIDRequestDto dto);

    ResponseEntity makeBlogUnBookmark(TwoIDRequestDto dto);

    ResponseEntity blogBookmarkCheck(TwoIDRequestDto dto);
}
