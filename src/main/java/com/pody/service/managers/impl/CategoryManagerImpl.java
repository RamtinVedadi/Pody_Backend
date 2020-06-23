package com.pody.service.managers.impl;

import com.pody.dto.repositories.CategoryParentListDto;
import com.pody.dto.repositories.CategorySearchDto;
import com.pody.dto.repositories.PodcastListDto;
import com.pody.dto.requests.TwoIDRequestDto;
import com.pody.dto.responses.CategoryDto;
import com.pody.dto.responses.CategoryInfoDto;
import com.pody.dto.responses.CategoryReadResultDto;
import com.pody.dto.responses.IdResponseDto;
import com.pody.model.Category;
import com.pody.model.CategoryFollow;
import com.pody.repository.CategoryFollowRepository;
import com.pody.repository.CategoryRepository;
import com.pody.repository.PodcastRepository;
import com.pody.service.ErrorJsonHandler;
import com.pody.service.managers.CategoryManager;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class CategoryManagerImpl implements CategoryManager {

    private CategoryFollowRepository categoryFollowRepository;
    private CategoryRepository categoryRepository;
    private PodcastRepository podcastRepository;
    private ModelMapper modelMapper;

    @Autowired
    public CategoryManagerImpl(CategoryFollowRepository categoryFollowRepository, CategoryRepository categoryRepository,
                               PodcastRepository podcastRepository, ModelMapper modelMapper) {
        this.categoryFollowRepository = categoryFollowRepository;
        this.categoryRepository = categoryRepository;
        this.podcastRepository = podcastRepository;
        this.modelMapper = modelMapper;
    }

    @Override //Tested
    public ResponseEntity create(Category category) {
        try {
            if (category != null) {
                category.setCreatedDate(new Date());
                category.setUpdateDate(new Date());
                Category result = categoryRepository.save(category);
                return ResponseEntity.ok(result);
            } else {
                return new ResponseEntity(ErrorJsonHandler.EMPTY_BODY, HttpStatus.BAD_REQUEST);
            }
        } catch (NullPointerException e) {
            return new ResponseEntity(ErrorJsonHandler.NULL_POINTER_EXCEPTION, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override //Tested but CategoryFollow
    public ResponseEntity read(TwoIDRequestDto dto) {
        //first id is for category id
        //second id is for logined user id
        try {
            if (dto != null) {
                if (dto.getFirstID() != null) {
                    CategoryReadResultDto crrd = new CategoryReadResultDto();

                    Category categoryInfo = categoryRepository.findOneById(dto.getFirstID());
                    crrd.setCategoryInfo(categoryInfo);

                    List<PodcastListDto> categoryPodcasts = podcastRepository.listPodcastsEachCategory(dto.getFirstID(), PageRequest.of(0, 24, Sort.by(Sort.Direction.DESC, "createdDate")));
                    crrd.setCategoryPodcasts(categoryPodcasts);

                    if (dto.getSecondID() == null) {
                        crrd.setIsFollow(false);
                    } else {
                        CategoryFollow cf = categoryFollowRepository.isCategoryFollowAvailable(dto.getSecondID(), dto.getFirstID());
                        if (cf != null) {
                            crrd.setIsFollow(true);
                        } else {
                            crrd.setIsFollow(false);
                        }
                    }

                    return ResponseEntity.ok(crrd);
                } else {

                    return ResponseEntity.ok(ErrorJsonHandler.EMPTY_ID_FIELD);
                }

            } else {
                return new ResponseEntity(ErrorJsonHandler.EMPTY_BODY, HttpStatus.BAD_REQUEST);
            }
        } catch (NullPointerException e) {
            return new ResponseEntity(ErrorJsonHandler.NULL_POINTER_EXCEPTION, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override //Tested
    public ResponseEntity update(Category category, UUID id) {
        try {
            if (id != null) {
                if (category != null) {

                    Category dbCategory = categoryRepository.findOneById(id);

                    category.setUpdateDate(new Date());

                    if (category.getParent() != null) {
                        if (dbCategory.getParent() != null) {
                            if (category.getParent().getId() != null) {
                                category.setParent(categoryRepository.findOneById(category.getParent().getId()));
                            } else {
                                category.setParent(null);
                            }
                        }
                    } else {
                        if (dbCategory.getParent().getId() != null) {
                            category.setParent(dbCategory.getParent());
                        } else {
                            category.setParent(null);
                        }
                    }
                    modelMapper.map(category, dbCategory);

                    Category result = categoryRepository.save(dbCategory);
                    return ResponseEntity.ok(result);
                } else {
                    return new ResponseEntity(ErrorJsonHandler.EMPTY_BODY, HttpStatus.BAD_REQUEST);
                }
            } else {
                return new ResponseEntity(ErrorJsonHandler.EMPTY_ID_FIELD, HttpStatus.BAD_REQUEST);
            }
        } catch (NullPointerException e) {
            return new ResponseEntity(ErrorJsonHandler.NULL_POINTER_EXCEPTION, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override //Tested
    public ResponseEntity delete(UUID id) {
        try {
            if (id != null) {
                categoryRepository.deleteById(id);
                return ResponseEntity.ok(ErrorJsonHandler.SUCCESSFUL);
            } else {
                return new ResponseEntity(ErrorJsonHandler.EMPTY_BODY, HttpStatus.BAD_REQUEST);
            }
        } catch (NullPointerException e) {
            return new ResponseEntity(ErrorJsonHandler.NULL_POINTER_EXCEPTION, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override //Tested
    @Transactional(readOnly = true)
    public ResponseEntity tree() {
        List<Category> categories = categoryRepository.findAll();
        Queue<CategoryDto> queue = new ArrayDeque<>();
        CategoryDto root = new CategoryDto();
        root.name = "DUMMY";
        root.id = null;
        queue.add(root);
        while (!queue.isEmpty()) {
            CategoryDto head = queue.remove();
            List<CategoryDto> children = findChildrenOf(head, categories);
            if (children != null && children.isEmpty()) {
                children = null;
            }
            head.children = children;
            if (children != null) {
                queue.addAll(children);
            }
        }
        return ResponseEntity.ok(root);
    }

    @Override //Tested
    public ResponseEntity listParents() {
        try {
            List<Category> listParents = categoryRepository.listCategoryParents(Sort.by(Sort.Direction.ASC, "name"));
            List<CategoryParentListDto> list = listParents.stream().map(c -> modelMapper.map(c, CategoryParentListDto.class)).collect(Collectors.toList());
            return ResponseEntity.ok(list);
        } catch (NullPointerException e) {
            return new ResponseEntity(ErrorJsonHandler.NULL_POINTER_EXCEPTION, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override //Tested
    public ResponseEntity listChildren(UUID id) {
        try {
            if (id != null) {
                List<Category> listChildren = categoryRepository.listCategoryChildren(id);
                List<CategorySearchDto> list = listChildren.stream().map(c -> modelMapper.map(c, CategorySearchDto.class)).collect(Collectors.toList());
                return ResponseEntity.ok(list);
            } else {
                return new ResponseEntity(ErrorJsonHandler.EMPTY_ID_FIELD, HttpStatus.BAD_REQUEST);
            }
        } catch (NullPointerException e) {
            return new ResponseEntity(ErrorJsonHandler.NULL_POINTER_EXCEPTION, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity listAllCategoryPage(IdResponseDto dto) {
        //This id is logined user id
        try {
            List<Category> listParents = categoryRepository.listCategoryParents(Sort.by(Sort.Direction.ASC, "name"));
            List<CategoryReadResultDto> finalList = new ArrayList<>();

            for (Category c : listParents) {
                CategoryReadResultDto crrd = new CategoryReadResultDto();

                Category categoryInfo = categoryRepository.findOneById(c.getId());
                crrd.setCategoryInfo(categoryInfo);

                List<PodcastListDto> categoryPodcasts = podcastRepository.listPodcastsEachCategory(c.getId(), PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "createdDate")));
                crrd.setCategoryPodcasts(categoryPodcasts);

                if (dto.getId() == null) {
                    crrd.setIsFollow(false);
                } else {
                    CategoryFollow cf = categoryFollowRepository.isCategoryFollowAvailable(dto.getId(), c.getId());
                    if (cf != null) {
                        crrd.setIsFollow(true);
                    } else {
                        crrd.setIsFollow(false);
                    }
                }

                finalList.add(crrd);
            }

            return ResponseEntity.ok(finalList);
        } catch (NullPointerException e) {
            return new ResponseEntity(ErrorJsonHandler.NULL_POINTER_EXCEPTION, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity readInfinite(int till, int to, IdResponseDto dto) {
        try {
            if (dto != null) {
                if (dto.getId() != null) {
                    List<PodcastListDto> categoryPodcasts = podcastRepository.listPodcastsEachCategory(dto.getId(), PageRequest.of(till, to, Sort.by(Sort.Direction.DESC, "createdDate")));
                    return ResponseEntity.ok(categoryPodcasts);
                } else {
                    return ResponseEntity.ok(ErrorJsonHandler.EMPTY_ID_FIELD);
                }

            } else {
                return new ResponseEntity(ErrorJsonHandler.EMPTY_BODY, HttpStatus.BAD_REQUEST);
            }
        } catch (NullPointerException e) {
            return new ResponseEntity(ErrorJsonHandler.NULL_POINTER_EXCEPTION, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity categoryInfo(IdResponseDto dto) {
        try {
            if (dto != null) {
                CategoryInfoDto cid = new CategoryInfoDto();
                Category category = categoryRepository.getOne(dto.getId());
                if (category != null) {
                    cid.setCategoryInfo(category);
                } else {
                    return new ResponseEntity(ErrorJsonHandler.NULL_POINTER_EXCEPTION, HttpStatus.INTERNAL_SERVER_ERROR);
                }
                String[] orderby = {"viewCount", "likeCount"};
                List<PodcastListDto> podcasts = podcastRepository.listTopPodcastsEachCategory(category.getId(), PageRequest.of(0, 3, Sort.by(Sort.Direction.DESC, orderby)));
                cid.setPodcasts(podcasts);

                return ResponseEntity.ok(cid);
            } else {
                return new ResponseEntity(ErrorJsonHandler.EMPTY_BODY, HttpStatus.BAD_REQUEST);
            }
        } catch (NullPointerException e) {
            return new ResponseEntity(ErrorJsonHandler.NULL_POINTER_EXCEPTION, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private List<CategoryDto> findChildrenOf(CategoryDto categoryDto, List<Category> allCategories) {
        List<CategoryDto> categories = new ArrayList<>();
        for (Category cat : allCategories) {
            boolean eq;
            if (cat.getParent() == null) {
                eq = categoryDto == null || categoryDto.id == null;
            } else {
                eq = categoryDto != null && Objects.equals(cat.getParent().getId(), categoryDto.id);
            }
            if (eq) {
                CategoryDto child = new CategoryDto();
                child.name = cat.getName();
                child.id = cat.getId();
                child.createdDate = cat.getCreatedDate();
                child.updateDate = cat.getUpdateDate();
                child.imageAddress = cat.getImageAddress();

                categories.add(child);
            }
        }
        return categories;
    }
}
