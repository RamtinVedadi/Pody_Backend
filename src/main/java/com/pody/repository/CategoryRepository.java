package com.pody.repository;

import com.pody.dto.repositories.CategorySearchDto;
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
            " where c.name like :name or c.englishName like :name or u.username like :name or u.title like :name")
    List<CategorySearchDto> searchCategory(@Param("name") String name);

    @Query("select new Category(c.id, c.name, c.imageAddress) from Category c where c.parent.id is null ")
    List<Category> listCategoryParents(Sort sort);

    @Query("select new Category(c.id, c.name, c.imageAddress) " +
            "from Category c inner join PodcastCategory pc on c.id = pc.category.id " +
            " inner join Podcast p on pc.podcast.id = p.id ")
    List<Category> listTrendingCategory(Pageable pageable);

    @Query("select new Category(c.id, c.name, c.imageAddress) from Category c where c.parent.id  = :id ")
    List<Category> listCategoryChildren(@Param("id") UUID id);

    @Modifying
    @Query("update Category c set c.imageAddress = :icon where c.id = :id ")
    int uploadCategoryIcon(@Param("icon") String icon, @Param("id") UUID id);


    Category findOneByEnglishName(String englishName);
}
