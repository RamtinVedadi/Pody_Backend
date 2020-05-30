package com.pody.contoller.impl;

import com.pody.contoller.NewsController;
import com.pody.model.News;
import com.pody.service.managers.NewsManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@CrossOrigin(origins = {"*", "https://pody.ir" , "https://www.pody.ir"}, maxAge = 3600)
public class NewsControllerImpl implements NewsController {

    @Autowired
    private NewsManager newsManager;

    @Override
    public ResponseEntity create(@RequestBody News news) {
        return newsManager.create(news);
    }

    @Override
    public ResponseEntity update(@RequestBody News news, @PathVariable UUID id) {
        return newsManager.update(news, id);
    }

    @Override
    public ResponseEntity delete(@PathVariable UUID id) {
        return newsManager.delete(id);
    }

    @Override
    public ResponseEntity listForHomePage() {
        return newsManager.listForHomePage();
    }

    @Override
    public ResponseEntity fullList() {
        return newsManager.fullList();
    }
}
