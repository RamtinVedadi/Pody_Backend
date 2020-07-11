package com.pody.dto.repositories;

import java.util.Date;
import java.util.UUID;

public interface BlogReadDto {

    UUID getId();

    int getViewCount();

    int getLikeCount();

    String getTitle();

    Date getCreatedDate();

    String getBlogImage();

    String getDescription();

    UUID getUserId();

    String getUsername();

    String getUserTitle();

    String getProfileImageAddress();
}
