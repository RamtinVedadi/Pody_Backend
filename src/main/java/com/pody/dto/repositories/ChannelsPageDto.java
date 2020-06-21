package com.pody.dto.repositories;

import java.util.UUID;

public interface ChannelsPageDto {
    UUID getId();

    String getUsername();

    String getImageAddress();

    String getFirstName();

    String getLastName();

    int getFollowCount();

    String getUserTitle();

    String getUserBio();

    String getInstagramUrl();

    String getTwitterUrl();

    String getFacebookUrl();

    String getYoutubeUrl();

    String getWebsiteUrl();
}
