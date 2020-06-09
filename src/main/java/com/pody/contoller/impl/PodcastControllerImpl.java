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
@CrossOrigin(origins = {"*", "http://pody.ir", "http://www.pody.ir"}, maxAge = 3600)
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
    public ResponseEntity listPodcastsEachUser(@PathVariable("id") UUID userId, @PathVariable int till, @PathVariable int to) {
        return podcastManager.listPodcastsEachUser(userId , till , to);
    }

    @Override
    public ResponseEntity listPodcastsMostLiked(@PathVariable int till, @PathVariable int to) {
        return podcastManager.listPodcastsMostLiked(till, to);
    }

    @Override
    public ResponseEntity listPodcastsMostViewed(@PathVariable int till, @PathVariable int to) {
        return podcastManager.listPodcastsMostViewed(till, to);
    }

    @Override
    public ResponseEntity listPodcastsSuggested(@PathVariable int till, @PathVariable int to) {
        return podcastManager.listPodcastsSuggested(till, to);
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
    public ResponseEntity podcastListenLaterList(@PathVariable int till, @PathVariable int to, @RequestBody IdResponseDto dto) {
        return podcastManager.podcastListenLaterList(till, to, dto);
    }

    @Override
    public ResponseEntity podcastListenLaterCheck(@RequestBody TwoIDRequestDto dto) {
        return podcastManager.podcastListenLaterCheck(dto);
    }

    @Override
    public ResponseEntity podcastListenLaterDelete(@RequestBody TwoIDRequestDto dto) {
        return podcastManager.podcastListenLaterDelete(dto);
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
    public ResponseEntity listFollowingPodcasts(@PathVariable int till, @PathVariable int to, @RequestBody IdResponseDto dto) {
        return podcastManager.listFollowingPodcasts(till, to, dto);
    }

    @Override
    public ResponseEntity homePagePodcastListMobile(@PathVariable int till, @PathVariable int to, @RequestBody IdResponseDto dto) {
        return podcastManager.homePagePodcastListMobile(till, to, dto);
    }
}
