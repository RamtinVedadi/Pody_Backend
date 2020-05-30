package com.pody.contoller.impl;

import com.pody.contoller.SearchController;
import com.pody.dto.requests.StringRequestDto;
import com.pody.service.managers.SearchManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = {"*", "https://pody.ir" , "https://www.pody.ir"}, maxAge = 3600)
public class SearchControllerImpl implements SearchController {
    @Autowired
    private SearchManager searchManager;

    @Override
    public ResponseEntity search(@RequestBody StringRequestDto searchString) {
        return searchManager.searchForEverything(searchString.getStringValue());
    }
}
