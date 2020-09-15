package com.pody.contoller.impl;

import com.pody.contoller.PlaylistController;
import com.pody.dto.responses.IdResponseDto;
import com.pody.model.Playlist;
import com.pody.model.PlaylistChannels;
import com.pody.service.managers.PlaylistManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RestController
@CrossOrigin(origins = {"*", "http://pody.ir", "http://www.pody.ir"}, maxAge = 3600)
public class PlaylistControllerImpl implements PlaylistController {
    @Autowired
    private PlaylistManager playlistManager;

    @Override
    public ResponseEntity create(@RequestBody Playlist playlist) {
        return playlistManager.create(playlist);
    }

    @Override
    public ResponseEntity addChannel(@RequestBody List<PlaylistChannels> channels) {
        return playlistManager.addChannel(channels);
    }

    @Override
    public ResponseEntity updatePlaylist(@RequestBody Playlist playlist, @PathVariable UUID id) {
        return playlistManager.update(playlist, id);
    }

    @Override
    public ResponseEntity playlistUploadImage(@RequestParam("image") MultipartFile image, @PathVariable UUID id) {
        return playlistManager.uploadPlaylistImage(image, id);
    }

    @Override
    public ResponseEntity readPlaylist(@RequestBody IdResponseDto dto) {
        return playlistManager.readPlaylist(dto);
    }

    @Override
    public ResponseEntity readAllPlaylist() {
        return playlistManager.readAll();
    }
}
