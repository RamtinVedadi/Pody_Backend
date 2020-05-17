package com.pody.repository;

import com.pody.model.UserHashtag;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UserHashtagRepository extends AbstractRepository<UserHashtag, UUID> {
}
