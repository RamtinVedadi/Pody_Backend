package com.pody.dto.responses;

import com.pody.dto.repositories.ChannelsListDto;
import com.pody.dto.repositories.PodcastListDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SubscriptionsDto {
    private ChannelsListDto channelInfo;
    private List<PodcastListDto> channelPodcasts;
}
