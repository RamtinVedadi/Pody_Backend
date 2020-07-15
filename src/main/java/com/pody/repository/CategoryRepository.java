package com.pody.repository;

import com.pody.dto.repositories.CategoryParentDto;
import com.pody.dto.repositories.CategorySearchDto;
import com.pody.dto.repositories.CategorySitemapDto;
import com.pody.model.Category;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CategoryRepository extends AbstractRepository<Category, UUID> {
    @Override
    Category save(Category category);

    @Query("select distinct c.id as id, c.name as name, c.imageAddress as imageAddress " +
            " from Category c inner join UserCategory uc on c.id = uc.category.id " +
            " inner join User u on uc.user.id = u.id " +
            " where c.name like :lowerCase or c.englishName like :lowerCase or u.username like :lowerCase or u.title like :lowerCase or " +
            " c.name like :upperCase or c.englishName like :upperCase or u.username like :upperCase or u.title like :upperCase")
    List<CategorySearchDto> searchCategory(@Param("lowerCase") String lowerCase, @Param("upperCase") String upperCase);

    @Query("select distinct c.id as id, c.name as name, c.imageAddress as imageAddress " +
            " from Category c inner join CategoryFollow cf on c.id = cf.category.id where cf.follower.id = :userId")
    List<CategorySearchDto> followingCategoryList(@Param("userId") UUID userId);

    @Query("select c.id as id, c.name as name, c.imageAddress as imageAddress, c.description as description from Category c where c.parent.id is null ")
    List<CategoryParentDto> listCategoryParents(Sort sort);

    @Query("select distinct c.id as id, c.name as name, c.imageAddress as imageAddress " +
            " from Category c inner join PodcastCategory pc on c.id = pc.category.id " +
            " inner join Podcast p on pc.podcast.id = p.id ")
    List<CategorySearchDto> listTrendingCategory(Pageable pageable);

    @Query("select c.id as id, c.name as name, c.imageAddress as imageAddress from Category c where c.parent.id  = :id ")
    List<CategorySearchDto> listCategoryChildren(@Param("id") UUID id);

    @Query("select c.id as id, c.createdDate as createdDate from Category c where c.parent.id  is null ")
    List<CategorySitemapDto> listCategorySitemap(Pageable pageable);

    Category findOneByEnglishName(String englishName);
}
