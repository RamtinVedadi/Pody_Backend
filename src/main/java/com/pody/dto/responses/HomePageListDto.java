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
public class HomePageListDto {
    @JsonProperty("categories")
    List<CategoryParentDto> categories;

    @JsonProperty("suggestions")
    List<PodcastListDto> suggestions;

    @JsonProperty("newAdded")
    List<PodcastListDto> latestReleased;

    @JsonProperty("mostView")
    List<PodcastListDto> mostViewed;

    @JsonProperty("mostLike")
    List<PodcastListDto> mostLiked;

    @JsonProperty("followings")
    List<PodcastListDto> followings;

    @JsonProperty("introduction")
    List<CategoryInfoDto> categoryIntroduction;

    @JsonProperty("news")
    List<NewsListDto> news;

    @JsonProperty("users")
    List<ChannelListenCountDto> users;

    @JsonProperty("blogs")
    List<BlogListDto> blogs;
}
