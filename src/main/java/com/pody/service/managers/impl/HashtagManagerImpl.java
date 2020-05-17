package com.pody.service.managers.impl;

import com.pody.model.Hashtag;
import com.pody.repository.HashtagRepository;
import com.pody.service.ErrorJsonHandler;
import com.pody.service.managers.HashtagManager;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional
public class HashtagManagerImpl implements HashtagManager {
    private HashtagRepository hashtagRepository;
    private ModelMapper modelMapper;

    @Autowired
    public HashtagManagerImpl(HashtagRepository hashtagRepository, ModelMapper modelMapper) {
        this.hashtagRepository = hashtagRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public ResponseEntity create(Hashtag hashtag) {
        try {
            if (hashtag != null) {
                Hashtag result = hashtagRepository.save(hashtag);
                return ResponseEntity.ok(result);
            } else {
                return new ResponseEntity(ErrorJsonHandler.EMPTY_BODY, HttpStatus.BAD_REQUEST);
            }
        } catch (NullPointerException e) {
            return new ResponseEntity(ErrorJsonHandler.NULL_POINTER_EXCEPTION, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity delete(UUID id) {
        try {
            if (id != null) {
                hashtagRepository.deleteById(id);
                return ResponseEntity.ok(ErrorJsonHandler.SUCCESSFUL);
            } else {
                return new ResponseEntity(ErrorJsonHandler.EMPTY_BODY, HttpStatus.BAD_REQUEST);
            }
        } catch (NullPointerException e) {
            return new ResponseEntity(ErrorJsonHandler.NULL_POINTER_EXCEPTION, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
