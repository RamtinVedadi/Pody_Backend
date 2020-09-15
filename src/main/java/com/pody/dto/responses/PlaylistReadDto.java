package com.pody.dto.responses;

import com.pody.dto.repositories.ChannelsListDto;
import com.pody.dto.repositories.PodcastListDto;
import com.pody.model.Playlist;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlaylistReadDto {
    private Playlist playlistInfo;
    private List<ChannelsListDto> playlistChannels;
    private List<PodcastListDto> playlistPodcasts;
}
