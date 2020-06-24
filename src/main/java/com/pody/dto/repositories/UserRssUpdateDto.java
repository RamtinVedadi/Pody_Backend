package com.pody.dto.repositories;

import java.util.UUID;

public interface UserRssUpdateDto {
    UUID getId();

    String getRssUrl();

    String getProfileImageAddress();

    String getTitle();
}
