package com.pody.contoller;

import com.pody.dto.responses.IdResponseDto;
import com.pody.model.Playlist;
import com.pody.model.PlaylistChannels;
import com.pody.service.UrlStringMapping;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

public interface PlaylistController {
    @PostMapping(value = UrlStringMapping.URL0300, consumes = APPLICATION_JSON_UTF8_VALUE, produces = APPLICATION_JSON_UTF8_VALUE)
    ResponseEntity create(Playlist playlist);

    @PostMapping(value = UrlStringMapping.URL0301, consumes = APPLICATION_JSON_UTF8_VALUE, produces = APPLICATION_JSON_UTF8_VALUE)
    ResponseEntity addChannel(List<PlaylistChannels> channels);

    @PostMapping(value = UrlStringMapping.URL0302, consumes = APPLICATION_JSON_UTF8_VALUE, produces = APPLICATION_JSON_UTF8_VALUE)
    ResponseEntity updatePlaylist(Playlist playlist, UUID id);

    @PostMapping(value = UrlStringMapping.URL0303)
    ResponseEntity playlistUploadImage(MultipartFile image, UUID id);

    @PostMapping(value = UrlStringMapping.URL0304, consumes = APPLICATION_JSON_UTF8_VALUE, produces = APPLICATION_JSON_UTF8_VALUE)
    ResponseEntity readPlaylist(IdResponseDto dto);

    @GetMapping(value = UrlStringMapping.URL0305, produces = APPLICATION_JSON_UTF8_VALUE)
    ResponseEntity readAllPlaylist();
}
