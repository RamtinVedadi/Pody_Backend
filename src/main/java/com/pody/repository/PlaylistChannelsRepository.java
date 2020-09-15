package com.pody.repository;

import com.pody.model.PlaylistChannels;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PlaylistChannelsRepository extends AbstractRepository<PlaylistChannels, UUID> {
}
