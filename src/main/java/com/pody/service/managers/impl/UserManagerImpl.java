package com.pody.service.managers.impl;

import com.pody.dto.repositories.*;
import com.pody.dto.repositories.PodcastListDto;
import com.pody.dto.requests.IdStringRequestDto;
import com.pody.dto.requests.StringRequestDto;
import com.pody.dto.requests.TwoIDRequestDto;
import com.pody.dto.responses.*;
import com.pody.model.User;
import com.pody.model.UserFollow;
import com.pody.repository.*;
import com.pody.repository.alt.AltUserRepository;
import com.pody.service.ErrorJsonHandler;
import com.pody.service.PasswordEncoding;
import com.pody.service.managers.UserManager;
import org.apache.commons.codec.binary.Hex;
import org.modelmapper.ModelMapper;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class UserManagerImpl implements UserManager {
    private PodcastViewRepository podcastViewRepository;
    private UserFollowRepository userFollowRepository;
    private CategoryRepository categoryRepository;
    private AltUserRepository altUserRepository;
    private PodcastRepository podcastRepository;
    private NewsRepository newsRepository;
    private UserRepository userRepository;
    private ModelMapper modelMapper;

    @Autowired
    public UserManagerImpl(PodcastViewRepository podcastViewRepository, UserFollowRepository userFollowRepository, CategoryRepository categoryRepository,
                           AltUserRepository altUserRepository, PodcastRepository podcastRepository,
                           NewsRepository newsRepository, UserRepository userRepository, ModelMapper modelMapper) {
        this.podcastViewRepository = podcastViewRepository;
        this.userFollowRepository = userFollowRepository;
        this.categoryRepository = categoryRepository;
        this.altUserRepository = altUserRepository;
        this.podcastRepository = podcastRepository;
        this.newsRepository = newsRepository;
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public ResponseEntity read(TwoIDRequestDto dto) {
        //first id is for user which we want to see his page
        //second id is for logined user
        try {
            if (dto != null) {
                UserReadResultDto urrd = new UserReadResultDto();

                User userInfo = userRepository.findOneById(dto.getFirstID());
                urrd.setUserInfo(userInfo);

                List<PodcastListDto> userPodcasts = podcastRepository.listPodcastEachUserPublished(dto.getFirstID(), PageRequest.of(0, 20, Sort.by(Sort.Direction.DESC, "createdDate")));
                urrd.setUserPodcasts(userPodcasts);

                List<NewsListDto> userNews = newsRepository.listNewsEachUser(dto.getFirstID(), PageRequest.of(0, 20, Sort.by(Sort.Direction.DESC, "createdDate")));
                urrd.setUserNews(userNews);

                if (dto.getSecondID() == null) {
                    urrd.setIsFollow(false);
                } else {
                    UserFollow isFollow = userFollowRepository.isUserFollowAvailable(dto.getSecondID(), dto.getFirstID());
                    if (isFollow != null) {
                        urrd.setIsFollow(true);
                    } else {
                        urrd.setIsFollow(false);
                    }
                }

                return ResponseEntity.ok(urrd);
            } else {
                return new ResponseEntity(ErrorJsonHandler.EMPTY_ID_FIELD, HttpStatus.BAD_REQUEST);
            }
        } catch (NullPointerException e) {
            return new ResponseEntity(ErrorJsonHandler.NULL_POINTER_EXCEPTION, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity update(User user, UUID id) {
        try {
            if (id != null) {
                if (user != null) {
                    User dbUser = userRepository.findOneById(id);
                    modelMapper.map(user, dbUser);
                    dbUser.setUpdateDate(new Date());
                    User result = userRepository.save(dbUser);
                    if (result != null) {
                        return ResponseEntity.ok(ErrorJsonHandler.SUCCESSFUL);
                    } else {
                        return new ResponseEntity(ErrorJsonHandler.NULL_POINTER_EXCEPTION, HttpStatus.INTERNAL_SERVER_ERROR);
                    }
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

    @Override
    public ResponseEntity delete(UUID id) {
        try {
            if (id != null) {
                userRepository.deleteById(id);
                return ResponseEntity.ok(ErrorJsonHandler.SUCCESSFUL);
            } else {
                return new ResponseEntity(ErrorJsonHandler.EMPTY_ID_FIELD, HttpStatus.BAD_REQUEST);
            }
        } catch (NullPointerException e) {
            return new ResponseEntity(ErrorJsonHandler.NULL_POINTER_EXCEPTION, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity isPremium(UUID id) {
        try {
            if (id != null) {
                int result = userRepository.isPremium(id);
                if (result == 1) {
                    return ResponseEntity.ok(ErrorJsonHandler.SUCCESSFUL);
                } else {
                    return new ResponseEntity(ErrorJsonHandler.NOT_SUCCESSFUL, HttpStatus.CONFLICT);
                }
            } else {
                return new ResponseEntity(ErrorJsonHandler.EMPTY_ID_FIELD, HttpStatus.BAD_REQUEST);
            }
        } catch (NullPointerException e) {
            return new ResponseEntity(ErrorJsonHandler.NULL_POINTER_EXCEPTION, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override //Tested
    public ResponseEntity uploadProfileImage(MultipartFile image, UUID id) {
        if (image == null || image.isEmpty()) {
            return new ResponseEntity(ErrorJsonHandler.NO_FILE_SELECTED, HttpStatus.BAD_REQUEST);
        } else {
            String filePath = "";

            try {
                Path copyLocation = Paths
                        .get("C:/xampp/htdocs/profileImages" + File.separator + StringUtils.cleanPath(id.toString() + ".jpg"));
                Files.copy(image.getInputStream(), copyLocation, StandardCopyOption.REPLACE_EXISTING);
                filePath = "http://localhost/profileImages" + File.separator + StringUtils.cleanPath(id.toString() + ".jpg");
            } catch (IOException e) {
                return new ResponseEntity(ErrorJsonHandler.IO_EXCEPTION, HttpStatus.INTERNAL_SERVER_ERROR);
            }
            int result = userRepository.updateImageAddress(filePath, id);
            if (result == 1) {
                return ResponseEntity.ok(ErrorJsonHandler.SUCCESSFUL);
            } else {
                return new ResponseEntity(ErrorJsonHandler.NULL_POINTER_EXCEPTION, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
    }

    @Override
    public ResponseEntity uploadChannelImage(MultipartFile image, UUID id) {
        if (image == null || image.isEmpty()) {
            return new ResponseEntity(ErrorJsonHandler.NO_FILE_SELECTED, HttpStatus.BAD_REQUEST);
        } else {
            String filePath = "";

            try {
                Path copyLocation = Paths
                        .get("C:/xampp/htdocs/channelImage" + File.separator + StringUtils.cleanPath(id.toString() + ".jpg"));
                Files.copy(image.getInputStream(), copyLocation, StandardCopyOption.REPLACE_EXISTING);
                filePath = "http://localhost/channelImage" + File.separator + StringUtils.cleanPath(id.toString() + ".jpg");
            } catch (IOException e) {
                return new ResponseEntity(ErrorJsonHandler.IO_EXCEPTION, HttpStatus.INTERNAL_SERVER_ERROR);
            }
            int result = userRepository.updateImageAddress(filePath, id);
            if (result == 1) {
                return ResponseEntity.ok(ErrorJsonHandler.SUCCESSFUL);
            } else {
                return new ResponseEntity(ErrorJsonHandler.NULL_POINTER_EXCEPTION, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
    }

    @Override
    public ResponseEntity checkEmail(StringRequestDto dto) {
        try {
            if (dto != null) {
                User result = userRepository.findOneByEmail(dto.getStringValue());
                if (result != null) {
                    return ResponseEntity.ok(ErrorJsonHandler.DUPLICATE_EMAIL);
                } else {
                    return ResponseEntity.ok(ErrorJsonHandler.OK_EMAIL);
                }
            } else {
                return new ResponseEntity(ErrorJsonHandler.EMPTY_ID_FIELD, HttpStatus.BAD_REQUEST);
            }
        } catch (NullPointerException e) {
            return new ResponseEntity(ErrorJsonHandler.NULL_POINTER_EXCEPTION, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity checkUsername(StringRequestDto dto) {
        try {
            if (dto != null) {
                User result = userRepository.findOneByUsername(dto.getStringValue());
                if (result != null) {
                    return ResponseEntity.ok(ErrorJsonHandler.DUPLICATE_USERNAME);
                } else {
                    return ResponseEntity.ok(ErrorJsonHandler.OK_USERNAME);
                }
            } else {
                return new ResponseEntity(ErrorJsonHandler.EMPTY_ID_FIELD, HttpStatus.BAD_REQUEST);
            }
        } catch (NullPointerException e) {
            return new ResponseEntity(ErrorJsonHandler.NULL_POINTER_EXCEPTION, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override //Tested
    public ResponseEntity resetPassword(IdStringRequestDto dto) {
        try {
            if (dto != null) {
                PasswordEncoding encoding = new PasswordEncoding();
                String salt = "Pody123456789";

                int iterations = 10000;
                int keyLength = 512;
                char[] passwordChars = dto.getStringValue().toCharArray();
                byte[] saltBytes = salt.getBytes();

                byte[] hashedBytes = encoding.hashPassword(passwordChars, saltBytes, iterations, keyLength);
                String hashedPassword = Hex.encodeHexString(hashedBytes);

                int result = userRepository.resetPassword(hashedPassword, new Date(), dto.getId());
                if (result == 1) {
                    return ResponseEntity.ok(ErrorJsonHandler.SUCCESSFUL);
                } else {
                    return new ResponseEntity(ErrorJsonHandler.NOT_SUCCESSFUL, HttpStatus.BAD_REQUEST);
                }
            } else {
                return new ResponseEntity(ErrorJsonHandler.EMPTY_BODY, HttpStatus.BAD_REQUEST);
            }
        } catch (NullPointerException e) {
            return new ResponseEntity(ErrorJsonHandler.NULL_POINTER_EXCEPTION, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity findUserForResetPassword(StringRequestDto dto) {
        try {
            if (dto != null) {
                User user = userRepository.findUserForResetPassword(dto.getStringValue());
                if (user != null) {
                    IdResponseDto response = new IdResponseDto();
                    response.setId(user.getId());
                    return ResponseEntity.ok(response);
                } else {
                    return new ResponseEntity(ErrorJsonHandler.NOT_SUCCESSFUL, HttpStatus.BAD_REQUEST);
                }
            } else {
                return new ResponseEntity(ErrorJsonHandler.EMPTY_BODY, HttpStatus.BAD_REQUEST);
            }
        } catch (NullPointerException e) {
            return new ResponseEntity(ErrorJsonHandler.NULL_POINTER_EXCEPTION, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity listChannels(IdResponseDto dto, int till, int to) {
        try {
            //till is 0 and to is 20
            //id is logined user id
            if (dto.getId() == null) {
                List<ChannelsPageDto> users = userRepository.listChannels(PageRequest.of(till, to, Sort.by(Sort.Direction.DESC, "followCount")));
                List<ChannelsDto> channelsList = new ArrayList<>();
                for (ChannelsPageDto cpd : users) {
                    ChannelsDto cd = new ChannelsDto();
                    cd.setChannelInfo(cpd);
                    cd.setIsFollow(false);

                    Integer episodeCount = podcastRepository.channelEpisodeCount(cpd.getId());
                    if (episodeCount != null) {
                        cd.setEpisodeCount(episodeCount);
                    } else {
                        cd.setEpisodeCount(0);
                    }
                    Integer listenCount = podcastViewRepository.channelListenCount(cpd.getId());
                    if (listenCount != null) {
                        cd.setListenCount(listenCount);
                    } else {
                        cd.setListenCount(0);
                    }

                    channelsList.add(cd);
                }
                return ResponseEntity.ok(channelsList);
            } else {
                List<ChannelsPageDto> users = userRepository.listChannelsLoginedUser(dto.getId(), PageRequest.of(till, to, Sort.by(Sort.Direction.DESC, "followerCount")));
                List<ChannelsDto> channelsList = new ArrayList<>();
                for (ChannelsPageDto cpd : users) {
                    ChannelsDto cd = new ChannelsDto();
                    cd.setChannelInfo(cpd);
                    //first id is for logined user
                    //second id is for channel id
                    UserFollow isAvailable = userFollowRepository.isUserFollowAvailable(dto.getId(), cpd.getId());
                    if (isAvailable != null) {
                        cd.setIsFollow(true);
                    } else {
                        cd.setIsFollow(false);
                    }

                    Integer episodeCount = podcastRepository.channelEpisodeCount(cpd.getId());
                    if (episodeCount != null) {
                        cd.setEpisodeCount(episodeCount);
                    } else {
                        cd.setEpisodeCount(0);
                    }
                    Integer listenCount = podcastViewRepository.channelListenCount(cpd.getId());
                    if (listenCount != null) {
                        cd.setListenCount(listenCount);
                    } else {
                        cd.setListenCount(0);
                    }

                    channelsList.add(cd);
                }
                return ResponseEntity.ok(channelsList);
            }
        } catch (NullPointerException e) {
            return new ResponseEntity(ErrorJsonHandler.NULL_POINTER_EXCEPTION, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity updateIsChannel(IdResponseDto dto) {
        try {
            if (dto.getId() != null) {
                int result = userRepository.updateIsChannel(dto.getId());
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
    public ResponseEntity userListSubscriptions(UUID id) {
        //This is logined user id
        try {
            if (id != null) {
                UserSubscriptionsListDto usld = new UserSubscriptionsListDto();

                List<ChannelsListDto> listFollowings = userRepository.listFollowingChannels(id, PageRequest.of(0, 15));
                List<SubscriptionsDto> finalList = new ArrayList<>();

                for (ChannelsListDto cld : listFollowings) {
                    SubscriptionsDto sd = new SubscriptionsDto();
                    sd.setChannelInfo(cld);
                    List<PodcastListDto> channelsPodcast = podcastRepository.listPodcastEachUserPublished(cld.getId(), PageRequest.of(0, 4, Sort.by(Sort.Direction.DESC, "createdDate")));
                    sd.setChannelPodcasts(channelsPodcast);

                    finalList.add(sd);
                }
                usld.setFollowingChannels(finalList);

                List<CategorySearchDto> followingCategories = categoryRepository.followingCategoryList(id);
                usld.setFollowingCategories(followingCategories);

                return ResponseEntity.ok(usld);
            } else {
                return new ResponseEntity(ErrorJsonHandler.EMPTY_ID_FIELD, HttpStatus.BAD_REQUEST);
            }
        } catch (NullPointerException e) {
            return new ResponseEntity(ErrorJsonHandler.NULL_POINTER_EXCEPTION, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity checkUserHasFollow(TwoIDRequestDto dto) {
        try {
            //first id is for channel id
            //second id is for logined user
            if (dto != null) {
                UserFollow isAvailable = userFollowRepository.isUserFollowAvailable(dto.getSecondID(), dto.getFirstID());
                if (isAvailable != null) {
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
    public ResponseEntity userListSubscriptionsSideNav(UUID id) {
        //This is logined user id
        try {
            if (id != null) {
                List<ChannelsListDto> listFollowings = userRepository.listFollowingChannelsSideNav(id);

                return ResponseEntity.ok(listFollowings);
            } else {
                return new ResponseEntity(ErrorJsonHandler.EMPTY_ID_FIELD, HttpStatus.BAD_REQUEST);
            }
        } catch (NullPointerException e) {
            return new ResponseEntity(ErrorJsonHandler.NULL_POINTER_EXCEPTION, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity uploadChannelImages(MultipartFile channelImage, MultipartFile pageImage, UUID id) {
        if (channelImage == null || channelImage.isEmpty() || pageImage.isEmpty()) {
            return new ResponseEntity(ErrorJsonHandler.NO_FILE_SELECTED, HttpStatus.BAD_REQUEST);
        } else {
            if (pageImage == null) {
                String filePath = "";

                try {
                    Path copyLocation = Paths
                            .get("C:/xampp/htdocs/profileImages" + File.separator + StringUtils.cleanPath(id.toString() + ".jpg"));
                    Files.copy(channelImage.getInputStream(), copyLocation, StandardCopyOption.REPLACE_EXISTING);
                    filePath = "http://localhost/profileImages" + File.separator + StringUtils.cleanPath(id.toString() + ".jpg");
                } catch (IOException e) {
                    return new ResponseEntity(ErrorJsonHandler.IO_EXCEPTION, HttpStatus.INTERNAL_SERVER_ERROR);
                }
                int result = userRepository.updateImageAddress(filePath, id);
                if (result == 1) {
                    return ResponseEntity.ok(ErrorJsonHandler.SUCCESSFUL);
                } else {
                    return new ResponseEntity(ErrorJsonHandler.NULL_POINTER_EXCEPTION, HttpStatus.INTERNAL_SERVER_ERROR);
                }
            } else {
                String filePath = "";

                try {
                    Path copyLocation = Paths
                            .get("C:/xampp/htdocs/profileImages" + File.separator + StringUtils.cleanPath(id.toString() + ".jpg"));
                    Files.copy(channelImage.getInputStream(), copyLocation, StandardCopyOption.REPLACE_EXISTING);
                    filePath = "http://localhost/profileImages" + File.separator + StringUtils.cleanPath(id.toString() + ".jpg");
                } catch (IOException e) {
                    return new ResponseEntity(ErrorJsonHandler.IO_EXCEPTION, HttpStatus.INTERNAL_SERVER_ERROR);
                }
                int resultImageAddress = userRepository.updateImageAddress(filePath, id);

                //channel page image
                String channelImagePath = "";

                try {
                    Path copyLocation = Paths
                            .get("C:/xampp/htdocs/channelImage" + File.separator + StringUtils.cleanPath(id.toString() + ".jpg"));
                    Files.copy(pageImage.getInputStream(), copyLocation, StandardCopyOption.REPLACE_EXISTING);
                    channelImagePath = "http://localhost/channelImage" + File.separator + StringUtils.cleanPath(id.toString() + ".jpg");
                } catch (IOException e) {
                    return new ResponseEntity(ErrorJsonHandler.IO_EXCEPTION, HttpStatus.INTERNAL_SERVER_ERROR);
                }
                int resultChannelImage = userRepository.updateChannelImage(channelImagePath, id);

                if (resultImageAddress == 1 && resultChannelImage == 1) {
                    User user = userRepository.findOneById(id);
                    return ResponseEntity.ok(user);
                } else {
                    return new ResponseEntity(ErrorJsonHandler.NULL_POINTER_EXCEPTION, HttpStatus.INTERNAL_SERVER_ERROR);
                }
            }
        }
    }
}
