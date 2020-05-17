package com.pody.service.managers.impl;

import com.pody.dto.responses.CommentListDto;
import com.pody.model.Comment;
import com.pody.model.CommentHashtag;
import com.pody.model.Hashtag;
import com.pody.model.Podcast;
import com.pody.repository.CommentHashtagRepository;
import com.pody.repository.CommentRepository;
import com.pody.repository.HashtagRepository;
import com.pody.service.ErrorJsonHandler;
import com.pody.service.managers.CommentManager;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional
public class CommentManagerImpl implements CommentManager {
    private CommentRepository commentRepository;
    //    private PodcastRepository podcastRepository;
    private HashtagRepository hashtagRepository;
    private CommentHashtagRepository commentHashtagRepository;
    private ModelMapper modelMapper;

    @Autowired
    public CommentManagerImpl(CommentRepository commentRepository, ModelMapper modelMapper,
//                              PodcastRepository podcastRepository,
                              HashtagRepository hashtagRepository, CommentHashtagRepository commentHashtagRepository) {
        this.commentRepository = commentRepository;
        this.modelMapper = modelMapper;
//        this.podcastRepository = podcastRepository;
        this.hashtagRepository = hashtagRepository;
        this.commentHashtagRepository = commentHashtagRepository;
    }

    @Override
    public ResponseEntity create(Comment comment) {
        try {
            if (comment != null) {
                comment.setId(null);
                comment.setIsApprove(false);
                Comment result = commentRepository.save(comment);
                if (result.getDescription().contains("#")) {
                    String[] hashtags = result.getDescription().split("#");

                    for (int i = 0; i < hashtags.length; i++) {
                        Hashtag h = new Hashtag();
                        h.setName(hashtags[i]);
                        h.setCreatedDate(new Date());
                        h.setImageAddress(null);
                        Hashtag rh = hashtagRepository.save(h);

                        CommentHashtag ch = new CommentHashtag();
                        ch.setComment(result);
                        ch.setHashtag(rh);
                        commentHashtagRepository.save(ch);
                    }
                }
                return ResponseEntity.ok(result);
            } else {
                return new ResponseEntity(ErrorJsonHandler.EMPTY_BODY, HttpStatus.BAD_REQUEST);
            }
        } catch (NullPointerException e) {
            return new ResponseEntity(ErrorJsonHandler.NULL_POINTER_EXCEPTION, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity update(Comment comment, UUID id) {
        try {
            if (id != null) {
                if (comment != null) {
                    Comment dbComment = commentRepository.findOneById(id);
                    modelMapper.map(comment, dbComment);
                    Comment result = commentRepository.save(dbComment);
                    if (result != null) {
                        if (result.getDescription().contains("#")) {
                            String[] hashtags = result.getDescription().split("#");

                            for (int i = 0; i < hashtags.length; i++) {
                                Hashtag h = new Hashtag();
                                h.setName(hashtags[i]);
                                h.setCreatedDate(new Date());
                                h.setImageAddress(null);
                                Hashtag rh = hashtagRepository.save(h);

                                CommentHashtag ch = new CommentHashtag();
                                ch.setComment(result);
                                ch.setHashtag(rh);
                                commentHashtagRepository.save(ch);
                            }
                        }
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
    public ResponseEntity delete(UUID commentId) {
        try {
            if (commentId != null) {
                commentRepository.deleteById(commentId);
                return ResponseEntity.ok(ErrorJsonHandler.SUCCESSFUL);
            } else {
                return new ResponseEntity(ErrorJsonHandler.EMPTY_BODY, HttpStatus.BAD_REQUEST);
            }
        } catch (NullPointerException e) {
            return new ResponseEntity(ErrorJsonHandler.NULL_POINTER_EXCEPTION, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity list(UUID podcastId) {
        try {
            if (podcastId != null) {
//                Podcast p = podcastRepository.findOneById(podcastId);
                Podcast p = new Podcast();
                List<Comment> comments = commentRepository.findAllByPodcast(p);
                Queue<CommentListDto> queue = new ArrayDeque<>();
                CommentListDto root = new CommentListDto();
                queue.add(root);
                while (!queue.isEmpty()) {
                    CommentListDto head = queue.remove();
                    List<CommentListDto> children = findchildrenof(head, comments);
                    if (children != null && children.isEmpty()) {
                        children = null;
                    }
                    head.children = children;
                    if (children != null) {
                        queue.addAll(children);
                    }
                }
                return ResponseEntity.ok(ErrorJsonHandler.SUCCESSFUL);
            } else {
                return new ResponseEntity(ErrorJsonHandler.EMPTY_BODY, HttpStatus.BAD_REQUEST);
            }
        } catch (NullPointerException e) {
            return new ResponseEntity(ErrorJsonHandler.NULL_POINTER_EXCEPTION, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity like(UUID commentId) {
        try {
            if (commentId != null) {
                int result = commentRepository.updateLikesCount(commentId);
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
    public ResponseEntity approve(UUID commentId) {
        try {
            if (commentId != null) {
                int result = commentRepository.updateApproveComment(commentId);
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

    private List<CommentListDto> findchildrenof(CommentListDto commentListDto, List<Comment> allComment) {
        List<CommentListDto> Comments = new ArrayList<>();
        for (Comment cat : allComment) {
            boolean eq;
            if (cat.getParent() == null) {
                eq = commentListDto == null || commentListDto.id == null;
            } else {
                eq = commentListDto != null && Objects.equals(cat.getParent().getId(), commentListDto.id);
            }
            if (eq) {
                CommentListDto child = new CommentListDto();
                child.id = cat.getId();


                Comments.add(child);
            }
        }
        return Comments;
    }
}
