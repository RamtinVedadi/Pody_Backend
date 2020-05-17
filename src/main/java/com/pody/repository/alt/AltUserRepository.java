package com.pody.repository.alt;

import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.UUID;

@Repository
public class AltUserRepository {

    @PersistenceContext
    private EntityManager entityManager;

    List listHomePageUsersWithUserId(UUID userId) {


        return null;
    }
}
