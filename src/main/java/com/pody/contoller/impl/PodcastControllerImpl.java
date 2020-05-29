package com.pody.contoller.impl;

import com.pody.contoller.PodcastController;
import com.pody.dto.requests.PodcastCreateDto;
import com.pody.dto.requests.RssDataDto;
import com.pody.dto.requests.StringRequestDto;
import com.pody.dto.requests.TwoIDRequestDto;
import com.pody.dto.responses.IdResponseDto;
import com.pody.service.managers.PodcastManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
public class PodcastControllerImpl implements PodcastController {

    @Autowired
    private PodcastManager podcastManager;

    @Override
    public ResponseEntity create(@RequestBody PodcastCreateDto dto) {
        return podcastManager.create(dto);
    }

    @Override
    public ResponseEntity read(@PathVariable UUID id) {
        return podcastManager.read(id);
    }

    @Override
    public ResponseEntity update(@RequestBody PodcastCreateDto dto, @PathVariable UUID id) {
        return podcastManager.update(dto, id);
    }

    @Override
    public ResponseEntity delete(@PathVariable UUID id) {
        return podcastManager.delete(id);
    }

    @Override
    public ResponseEntity uploadImage(@RequestParam("image") MultipartFile image, @RequestParam("audio") MultipartFile audio, @PathVariable("id") UUID podcastId) {
        return podcastManager.uploadPodcast(image, audio, podcastId);
    }

    @Override
    public ResponseEntity updateViewCount(@PathVariable("id") UUID podcastId) {
        return podcastManager.updateViewCount(podcastId);
    }

    @Override
    public ResponseEntity updateLikeCount(@PathVariable("id") UUID podcastId) {
        return podcastManager.updateLikeCount(podcastId);
    }

    @Override
    public ResponseEntity listPodcastsEachUser(@PathVariable("id") UUID userId) {
        return podcastManager.listPodcastsEachUser(userId);
    }

    @Override
    public ResponseEntity listPodcastsMostLiked() {
        return podcastManager.listPodcastsMostLiked();
    }

    @Override
    public ResponseEntity listPodcastsMostViewed() {
        return podcastManager.listPodcastsMostViewed();
    }

    @Override
    public ResponseEntity listPodcastsSuggested() {
        return podcastManager.listPodcastsSuggested();
    }

    @Override
    public ResponseEntity listPodcastsNewAdded(@PathVariable int till, @PathVariable int to) {
        return podcastManager.listPodcastsNewAdded(till, to);
    }

    @Override
    public ResponseEntity addToListenLater(@RequestBody TwoIDRequestDto dto) {
        return podcastManager.addToListenLater(dto);
    }

    @Override
    public ResponseEntity addToHistory(@PathVariable UUID id) {
        return null;
    }

    @Override
    public ResponseEntity userPodcastHistoryList(@PathVariable("id") UUID userId) {
        return null;
    }

    @Override
    public ResponseEntity getUserRssData(@RequestBody RssDataDto rssDataDto) {
        return podcastManager.getRssData(rssDataDto);
    }

    @Override
    public ResponseEntity listTrendingPodcasts() {
        return podcastManager.listTrendingPodcasts();
    }

    @Override
    public ResponseEntity homePagePodcastList(@RequestBody IdResponseDto dto) {
        return podcastManager.homePagePodcastList(dto);
    }

    @Override
    public ResponseEntity getAllDataWithOnlyRssUrl(@RequestBody StringRequestDto dto) {
        return podcastManager.getAllDataWithOnlyRssUrl(dto);
    }

    @Override
    public ResponseEntity listFollowingPodcasts(@RequestBody IdResponseDto dto) {
        return podcastManager.listFollowingPodcasts(dto);
    }
}
