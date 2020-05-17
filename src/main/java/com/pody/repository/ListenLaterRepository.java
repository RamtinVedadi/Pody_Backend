package com.pody.repository;

import com.pody.model.ListenLater;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ListenLaterRepository extends AbstractRepository<ListenLater, UUID> {
}
