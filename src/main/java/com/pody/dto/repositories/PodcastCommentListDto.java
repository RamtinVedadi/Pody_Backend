package com.pody.dto.repositories;

import java.util.UUID;

public interface PodcastCommentListDto {

    public UUID getCommentId();

    public String getDescription();

    public UUID getUserId();

    public String getUserImage();

    public String getUsername();

    public String getUserTitle();
}
