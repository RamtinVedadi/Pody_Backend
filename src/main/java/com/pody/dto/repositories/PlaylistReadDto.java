package com.pody.dto.repositories;

import java.util.UUID;

public interface PlaylistReadDto {
    UUID getId();

    String getTitle();

    String getDescription();

    String getImageAddress();
}
