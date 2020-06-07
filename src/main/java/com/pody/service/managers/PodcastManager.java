package com.pody.service.managers;

import com.pody.dto.requests.PodcastCreateDto;
import com.pody.dto.requests.RssDataDto;
import com.pody.dto.requests.StringRequestDto;
import com.pody.dto.requests.TwoIDRequestDto;
import com.pody.dto.responses.IdResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

public interface PodcastManager {

    ResponseEntity create(PodcastCreateDto dto);

    ResponseEntity read(UUID id);

    ResponseEntity update(PodcastCreateDto dto, UUID id);

    ResponseEntity delete(UUID id);

    ResponseEntity uploadPodcast(MultipartFile image, MultipartFile audio, UUID podcastId);

    ResponseEntity updateViewCount(UUID podcastId);

    ResponseEntity updateLikeCount(UUID podcastId);

    ResponseEntity updateDisLikeCount(UUID podcastId);

    ResponseEntity listPodcastsEachUser(UUID userId);

    ResponseEntity listPodcastsMostLiked(int till, int to);

    ResponseEntity listPodcastsMostViewed(int till, int to);

    ResponseEntity listPodcastsSuggested(int till, int to);

    ResponseEntity listPodcastsNewAdded(int till, int to);

    ResponseEntity addToListenLater(TwoIDRequestDto dto);

    ResponseEntity getRssData(RssDataDto rssDataDto);

    ResponseEntity listTrendingPodcasts();

    ResponseEntity homePagePodcastList(IdResponseDto dto);

    ResponseEntity getAllDataWithOnlyRssUrl(StringRequestDto dto);

    ResponseEntity listFollowingPodcasts(int till, int to, IdResponseDto dto);

    ResponseEntity homePagePodcastListMobile(int till, int to, IdResponseDto dto);

    ResponseEntity podcastListenLaterList(int till, int to, IdResponseDto dto);
}
