package com.pody.dto.responses;

import com.pody.dto.repositories.NewsListDto;
import com.pody.dto.repositories.PodcastListDto;
import com.pody.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserReadResultDto {
    private User userInfo;
    private List<PodcastListDto> userPodcasts;
    private List<NewsListDto> userNews;
    private Boolean isFollow;
}
