package com.pody.dto.responses;

import com.pody.dto.repositories.PodcastCommentListDto;
import com.pody.dto.repositories.PodcastReadDto;
import com.pody.dto.repositories.PodcastListDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PodcastReadResultDto {
    private PodcastReadDto podcastInfo;
    private List<PodcastCommentListDto> commentsList;
    private PodcastListDto previousEpisode;
    private PodcastListDto nextEpisode;
    private List<PodcastListDto> sameCategory;
}
