package com.pody.service.managers;

import org.springframework.http.ResponseEntity;

public interface SearchManager {

    ResponseEntity searchForEverything(String searchString);
}
