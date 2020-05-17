package com.pody.repository;

import com.pody.model.CommentHashtag;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CommentHashtagRepository extends AbstractRepository<CommentHashtag, UUID> {
}
