package com.pody.dto.responses;

import com.pody.dto.repositories.ChannelsPageDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChannelsDto {
    private ChannelsPageDto channelInfo;
    private Integer listenCount;
    private Integer episodeCount;
    private Boolean isFollow;
}
