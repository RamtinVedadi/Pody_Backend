package com.pody.service.managers;

import com.pody.model.News;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

public interface NewsManager {
    ResponseEntity create(News news);

    ResponseEntity update(News news, UUID id);

    ResponseEntity delete(UUID id);

    ResponseEntity listForHomePage();

    ResponseEntity fullList();
}
