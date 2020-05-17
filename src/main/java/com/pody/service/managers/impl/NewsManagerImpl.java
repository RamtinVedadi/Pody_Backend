package com.pody.service.managers.impl;

import com.pody.dto.repositories.NewsListDto;
import com.pody.model.News;
import com.pody.repository.NewsRepository;
import com.pody.service.ErrorJsonHandler;
import com.pody.service.managers.NewsManager;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class NewsManagerImpl implements NewsManager {
    private NewsRepository newsRepository;
    private ModelMapper modelMapper;

    @Autowired
    public NewsManagerImpl(NewsRepository newsRepository, ModelMapper modelMapper) {
        this.newsRepository = newsRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public ResponseEntity create(News news) {
        try {
            if (news != null) {
                news.setCreatedDate(new Date());
                news.setUpdateDate(new Date());
                news.setDeleteFlag(false);
                News result = newsRepository.save(news);
                return ResponseEntity.ok(result);
            } else {
                return new ResponseEntity(ErrorJsonHandler.EMPTY_BODY, HttpStatus.BAD_REQUEST);
            }
        } catch (NullPointerException e) {
            return new ResponseEntity(ErrorJsonHandler.NULL_POINTER_EXCEPTION, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity update(News news, UUID id) {
        try {
            if (id != null) {
                if (news != null) {
                    News dbNews = newsRepository.findOneById(id);

                    news.setUpdateDate(new Date());

                    modelMapper.map(news, dbNews);

                    News result = newsRepository.save(dbNews);
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

    @Override
    public ResponseEntity delete(UUID id) {
        try {
            if (id != null) {
                newsRepository.deleteById(id);
                return ResponseEntity.ok(ErrorJsonHandler.SUCCESSFUL);
            } else {
                return new ResponseEntity(ErrorJsonHandler.EMPTY_BODY, HttpStatus.BAD_REQUEST);
            }
        } catch (NullPointerException e) {
            return new ResponseEntity(ErrorJsonHandler.NULL_POINTER_EXCEPTION, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity listForHomePage() {
        try {
            Date date = new Date();
            Calendar c = Calendar.getInstance();
            c.setTime(date);
            int i = c.get(Calendar.DAY_OF_WEEK) - c.getFirstDayOfWeek();
            c.add(Calendar.DATE, -i - 7);
            Date start = c.getTime();
            c.add(Calendar.DATE, 6);
            Date end = c.getTime();

            List<NewsListDto> lists = newsRepository.listNews(end, start, PageRequest.of(0, 15, Sort.by(Sort.Direction.DESC, "createdDate")));

            return ResponseEntity.ok(lists);
        } catch (NullPointerException e) {
            return new ResponseEntity(ErrorJsonHandler.NULL_POINTER_EXCEPTION, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity fullList() {
        return null;
    }
}
