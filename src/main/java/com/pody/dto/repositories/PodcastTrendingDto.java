package com.pody.dto.repositories;

import java.util.UUID;

public interface PodcastTrendingDto {
    UUID getPodcastId();

    String getTitle();

    String getPodcastImage();

    String getAudioAddress();

    Integer getEpisodeNumber();

    Integer getSeasonNumber();

    String getUserTitle();

    String getDuration();

    String getShortDescription();

    int getViewCount();

    int getLikeCount();

    UUID getUserId();

    String getProfileImageAddress();

    String getUsername();
}
