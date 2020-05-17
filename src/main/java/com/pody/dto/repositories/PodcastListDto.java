package com.pody.dto.repositories;

import java.util.UUID;

public interface PodcastListDto {
    UUID getPodcastId();

    String getTitle();

    String getPodcastImage();

    String getAudioAddress();

    Integer getEpisodeNumber();

    Integer getSeasonNumber();

    String getShortDescription();

    String getUserTitle();

    String getDuration();

    int getViewCount();

    int getLikeCount();

    UUID getUserId();

    String getProfileImageAddress();

    String getUsername();

    UUID getCategoryId();

    String getCategoryName();

    String getCategoryIcon();
}
