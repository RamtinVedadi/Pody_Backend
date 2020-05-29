package com.pody.service.managers.impl;

import com.pody.dto.requests.TwoIDRequestDto;
import com.pody.model.CategoryFollow;
import com.pody.model.HashtagFollow;
import com.pody.model.UserFollow;
import com.pody.repository.CategoryFollowRepository;
import com.pody.repository.HashtagFollowRepository;
import com.pody.repository.UserFollowRepository;
import com.pody.repository.UserRepository;
import com.pody.service.ErrorJsonHandler;
import com.pody.service.managers.FollowManager;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional
public class FollowManagerImpl implements FollowManager {
    private CategoryFollowRepository categoryFollowRepository;
    private HashtagFollowRepository hashtagFollowRepository;
    private UserFollowRepository userFollowRepository;
    private UserRepository userRepository;
    private ModelMapper modelMapper;

    @Autowired
    public FollowManagerImpl(CategoryFollowRepository categoryFollowRepository, HashtagFollowRepository hashtagFollowRepository, UserFollowRepository userFollowRepository, UserRepository userRepository, ModelMapper modelMapper) {
        this.categoryFollowRepository = categoryFollowRepository;
        this.hashtagFollowRepository = hashtagFollowRepository;
        this.userFollowRepository = userFollowRepository;
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
    }

    @Override //Tested
    public ResponseEntity createUserFollow(UserFollow userFollow) {
        try {
            if (userFollow != null) {
                userFollow.setId(null);
                userRepository.updateFollowerCount(userFollow.getFollower().getId());
                userRepository.updateFollowingCount(userFollow.getUser().getId());
                UserFollow result = userFollowRepository.save(userFollow);
                if (result != null) {
                    return ResponseEntity.ok(ErrorJsonHandler.SUCCESSFUL);
                } else {
                    return ResponseEntity.ok("NOT SUCCESSFUL");
                }
            } else {
                return new ResponseEntity(ErrorJsonHandler.EMPTY_BODY, HttpStatus.BAD_REQUEST);
            }
        } catch (NullPointerException e) {
            return new ResponseEntity(ErrorJsonHandler.NULL_POINTER_EXCEPTION, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override //Tested
    public ResponseEntity deleteUserFollow(TwoIDRequestDto dto) {
        //first id is for user id which want to unfollow
        //second id is for channels id
        try {
            if (dto.getFirstID() != null && dto.getSecondID() != null) {
                userRepository.updateUnFollowerCount(dto.getSecondID());
                userRepository.updateUnFollowingCount(dto.getFirstID());
                int result = userFollowRepository.deleteFollowership(dto.getFirstID(), dto.getSecondID());

                if (result == 1) {
                    return ResponseEntity.ok(ErrorJsonHandler.SUCCESSFUL);
                } else {
                    return ResponseEntity.ok("NOT SUCCESSFUL");
                }
            } else {
                return new ResponseEntity(ErrorJsonHandler.EMPTY_BODY, HttpStatus.BAD_REQUEST);
            }
        } catch (NullPointerException e) {
            return new ResponseEntity(ErrorJsonHandler.NULL_POINTER_EXCEPTION, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override //Tested
    public ResponseEntity createCategoryFollow(CategoryFollow categoryFollow) {
        try {
            if (categoryFollow != null) {
                categoryFollow.setId(null);
                CategoryFollow result = categoryFollowRepository.save(categoryFollow);
                if (result != null) {
                    return ResponseEntity.ok(ErrorJsonHandler.SUCCESSFUL);
                } else {
                    return ResponseEntity.ok("NOT SUCCESSFUL");
                }
            } else {
                return new ResponseEntity(ErrorJsonHandler.EMPTY_BODY, HttpStatus.BAD_REQUEST);
            }
        } catch (NullPointerException e) {
            return new ResponseEntity(ErrorJsonHandler.NULL_POINTER_EXCEPTION, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity deleteCategoryFollow(TwoIDRequestDto dto) {
        try {
            if (dto.getFirstID() != null && dto.getSecondID() != null) {
                int result = categoryFollowRepository.deleteFollowership(dto.getFirstID(), dto.getSecondID());

                if (result == 1) {
                    return ResponseEntity.ok(ErrorJsonHandler.SUCCESSFUL);
                } else {
                    return ResponseEntity.ok("NOT SUCCESSFUL");
                }
            } else {
                return new ResponseEntity(ErrorJsonHandler.EMPTY_BODY, HttpStatus.BAD_REQUEST);
            }
        } catch (NullPointerException e) {
            return new ResponseEntity(ErrorJsonHandler.NULL_POINTER_EXCEPTION, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity createHashtagFollow(HashtagFollow hashtagFollow) {
        try {
            if (hashtagFollow != null) {
                hashtagFollow.setId(null);
                HashtagFollow result = hashtagFollowRepository.save(hashtagFollow);
                if (result != null) {
                    return ResponseEntity.ok(ErrorJsonHandler.SUCCESSFUL);
                } else {
                    return ResponseEntity.ok("NOT SUCCESSFUL");
                }
            } else {
                return new ResponseEntity(ErrorJsonHandler.EMPTY_BODY, HttpStatus.BAD_REQUEST);
            }
        } catch (NullPointerException e) {
            return new ResponseEntity(ErrorJsonHandler.NULL_POINTER_EXCEPTION, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity deleteHashtagFollow(TwoIDRequestDto dto) {
        try {
            if (dto.getFirstID() != null && dto.getSecondID() != null) {
                int result = hashtagFollowRepository.deleteFollowership(dto.getFirstID(), dto.getSecondID());
                if (result == 1) {
                    return ResponseEntity.ok(ErrorJsonHandler.SUCCESSFUL);
                } else {
                    return ResponseEntity.ok("NOT SUCCESSFUL");
                }
            } else {
                return new ResponseEntity(ErrorJsonHandler.EMPTY_BODY, HttpStatus.BAD_REQUEST);
            }
        } catch (NullPointerException e) {
            return new ResponseEntity(ErrorJsonHandler.NULL_POINTER_EXCEPTION, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity listFollowings(UUID userId) {
        try {
            if (userId != null) {
                //TODO
                return ResponseEntity.ok("");
            } else {
                return new ResponseEntity(ErrorJsonHandler.EMPTY_BODY, HttpStatus.BAD_REQUEST);
            }
        } catch (NullPointerException e) {
            return new ResponseEntity(ErrorJsonHandler.NULL_POINTER_EXCEPTION, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity listFollowers(UUID userId) {
        try {
            if (userId != null) {
                //TODO
                return ResponseEntity.ok("");
            } else {
                return new ResponseEntity(ErrorJsonHandler.EMPTY_BODY, HttpStatus.BAD_REQUEST);
            }
        } catch (NullPointerException e) {
            return new ResponseEntity(ErrorJsonHandler.NULL_POINTER_EXCEPTION, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
