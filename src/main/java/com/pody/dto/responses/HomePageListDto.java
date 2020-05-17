package com.pody.dto.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.pody.dto.repositories.CategoryParentListDto;
import com.pody.dto.repositories.ChannelsListDto;
import com.pody.dto.repositories.NewsListDto;
import com.pody.dto.repositories.PodcastListDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HomePageListDto {
    @JsonProperty("categories")
    List<CategoryParentListDto> categories;

    @JsonProperty("suggestions")
    List<PodcastListDto> suggestions;

    @JsonProperty("newAdded")
    List<PodcastListDto> latestReleased;

    @JsonProperty("mostView")
    List<PodcastListDto> mostViewed;

    @JsonProperty("mostLike")
    List<PodcastListDto> mostLiked;

    @JsonProperty("news")
    List<NewsListDto> news;

    @JsonProperty("users")
    List<ChannelsListDto> users;

}
