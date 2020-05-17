package com.pody.repository;

import com.pody.model.History;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface HistoryRepository extends AbstractRepository<History, UUID> {
}
