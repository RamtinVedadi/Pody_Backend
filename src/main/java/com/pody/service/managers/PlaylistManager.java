package com.pody.service.managers;

import com.pody.dto.responses.IdResponseDto;
import com.pody.model.Playlist;
import com.pody.model.PlaylistChannels;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

public interface PlaylistManager {
    ResponseEntity create(Playlist playlist);

    ResponseEntity addChannel(List<PlaylistChannels> channels);

    ResponseEntity update(Playlist playlist, UUID id);

    ResponseEntity uploadPlaylistImage(MultipartFile image, UUID id);

    ResponseEntity readPlaylist(IdResponseDto dto);

    ResponseEntity readAll();
}
