package com.pody.service.managers.impl;

import com.pody.dto.repositories.ChannelsListDto;
import com.pody.dto.repositories.IdListDto;
import com.pody.dto.repositories.PodcastListDto;
import com.pody.dto.responses.IdResponseDto;
import com.pody.dto.responses.PlaylistReadDto;
import com.pody.model.Playlist;
import com.pody.model.PlaylistChannels;
import com.pody.model.PlaylistPodcasts;
import com.pody.model.Podcast;
import com.pody.repository.*;
import com.pody.service.ErrorJsonHandler;
import com.pody.service.managers.PlaylistManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class PlaylistManagerImpl implements PlaylistManager {
    private PlaylistChannelsRepository playlistChannelsRepository;
    private PlaylistPodcastsRepository playlistPodcastsRepository;
    private PlaylistRepository playlistRepository;
    private PodcastRepository podcastRepository;
    private UserRepository userRepository;

    @Autowired
    public PlaylistManagerImpl(PlaylistChannelsRepository playlistChannelsRepository, PlaylistPodcastsRepository playlistPodcastsRepository, PlaylistRepository playlistRepository, PodcastRepository podcastRepository, UserRepository userRepository) {
        this.playlistChannelsRepository = playlistChannelsRepository;
        this.playlistPodcastsRepository = playlistPodcastsRepository;
        this.playlistRepository = playlistRepository;
        this.podcastRepository = podcastRepository;
        this.userRepository = userRepository;
    }

    @Override
    public ResponseEntity create(Playlist playlist) {
        try {
            if (playlist != null) {
                playlist.setId(null);
                playlist.setCreatedDate(new Date());
                playlist.setUpdateDate(new Date());
                playlist.setImageAddress("http://pody.ir/defaultImages/default.jpg");
                playlist = playlistRepository.save(playlist);

                return ResponseEntity.ok(playlist);
            } else {
                return new ResponseEntity(ErrorJsonHandler.EMPTY_BODY, HttpStatus.BAD_REQUEST);
            }
        } catch (NullPointerException e) {
            return new ResponseEntity(ErrorJsonHandler.NULL_POINTER_EXCEPTION, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity addChannel(List<PlaylistChannels> channels) {
        try {
            if (channels != null) {
                for (PlaylistChannels pc : channels) {
                    PlaylistChannels playlistChannel = playlistChannelsRepository.save(pc);
                    if (playlistChannel != null) {
                        List<IdListDto> podcasts = podcastRepository.listPodcastsForPlaylist(playlistChannel.getChannel().getId(), PageRequest.of(0, 2, Sort.by(Sort.Direction.DESC, "viewCount")));
                        if (podcasts.size() > 0) {
                            for (IdListDto pid : podcasts) {
                                PlaylistPodcasts pp = new PlaylistPodcasts();
                                pp.setId(null);
                                pp.setPlaylist(playlistChannel.getPlaylist());
                                pp.setPodcast(new Podcast(pid.getPodcastId()));

                                PlaylistPodcasts save = playlistPodcastsRepository.save(pp);
                                if (save != null) {
                                    continue;
                                } else {
                                    return new ResponseEntity(ErrorJsonHandler.PROBLEM, HttpStatus.BAD_REQUEST);
                                }
                            }
                        }
                    } else {
                        return new ResponseEntity(ErrorJsonHandler.PROBLEM, HttpStatus.BAD_REQUEST);
                    }
                }

                return ResponseEntity.ok(ErrorJsonHandler.SUCCESSFUL);
            } else {
                return new ResponseEntity(ErrorJsonHandler.EMPTY_BODY, HttpStatus.BAD_REQUEST);
            }
        } catch (NullPointerException e) {
            return new ResponseEntity(ErrorJsonHandler.NULL_POINTER_EXCEPTION, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity update(Playlist playlist, UUID id) {
        return null;
    }

    @Override
    public ResponseEntity uploadPlaylistImage(MultipartFile image, UUID id) {
        try {
            if (id != null) {
                if (image != null) {
                    int resultImage;
                    String filePath = "";
                    try {
                        Path copyLocation = Paths
                                .get("C:/xampp/htdocs/coverImages" + File.separator + StringUtils.cleanPath(id.toString() + ".jpg"));
                        Files.copy(image.getInputStream(), copyLocation, StandardCopyOption.REPLACE_EXISTING);
                        InetAddress ip = InetAddress.getLocalHost();
                        filePath = "http://localhost/coverImages/" + StringUtils.cleanPath(id.toString() + ".jpg");
                    } catch (IOException e) {
                        return new ResponseEntity(ErrorJsonHandler.IO_EXCEPTION, HttpStatus.INTERNAL_SERVER_ERROR);
                    }
                    resultImage = playlistRepository.updateImageAddress(filePath, id);

                    if (resultImage == 1) {
                        return ResponseEntity.ok(ErrorJsonHandler.SUCCESSFUL);
                    } else {
                        return new ResponseEntity(ErrorJsonHandler.NULL_POINTER_EXCEPTION, HttpStatus.INTERNAL_SERVER_ERROR);
                    }
                } else {
                    return new ResponseEntity(ErrorJsonHandler.EMPTY_FILE, HttpStatus.BAD_REQUEST);
                }
            } else {
                return new ResponseEntity(ErrorJsonHandler.EMPTY_ID_FIELD, HttpStatus.BAD_REQUEST);
            }
        } catch (NullPointerException e) {
            return new ResponseEntity(ErrorJsonHandler.NULL_POINTER_EXCEPTION, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity readPlaylist(IdResponseDto dto) {
        try {
            if (dto != null) {
                PlaylistReadDto prd = new PlaylistReadDto();
                Playlist playlist = playlistRepository.findOneById(dto.getId());
                prd.setPlaylistInfo(playlist);

                List<ChannelsListDto> playlistChannelsList = userRepository.listChannelsPlaylist(dto.getId());
                prd.setPlaylistChannels(playlistChannelsList);


                List<PodcastListDto> playlistPodcasts = podcastRepository.listPodcastsPlaylist(dto.getId(), PageRequest.of(0, 100, Sort.by(Sort.Direction.DESC, "viewCount")));
                prd.setPlaylistPodcasts(playlistPodcasts);

                return ResponseEntity.ok(prd);
            } else {
                return new ResponseEntity(ErrorJsonHandler.EMPTY_BODY, HttpStatus.BAD_REQUEST);
            }
        } catch (NullPointerException e) {
            return new ResponseEntity(ErrorJsonHandler.NULL_POINTER_EXCEPTION, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity readAll() {
        try {
            List<com.pody.dto.repositories.PlaylistReadDto> playlistRead = playlistRepository.listAllPlaylists();

            return ResponseEntity.ok(playlistRead);
        } catch (NullPointerException e) {
            return new ResponseEntity(ErrorJsonHandler.NULL_POINTER_EXCEPTION, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
