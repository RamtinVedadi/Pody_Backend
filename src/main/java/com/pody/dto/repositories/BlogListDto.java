package com.pody.dto.repositories;

import java.util.Date;
import java.util.UUID;

public interface BlogListDto {

    UUID getId();

    int getViewCount();

    int getLikeCount();

    String getTitle();

    Date getCreatedDate();

    String getBlogImage();

    String getShortDescription();

    UUID getUserId();

    String getUsername();

    String getUserTitle();

    String getProfileImageAddress();
}
