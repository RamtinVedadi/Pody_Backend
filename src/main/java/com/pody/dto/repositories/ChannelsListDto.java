package com.pody.dto.repositories;

import java.util.UUID;

public interface ChannelsListDto {
    UUID getId();

    String getUsername();

    String getImageAddress();

    int getFollowCount();

    String getUserTitle();

    String getUserBio();
}
