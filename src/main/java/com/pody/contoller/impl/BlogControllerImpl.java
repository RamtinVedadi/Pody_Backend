package com.pody.contoller.impl;

import com.pody.contoller.BlogController;
import com.pody.dto.requests.BlogCreateDto;
import com.pody.dto.requests.TwoIDRequestDto;
import com.pody.dto.responses.IdResponseDto;
import com.pody.model.Blog;
import com.pody.service.managers.BlogManager;
import com.pody.service.managers.CategoryManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@RestController
@CrossOrigin(origins = {"*", "http://pody.ir", "http://www.pody.ir"}, maxAge = 3600)
public class BlogControllerImpl implements BlogController {
    @Autowired
    private BlogManager blogManager;

    @Override
    public ResponseEntity create(@RequestBody BlogCreateDto dto) {
        return blogManager.create(dto);
    }

    @Override
    public ResponseEntity read(@RequestBody TwoIDRequestDto dto) {
        return blogManager.read(dto);
    }

    @Override
    public ResponseEntity update(@RequestBody Blog blog, @PathVariable("id") UUID id) {
        return blogManager.update(blog, id);
    }

    @Override
    public ResponseEntity uploadImage(@RequestParam("image") MultipartFile image, @PathVariable("id") UUID blogId) {
        return blogManager.uploadImage(image, blogId);
    }

    @Override
    public ResponseEntity updateIsPublish(@PathVariable("id") UUID blogId) {
        return blogManager.updateIsPublish(blogId);
    }

    @Override
    public ResponseEntity blogList(@RequestBody IdResponseDto dto, @PathVariable int page, @PathVariable int size) {
        return blogManager.blogList(dto, page, size);
    }

    @Override
    public ResponseEntity blogLikeUpdate(@RequestBody TwoIDRequestDto dto) {
        return blogManager.likeUpdate(dto);
    }

    @Override
    public ResponseEntity blogDisLikeUpdate(@RequestBody TwoIDRequestDto dto) {
        return blogManager.disLikeUpdate(dto);
    }

    @Override
    public ResponseEntity blogViewUpdate(@RequestBody TwoIDRequestDto dto) {
        return blogManager.viewUpdate(dto);
    }

    @Override
    public ResponseEntity listLikedBlogEachUser(@PathVariable("id") UUID userId) {
        return blogManager.listLikedBlogEachUser(userId);
    }

    @Override
    public ResponseEntity makeBlogBookmark(@RequestBody TwoIDRequestDto dto) {
        return blogManager.makeBlogBookmark(dto);
    }

    @Override
    public ResponseEntity makeBlogUnBookmark(@RequestBody TwoIDRequestDto dto) {
        return blogManager.makeBlogUnBookmark(dto);
    }

    @Override
    public ResponseEntity blogBookmarkCheck(@RequestBody TwoIDRequestDto dto) {
        return blogManager.blogBookmarkCheck(dto);
    }
}
