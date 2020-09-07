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
    @PostMapping(value = UrlStringMapping.URL0040, consumes = APPLICATION_JSON_UTF8_VALUE, produces = APPLICATION_JSON_UTF8_VALUE)
    ResponseEntity create(PodcastCreateDto dto);

    @GetMapping(value = UrlStringMapping.URL0041, produces = APPLICATION_JSON_UTF8_VALUE)
    ResponseEntity read(UUID id);

    @PutMapping(value = UrlStringMapping.URL0042, consumes = APPLICATION_JSON_UTF8_VALUE, produces = APPLICATION_JSON_UTF8_VALUE)
    ResponseEntity update(PodcastCreateDto dto, UUID id);

    @DeleteMapping(value = UrlStringMapping.URL0043, produces = APPLICATION_JSON_UTF8_VALUE)
    ResponseEntity delete(UUID id);

    @PostMapping(value = UrlStringMapping.URL0044)
    ResponseEntity uploadImage(MultipartFile image, MultipartFile audio, UUID podcastId);

    @PostMapping(value = UrlStringMapping.URL0061)
    ResponseEntity uploadPodcastWithoutCover(MultipartFile audio, UUID podcastId, UUID userId);

    @PostMapping(value = UrlStringMapping.URL0045, produces = APPLICATION_JSON_UTF8_VALUE)
    ResponseEntity updateViewCount(TwoIDRequestDto dto);

    @PostMapping(value = UrlStringMapping.URL0046, produces = APPLICATION_JSON_UTF8_VALUE)
    ResponseEntity updateLikeCount(TwoIDRequestDto dto);

    @PostMapping(value = UrlStringMapping.URL0047, produces = APPLICATION_JSON_UTF8_VALUE)
    ResponseEntity updateDisLikeCount(TwoIDRequestDto dto);

    @PostMapping(value = UrlStringMapping.URL0048, consumes = APPLICATION_JSON_UTF8_VALUE, produces = APPLICATION_JSON_UTF8_VALUE)
    ResponseEntity userLikeCheck(TwoIDRequestDto dto);

    @GetMapping(value = UrlStringMapping.URL0049, produces = APPLICATION_JSON_UTF8_VALUE)
    ResponseEntity listLikedPodcastsEachUser(UUID userId, int till, int to);

    @GetMapping(value = UrlStringMapping.URL0050, produces = APPLICATION_JSON_UTF8_VALUE)
    ResponseEntity listHistoryEachUser(UUID userId, int till, int to);

    @GetMapping(value = UrlStringMapping.URL0051, produces = APPLICATION_JSON_UTF8_VALUE)
    ResponseEntity listPodcastsEachUser(int flag, UUID userId, int till, int to);

    @GetMapping(value = UrlStringMapping.URL0052, produces = APPLICATION_JSON_UTF8_VALUE)
    ResponseEntity listPodcastsMostLiked(int till, int to);

    @GetMapping(value = UrlStringMapping.URL0053, produces = APPLICATION_JSON_UTF8_VALUE)
    ResponseEntity listPodcastsMostViewed(int till, int to);

    @GetMapping(value = UrlStringMapping.URL0054, produces = APPLICATION_JSON_UTF8_VALUE)
    ResponseEntity listPodcastsSuggested(int till, int to);

    @GetMapping(value = UrlStringMapping.URL0055, produces = APPLICATION_JSON_UTF8_VALUE)
    ResponseEntity listPodcastsNewAdded(int till, int to);

    @PostMapping(value = UrlStringMapping.URL0056, consumes = APPLICATION_JSON_UTF8_VALUE, produces = APPLICATION_JSON_UTF8_VALUE)
    ResponseEntity addToListenLater(TwoIDRequestDto dto);

    @PostMapping(value = UrlStringMapping.URL0057, consumes = APPLICATION_JSON_UTF8_VALUE, produces = APPLICATION_JSON_UTF8_VALUE)
    ResponseEntity podcastListenLaterList(int till, int to, IdResponseDto dto);

    @PostMapping(value = UrlStringMapping.URL0058, consumes = APPLICATION_JSON_UTF8_VALUE, produces = APPLICATION_JSON_UTF8_VALUE)
    ResponseEntity podcastListenLaterCheck(TwoIDRequestDto dto);

    @DeleteMapping(value = UrlStringMapping.URL0059, consumes = APPLICATION_JSON_UTF8_VALUE, produces = APPLICATION_JSON_UTF8_VALUE)
    ResponseEntity podcastListenLaterDelete(TwoIDRequestDto dto);

    @PostMapping(value = UrlStringMapping.URL0062, consumes = APPLICATION_JSON_UTF8_VALUE, produces = APPLICATION_JSON_UTF8_VALUE)
    ResponseEntity getUserRssData(RssDataDto rssDataDto);

    @GetMapping(value = UrlStringMapping.URL0063, produces = APPLICATION_JSON_UTF8_VALUE)
    ResponseEntity listTrendingPodcasts();

    @PostMapping(value = UrlStringMapping.URL0064, consumes = APPLICATION_JSON_UTF8_VALUE, produces = APPLICATION_JSON_UTF8_VALUE)
    ResponseEntity homePagePodcastList(IdResponseDto dto);

    @PostMapping(value = UrlStringMapping.URL0065, consumes = APPLICATION_JSON_UTF8_VALUE, produces = APPLICATION_JSON_UTF8_VALUE)
    ResponseEntity getAllDataWithOnlyRssUrl(StringRequestDto dto);

    @PostMapping(value = UrlStringMapping.URL0066, consumes = APPLICATION_JSON_UTF8_VALUE, produces = APPLICATION_JSON_UTF8_VALUE)
    ResponseEntity listFollowingPodcasts(int till, int to, IdResponseDto dto);

    @PostMapping(value = UrlStringMapping.URL0067, consumes = APPLICATION_JSON_UTF8_VALUE, produces = APPLICATION_JSON_UTF8_VALUE)
    ResponseEntity homePagePodcastListMobile(IdResponseDto dto);

    @PostMapping(value = UrlStringMapping.URL0068, consumes = APPLICATION_JSON_UTF8_VALUE, produces = APPLICATION_JSON_UTF8_VALUE)
    ResponseEntity homePagePodcastListMobileInfinite(int till, int to, IdResponseDto dto);

    @PostMapping(value = UrlStringMapping.URL0060, consumes = APPLICATION_JSON_UTF8_VALUE, produces = APPLICATION_JSON_UTF8_VALUE)
    ResponseEntity updateIsPublish(IdResponseDto dto, int flag);

    @PostMapping(value = UrlStringMapping.URL0069, consumes = APPLICATION_JSON_UTF8_VALUE, produces = APPLICATION_JSON_UTF8_VALUE)
    ResponseEntity rssForceUpdate(IdResponseDto dto);
}
