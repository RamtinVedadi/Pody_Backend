package com.pody.contoller;

import com.pody.dto.requests.PodcastCreateDto;
import com.pody.dto.requests.RssDataDto;
import com.pody.dto.requests.StringRequestDto;
import com.pody.dto.requests.TwoIDRequestDto;
import com.pody.dto.responses.IdResponseDto;
import com.pody.service.UrlStringMapping;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

public interface PodcastController {
    @PostMapping(value = UrlStringMapping.URL015, consumes = APPLICATION_JSON_UTF8_VALUE, produces = APPLICATION_JSON_UTF8_VALUE)
    ResponseEntity create(PodcastCreateDto dto);

    @GetMapping(value = UrlStringMapping.URL016, produces = APPLICATION_JSON_UTF8_VALUE)
    ResponseEntity read(UUID id);

    @PutMapping(value = UrlStringMapping.URL017, consumes = APPLICATION_JSON_UTF8_VALUE, produces = APPLICATION_JSON_UTF8_VALUE)
    ResponseEntity update(PodcastCreateDto dto, UUID id);

    @DeleteMapping(value = UrlStringMapping.URL018, produces = APPLICATION_JSON_UTF8_VALUE)
    ResponseEntity delete(UUID id);

    @PostMapping(value = UrlStringMapping.URL019)
    ResponseEntity uploadImage(MultipartFile image, MultipartFile audio, UUID podcastId);

    @GetMapping(value = UrlStringMapping.URL021, produces = APPLICATION_JSON_UTF8_VALUE)
    ResponseEntity updateViewCount(UUID podcastId);

    @GetMapping(value = UrlStringMapping.URL022, produces = APPLICATION_JSON_UTF8_VALUE)
    ResponseEntity updateLikeCount(UUID podcastId);

    @GetMapping(value = UrlStringMapping.URL023, produces = APPLICATION_JSON_UTF8_VALUE)
    ResponseEntity listPodcastsEachUser(UUID userId);

    @GetMapping(value = UrlStringMapping.URL024, produces = APPLICATION_JSON_UTF8_VALUE)
    ResponseEntity listPodcastsMostLiked(int till, int to);

    @GetMapping(value = UrlStringMapping.URL025, produces = APPLICATION_JSON_UTF8_VALUE)
    ResponseEntity listPodcastsMostViewed(int till, int to);

    @GetMapping(value = UrlStringMapping.URL026, produces = APPLICATION_JSON_UTF8_VALUE)
    ResponseEntity listPodcastsSuggested(int till, int to);

    @GetMapping(value = UrlStringMapping.URL027, produces = APPLICATION_JSON_UTF8_VALUE)
    ResponseEntity listPodcastsNewAdded(int till, int to);

    @PostMapping(value = UrlStringMapping.URL028, consumes = APPLICATION_JSON_UTF8_VALUE, produces = APPLICATION_JSON_UTF8_VALUE)
    ResponseEntity addToListenLater(TwoIDRequestDto dto);

    @PostMapping(value = UrlStringMapping.URL087, consumes = APPLICATION_JSON_UTF8_VALUE, produces = APPLICATION_JSON_UTF8_VALUE)
    ResponseEntity podcastListenLaterList(int till, int to, IdResponseDto dto);

    @GetMapping(value = UrlStringMapping.URL029, produces = APPLICATION_JSON_UTF8_VALUE)
    ResponseEntity addToHistory(UUID id);

    @GetMapping(value = UrlStringMapping.URL071, produces = APPLICATION_JSON_UTF8_VALUE)
    ResponseEntity userPodcastHistoryList(UUID userId);

    @PostMapping(value = UrlStringMapping.URL072, consumes = APPLICATION_JSON_UTF8_VALUE, produces = APPLICATION_JSON_UTF8_VALUE)
    ResponseEntity getUserRssData(RssDataDto rssDataDto);

    @GetMapping(value = UrlStringMapping.URL075, produces = APPLICATION_JSON_UTF8_VALUE)
    ResponseEntity listTrendingPodcasts();

    @PostMapping(value = UrlStringMapping.URL077, consumes = APPLICATION_JSON_UTF8_VALUE, produces = APPLICATION_JSON_UTF8_VALUE)
    ResponseEntity homePagePodcastList(IdResponseDto dto);

    @PostMapping(value = UrlStringMapping.URL082, consumes = APPLICATION_JSON_UTF8_VALUE, produces = APPLICATION_JSON_UTF8_VALUE)
    ResponseEntity getAllDataWithOnlyRssUrl(StringRequestDto dto);

    @PostMapping(value = UrlStringMapping.URL083, consumes = APPLICATION_JSON_UTF8_VALUE, produces = APPLICATION_JSON_UTF8_VALUE)
    ResponseEntity listFollowingPodcasts(int till, int to, IdResponseDto dto);

    @PostMapping(value = UrlStringMapping.URL085, consumes = APPLICATION_JSON_UTF8_VALUE, produces = APPLICATION_JSON_UTF8_VALUE)
    ResponseEntity homePagePodcastListMobile(int till, int to, IdResponseDto dto);
}
