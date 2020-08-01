package com.pody.dto.responses;

import com.pody.dto.repositories.PodcastListDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MyPodcastsResponseDto {
    List<PodcastListDto> published;
    List<PodcastListDto> unpublished;
}
