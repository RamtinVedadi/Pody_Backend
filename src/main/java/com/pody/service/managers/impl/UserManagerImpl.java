package com.pody.service.managers.impl;

import com.pody.dto.repositories.ChannelsListDto;
import com.pody.dto.repositories.NewsListDto;
import com.pody.dto.repositories.PodcastListDto;
import com.pody.dto.requests.IdStringRequestDto;
import com.pody.dto.requests.StringRequestDto;
import com.pody.dto.requests.TwoIDRequestDto;
import com.pody.dto.responses.IdResponseDto;
import com.pody.dto.responses.SubscriptionsDto;
import com.pody.dto.responses.UserReadResultDto;
import com.pody.model.User;
import com.pody.model.UserFollow;
import com.pody.repository.NewsRepository;
import com.pody.repository.PodcastRepository;
import com.pody.repository.UserFollowRepository;
import com.pody.repository.UserRepository;
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
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class UserManagerImpl implements UserManager {
    private UserFollowRepository userFollowRepository;
    private AltUserRepository altUserRepository;
    private PodcastRepository podcastRepository;
    private NewsRepository newsRepository;
    private UserRepository userRepository;
    private ModelMapper modelMapper;

    @Autowired
    public UserManagerImpl(UserFollowRepository userFollowRepository, AltUserRepository altUserRepository, PodcastRepository podcastRepository, NewsRepository newsRepository, UserRepository userRepository, ModelMapper modelMapper) {
        this.userFollowRepository = userFollowRepository;
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

                List<PodcastListDto> userPodcasts = podcastRepository.listPodcastEachUser(dto.getFirstID(), PageRequest.of(0, 24, Sort.by(Sort.Direction.DESC, "createdDate")));
                urrd.setUserPodcasts(userPodcasts);

                List<NewsListDto> userNews = newsRepository.listNewsEachUser(dto.getFirstID(), PageRequest.of(0, 24, Sort.by(Sort.Direction.DESC, "createdDate")));
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
                    User result = userRepository.save(dbUser);
                    if (result != null) {
                        return ResponseEntity.ok(result);
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

                int result = userRepository.resetPassword(hashedPassword, dto.getId());
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

    @Override //First Part Tested
    public ResponseEntity listChannels(IdResponseDto dto) {
        try {
            if (dto.getId() == null) {
                List<ChannelsListDto> users = userRepository.listHomePageUsers(PageRequest.of(0, 15, Sort.by(Sort.Direction.DESC, "followCount")));
                return ResponseEntity.ok(users);
            } else {


                return ResponseEntity.ok("");
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
                List<ChannelsListDto> listFollowings = userRepository.listFollowingChannels(id, PageRequest.of(0, 15));
                List<SubscriptionsDto> finalList = new ArrayList<>();

                for (ChannelsListDto cld : listFollowings) {
                    SubscriptionsDto sd = new SubscriptionsDto();
                    sd.setChannelInfo(cld);
                    List<PodcastListDto> channelsPodcast = podcastRepository.listPodcastEachUser(cld.getId(), PageRequest.of(0, 4, Sort.by(Sort.Direction.DESC, "createdDate")));
                    sd.setChannelPodcasts(channelsPodcast);

                    finalList.add(sd);
                }

                return ResponseEntity.ok(finalList);
            } else {
                return new ResponseEntity(ErrorJsonHandler.EMPTY_ID_FIELD, HttpStatus.BAD_REQUEST);
            }
        } catch (NullPointerException e) {
            return new ResponseEntity(ErrorJsonHandler.NULL_POINTER_EXCEPTION, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
