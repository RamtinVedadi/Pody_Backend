package com.pody.repository;

import com.pody.dto.repositories.HashtagSearchDto;
import com.pody.model.Hashtag;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface HashtagRepository extends AbstractRepository<Hashtag, UUID> {

    @Query("select h.id as id, h.name as name from Hashtag h where h.name like :name")
    List<HashtagSearchDto> searchHashtag(@Param("name") String name);

    Hashtag findOneByName(String name);
}
