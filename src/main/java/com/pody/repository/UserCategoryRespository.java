package com.pody.repository;

import com.pody.model.Category;
import com.pody.model.User;
import com.pody.model.UserCategory;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UserCategoryRespository extends AbstractRepository<UserCategory, UUID> {

    UserCategory findOneByCategoryAndUser(Category category, User user);
}
