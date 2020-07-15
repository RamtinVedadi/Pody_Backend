package com.pody.service.managers.impl;

import com.pody.dto.repositories.BlogListDto;
import com.pody.dto.repositories.BlogReadDto;
import com.pody.dto.repositories.PodcastListDto;
import com.pody.dto.requests.BlogCreateDto;
import com.pody.dto.requests.TwoIDRequestDto;
import com.pody.dto.responses.BlogReadResponseDto;
import com.pody.dto.responses.IdResponseDto;
import com.pody.model.*;
import com.pody.repository.*;
import com.pody.service.ErrorJsonHandler;
import com.pody.service.managers.BlogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class BlogManagerImpl implements BlogManager {
    private BlogCategoryRepository blogCategoryRepository;
    private BlogHistoryRepository blogHistoryRepository;
    private UserFollowRepository userFollowRepository;
    private ReadLaterRepository readLaterRepository;
    private BlogLikeRepository blogLikeRepository;
    private PodcastRepository podcastRepository;
    private BlogRepository blogRepository;
    private UserRepository userRepository;

    @Autowired
    public BlogManagerImpl(BlogCategoryRepository blogCategoryRepository, BlogHistoryRepository blogHistoryRepository, UserFollowRepository userFollowRepository, ReadLaterRepository readLaterRepository, BlogLikeRepository blogLikeRepository, PodcastRepository podcastRepository, BlogRepository blogRepository, UserRepository userRepository) {
        this.blogCategoryRepository = blogCategoryRepository;
        this.blogHistoryRepository = blogHistoryRepository;
        this.userFollowRepository = userFollowRepository;
        this.readLaterRepository = readLaterRepository;
        this.blogLikeRepository = blogLikeRepository;
        this.podcastRepository = podcastRepository;
        this.blogRepository = blogRepository;
        this.userRepository = userRepository;
    }

    @Override //Tested
    public ResponseEntity create(BlogCreateDto dto) {
        try {
            if (dto != null) {
                if (dto.getBlog() != null) {
                    if (dto.getBlogCategory() != null) {
                        dto.getBlog().setId(null);
                        dto.getBlog().setCreatedDate(new Date());
                        dto.getBlog().setUpdateDate(new Date());
                        dto.getBlog().setImageAddress("http://pody.ir/defaultImages/default.jpg");
                        dto.getBlog().setIsPublish(false);
                        if (dto.getBlog().getUser().getId() == null) {
                            return new ResponseEntity(ErrorJsonHandler.EMPTY_ID_FIELD, HttpStatus.BAD_REQUEST);
                        }
                        Blog blogResult = blogRepository.save(dto.getBlog());
                        if (blogResult != null) {
                            dto.getBlogCategory().setBlog(blogResult);
                            if (dto.getBlogCategory().getSubCategory().getId() == null) {
                                dto.getBlogCategory().setSubCategory(null);
                            }
                        }
                        BlogCategory blogCategoryResult = blogCategoryRepository.save(dto.getBlogCategory());
                        if (blogCategoryResult != null) {
                            String json = "{" +
                                    "\"message\":\"SUCCESSFUL\"," +
                                    "\"blogId\":\"" + blogCategoryResult.getId().toString() + "\"" +
                                    "}";
                            return ResponseEntity.ok(ErrorJsonHandler.SUCCESSFUL);
                        } else {
                            return ResponseEntity.ok(ErrorJsonHandler.NOT_SUCCESSFUL);
                        }
                    } else {
                        return new ResponseEntity(ErrorJsonHandler.EMPTY_BODY, HttpStatus.BAD_REQUEST);
                    }
                } else {
                    return new ResponseEntity(ErrorJsonHandler.EMPTY_BODY, HttpStatus.BAD_REQUEST);
                }
            } else {
                return new ResponseEntity(ErrorJsonHandler.EMPTY_BODY, HttpStatus.BAD_REQUEST);
            }
        } catch (NullPointerException e) {
            return new ResponseEntity(ErrorJsonHandler.NULL_POINTER_EXCEPTION, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity read(TwoIDRequestDto dto) {
        try {
            //first id is for blog id
            //second id is for user logined in
            if (dto != null) {
                BlogReadResponseDto brrd = new BlogReadResponseDto();
                BlogReadDto blogInfo = blogRepository.readBlog(dto.getFirstID());
                brrd.setBlogInfo(blogInfo);

                if (dto.getSecondID() != null) {
                    UserFollow isAvailable = userFollowRepository.isUserFollowAvailable(dto.getSecondID(), blogInfo.getUserId());
                    if (isAvailable == null) {
                        brrd.setUserFollow(false);
                    } else {
                        brrd.setUserFollow(true);
                    }
                } else {
                    brrd.setUserFollow(false);
                }
                List<PodcastListDto> lastPodcastOfUser = podcastRepository.listPodcastEachUser(blogInfo.getUserId(), PageRequest.of(0, 1, Sort.by(Sort.Direction.DESC, "createdDate")));
                if (lastPodcastOfUser.size() > 0) {
                    brrd.setPodcast(lastPodcastOfUser);
                } else {
                    String orderBy[] = {"createdDate", "viewCount", "likeCount"};
                    List<PodcastListDto> lastRandomPodcast = podcastRepository.listMostViewedAndLiked(PageRequest.of(0, 1, Sort.by(Sort.Direction.DESC, orderBy)));
                    brrd.setPodcast(lastRandomPodcast);
                }

                List<BlogListDto> sameCategory = blogRepository.blogSameCategory(blogInfo.getCategoryId(), blogInfo.getId(), PageRequest.of(0, 5, Sort.by(Sort.Direction.DESC, "createdDate")));
                if (sameCategory != null && sameCategory.size() > 0) {
                    brrd.setSuggestionBlogs(sameCategory);
                } else {
                    List<BlogListDto> notSameCategory = blogRepository.listBlogsWithoutItSelf(blogInfo.getId(), PageRequest.of(0, 5, Sort.by(Sort.Direction.DESC, "createdDate")));
                    brrd.setSuggestionBlogs(notSameCategory);
                }

                if (dto.getSecondID() == null) {
                    brrd.setIsBookmark(false);
                } else {
                    List<ReadLater> result = readLaterRepository.checkIsBookmark(dto.getSecondID(), dto.getFirstID());
                    if (result != null && result.size() > 0) {
                        brrd.setIsBookmark(true);
                    } else {
                        brrd.setIsBookmark(false);
                    }
                }

                if (dto.getSecondID() == null) {
                    brrd.setIsLike(false);
                } else {
                    List<BlogLike> result = blogLikeRepository.checkIsLike(dto.getSecondID(), dto.getFirstID());
                    if (result != null && result.size() > 0) {
                        brrd.setIsLike(true);
                    } else {
                        brrd.setIsLike(false);
                    }
                }

                return ResponseEntity.ok(brrd);
            } else {
                return new ResponseEntity(ErrorJsonHandler.EMPTY_BODY, HttpStatus.BAD_REQUEST);
            }
        } catch (NullPointerException e) {
            return new ResponseEntity(ErrorJsonHandler.NULL_POINTER_EXCEPTION, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity update(Blog blog, UUID id) {
        return null;
    }

    @Override
    public ResponseEntity uploadImage(MultipartFile image, UUID blogId) {
        try {
            if (blogId != null) {
                if (image != null) {
                    String filePath = "";
                    try {
                        Path copyLocation = Paths
                                .get("C:/xampp/htdocs/blogImages" + File.separator + StringUtils.cleanPath(blogId.toString() + ".jpg"));
                        Files.copy(image.getInputStream(), copyLocation, StandardCopyOption.REPLACE_EXISTING);
                        filePath = "http://loacalhost/blogImages/" + StringUtils.cleanPath(blogId.toString() + ".jpg");
                    } catch (IOException e) {
                        return new ResponseEntity(ErrorJsonHandler.IO_EXCEPTION, HttpStatus.INTERNAL_SERVER_ERROR);
                    }
                    int resultImage = blogRepository.updateImageAddress(filePath, blogId);

                    if (resultImage == 1) {
                        return ResponseEntity.ok(ErrorJsonHandler.SUCCESSFUL);
                    } else {
                        return new ResponseEntity(ErrorJsonHandler.NULL_POINTER_EXCEPTION, HttpStatus.INTERNAL_SERVER_ERROR);
                    }
                } else {
                    return new ResponseEntity(ErrorJsonHandler.EMPTY_FILE, HttpStatus.BAD_REQUEST);
                }
            } else {
                return new ResponseEntity(ErrorJsonHandler.EMPTY_ID_FIELD, HttpStatus.BAD_REQUEST);
            }
        } catch (NullPointerException e) {
            return new ResponseEntity(ErrorJsonHandler.NULL_POINTER_EXCEPTION, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity updateIsPublish(UUID blogId) {
        try {
            if (blogId != null) {
                int result = blogRepository.updateIsPublish(blogId);
                if (result == 1) {
                    return ResponseEntity.ok(ErrorJsonHandler.SUCCESSFUL);
                } else {
                    return ResponseEntity.ok(ErrorJsonHandler.NOT_SUCCESSFUL);
                }
            } else {
                return new ResponseEntity(ErrorJsonHandler.EMPTY_ID_FIELD, HttpStatus.BAD_REQUEST);
            }
        } catch (NullPointerException e) {
            return new ResponseEntity(ErrorJsonHandler.NULL_POINTER_EXCEPTION, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity blogList(IdResponseDto dto, int page, int size) {
        try {
            //first id is for blog id
            //second id is for user logined in
            if (dto != null) {
                List<BlogListDto> list = blogRepository.listBlogs(PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdDate")));
                return ResponseEntity.ok(list);
            } else {
                return new ResponseEntity(ErrorJsonHandler.EMPTY_BODY, HttpStatus.BAD_REQUEST);
            }
        } catch (NullPointerException e) {
            return new ResponseEntity(ErrorJsonHandler.NULL_POINTER_EXCEPTION, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity likeUpdate(TwoIDRequestDto dto) {
        try {
            //first id is for blog id
            //second id is for user logined in
            if (dto != null) {
                int likeResult = blogRepository.updateLikeCount(dto.getFirstID());
                if (likeResult == 1) {
                    User userLookup = userRepository.findOneById(dto.getSecondID());
                    if (userLookup != null) {
                        BlogLike bl = new BlogLike();
                        bl.setBlog(new Blog(dto.getFirstID()));
                        bl.setUser(userLookup);
                        bl.setCreatedDate(new Date());
                        BlogLike blogLikeResult = blogLikeRepository.save(bl);
                        if (blogLikeResult != null) {
                            return ResponseEntity.ok(ErrorJsonHandler.SUCCESSFUL);
                        } else {
                            return ResponseEntity.ok(ErrorJsonHandler.NOT_SUCCESSFUL);
                        }
                    } else {
                        return new ResponseEntity(ErrorJsonHandler.NOT_SUCCESSFUL, HttpStatus.BAD_REQUEST);
                    }
                } else {
                    return ResponseEntity.ok(ErrorJsonHandler.NOT_SUCCESSFUL);
                }
            } else {
                return new ResponseEntity(ErrorJsonHandler.EMPTY_BODY, HttpStatus.BAD_REQUEST);
            }
        } catch (NullPointerException e) {
            return new ResponseEntity(ErrorJsonHandler.NULL_POINTER_EXCEPTION, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity disLikeUpdate(TwoIDRequestDto dto) {
        try {
            //first id is for blog id
            //second id is for user logined in
            if (dto != null) {
                int likeResult = blogRepository.updateDisLikeCount(dto.getFirstID());
                if (likeResult == 1) {
                    int deleteResult = blogLikeRepository.deleteBlogLike(dto.getFirstID(), dto.getSecondID());
                    if (deleteResult == 1) {
                        return ResponseEntity.ok(ErrorJsonHandler.SUCCESSFUL);
                    } else {
                        return ResponseEntity.ok(ErrorJsonHandler.NOT_SUCCESSFUL);
                    }
                } else {
                    return ResponseEntity.ok(ErrorJsonHandler.NOT_SUCCESSFUL);
                }
            } else {
                return new ResponseEntity(ErrorJsonHandler.EMPTY_BODY, HttpStatus.BAD_REQUEST);
            }
        } catch (NullPointerException e) {
            return new ResponseEntity(ErrorJsonHandler.NULL_POINTER_EXCEPTION, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity viewUpdate(TwoIDRequestDto dto) {
        return null;
    }

    @Override
    public ResponseEntity listLikedBlogEachUser(UUID userId) {
        return null;
    }

    @Override
    public ResponseEntity makeBlogBookmark(TwoIDRequestDto dto) {
        try {
            //first id is for blog id
            //second id is for user logined in
            if (dto != null) {
                ReadLater rl = new ReadLater();
                rl.setBlog(new Blog(dto.getFirstID()));
                rl.setUser(new User(dto.getSecondID()));
                rl.setCreatedDate(new Date());
                ReadLater save = readLaterRepository.save(rl);
                if (save != null) {
                    return ResponseEntity.ok(ErrorJsonHandler.SUCCESSFUL);
                } else {
                    return ResponseEntity.ok(ErrorJsonHandler.NOT_SUCCESSFUL);
                }
            } else {
                return new ResponseEntity(ErrorJsonHandler.EMPTY_BODY, HttpStatus.BAD_REQUEST);
            }
        } catch (NullPointerException e) {
            return new ResponseEntity(ErrorJsonHandler.NULL_POINTER_EXCEPTION, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity makeBlogUnBookmark(TwoIDRequestDto dto) {
        try {
            //first id is for blog id
            //second id is for user logined in
            if (dto != null) {
                int result = readLaterRepository.deleteListenLater(dto.getSecondID(), dto.getFirstID());
                if (result == 1) {
                    return ResponseEntity.ok(ErrorJsonHandler.SUCCESSFUL);
                } else {
                    return ResponseEntity.ok(ErrorJsonHandler.NOT_SUCCESSFUL);
                }
            } else {
                return new ResponseEntity(ErrorJsonHandler.EMPTY_BODY, HttpStatus.BAD_REQUEST);
            }
        } catch (NullPointerException e) {
            return new ResponseEntity(ErrorJsonHandler.NULL_POINTER_EXCEPTION, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity blogBookmarkCheck(TwoIDRequestDto dto) {
        try {
            //first id is for blog id
            //second id is for user logined in
            if (dto != null) {
                List<ReadLater> result = readLaterRepository.checkIsBookmark(dto.getSecondID(), dto.getFirstID());
                if (result != null && result.size() > 0) {
                    return ResponseEntity.ok(ErrorJsonHandler.TRUE);
                } else {
                    return ResponseEntity.ok(ErrorJsonHandler.FALSE);
                }
            } else {
                return new ResponseEntity(ErrorJsonHandler.EMPTY_BODY, HttpStatus.BAD_REQUEST);
            }
        } catch (NullPointerException e) {
            return new ResponseEntity(ErrorJsonHandler.NULL_POINTER_EXCEPTION, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
