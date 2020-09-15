package com.pody.repository;

import com.pody.dto.repositories.PlaylistReadDto;
import com.pody.model.Playlist;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PlaylistRepository extends AbstractRepository<Playlist, UUID> {
    @Modifying
    @Query("update Playlist p set p.imageAddress = :imagePath where p.id = :id")
    int updateImageAddress(@Param("imagePath") String imagePath, @Param("id") UUID id);

    @Query("select p.id as id, p.title as title, p.description as description, p.imageAddress as imageAddress from Playlist p")
    List<PlaylistReadDto> listAllPlaylists();
}
