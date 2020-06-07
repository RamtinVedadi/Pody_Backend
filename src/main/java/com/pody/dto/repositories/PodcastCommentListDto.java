package com.pody.dto.repositories;

import java.util.UUID;

public interface PodcastCommentListDto {

    UUID getCommentId();

    String getDescription();

    UUID getUserId();

    String getUserImage();

    String getUsername();

    String getUserTitle();

    String getCreatedDate();
}
