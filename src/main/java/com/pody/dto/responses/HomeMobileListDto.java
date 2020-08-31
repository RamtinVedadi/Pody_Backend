package com.pody.dto.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.pody.dto.repositories.*;
import com.pody.dto.repositories.PodcastListDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HomeMobileListDto {
    @JsonProperty("categories")
    List<CategoryParentDto> categories;

    @JsonProperty("partOne")
    List<PodcastListDto> partOnePodcasts;

    @JsonProperty("firstNews")
    List<NewsListDto> firstNews;

    @JsonProperty("partTwo")
    List<PodcastListDto> partTwoPodcasts;

    @JsonProperty("secondNews")
    List<NewsListDto> secondNews;

    @JsonProperty("users")
    List<ChannelsListDto> users;

    @JsonProperty("special")
    List<SpecialPodcastsDto> specials;
}
