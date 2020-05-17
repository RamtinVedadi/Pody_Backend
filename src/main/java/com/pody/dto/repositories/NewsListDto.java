package com.pody.dto.repositories;

import java.util.Date;
import java.util.UUID;

public interface NewsListDto {
    UUID getId();

    String getTitle();

    String getDescription();

    String getImageAddress();

    int getLikeCount();

    int getDisLikeCount();

    Date getCreatedDate();

    UUID getUserId();

    String getUserTitle();

    String getProfileImageAddress();

    String getUsername();
}

