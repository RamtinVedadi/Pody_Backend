package com.pody.dto.responses;

import com.pody.dto.repositories.ChannelsListDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChannelListenCountDto {
    private ChannelsListDto channelInfo;
    private Integer listenCount;
}
