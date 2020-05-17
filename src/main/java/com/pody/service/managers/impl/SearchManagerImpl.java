package com.pody.service.managers.impl;

import com.pody.dto.repositories.CategorySearchDto;
import com.pody.dto.repositories.PodcastListDto;
import com.pody.dto.repositories.UserSearchDto;
import com.pody.dto.responses.SearchResponseDto;
import com.pody.repository.CategoryRepository;
import com.pody.repository.HashtagRepository;
import com.pody.repository.PodcastRepository;
import com.pody.repository.UserRepository;
import com.pody.service.ErrorJsonHandler;
import com.pody.service.managers.SearchManager;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class SearchManagerImpl implements SearchManager {
    private CategoryRepository categoryRepository;
    private PodcastRepository podcastRepository;
    private UserRepository userRepository;
    private HashtagRepository hashtagRepository;
    private ModelMapper modelMapper;

    @Autowired
    public SearchManagerImpl(CategoryRepository categoryRepository, PodcastRepository podcastRepository,
                             UserRepository userRepository, HashtagRepository hashtagRepository, ModelMapper modelMapper) {
        this.categoryRepository = categoryRepository;
        this.podcastRepository = podcastRepository;
        this.userRepository = userRepository;
        this.hashtagRepository = hashtagRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public ResponseEntity searchForEverything(String searchString) {
        try {
            if (searchString != null) {
                String search = "%" + searchString + "%";

                List<UserSearchDto> users = userRepository.searchUsers(search);

                List<CategorySearchDto> categories = categoryRepository.searchCategory(search);
                List<PodcastListDto> podcasts = podcastRepository.searchPodcast(search, PageRequest.of(0, 24, Sort.by(Sort.Direction.DESC, "createdDate")));
//                List<HashtagSearchDto> hashtags = hashtagRepository.searchHashtag(search);

                SearchResponseDto result = new SearchResponseDto();

                result.setUsers(users);
                result.setCategories(categories);
                result.setPodcasts(podcasts);
//                result.setHashtags(hashtags);

                return ResponseEntity.ok(result);
            } else {
                return new ResponseEntity(ErrorJsonHandler.EMPTY_BODY, HttpStatus.BAD_REQUEST);
            }
        } catch (NullPointerException e) {
            return new ResponseEntity(ErrorJsonHandler.NULL_POINTER_EXCEPTION, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
