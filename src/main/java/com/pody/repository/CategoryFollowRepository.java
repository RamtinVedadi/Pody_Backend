package com.pody.repository;

import com.pody.model.CategoryFollow;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CategoryFollowRepository extends AbstractRepository<CategoryFollow, UUID> {
    @Modifying
    @Query("delete from CategoryFollow cf where cf.category.id = :categoryId and cf.follower.id = :followerId")
    int deleteFollowership(@Param("categoryId") UUID categoryid, @Param("followerId") UUID followerid);

    @Query("select new CategoryFollow (cf.id) from CategoryFollow cf where cf.follower.id = :userId and cf.category.id = :categoryId ")
    CategoryFollow isCategoryFollowAvailable(@Param("userId") UUID userId, @Param("categoryId") UUID categoryId);
}
