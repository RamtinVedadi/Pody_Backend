package com.pody.dto.repositories;


import java.util.UUID;

public interface UserSearchDto {
    UUID getId();

    String getUsername();

    String getUserTitle();

    String getImageAddress();

    int getFollowerCount();
}
