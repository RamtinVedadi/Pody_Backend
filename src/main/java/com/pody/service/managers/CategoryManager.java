package com.pody.service.managers;

import com.pody.dto.requests.TwoIDRequestDto;
import com.pody.dto.responses.IdResponseDto;
import com.pody.model.Category;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

public interface CategoryManager {
    ResponseEntity create(Category category);

    ResponseEntity read(TwoIDRequestDto dto);

    ResponseEntity update(Category category, UUID id);

    ResponseEntity delete(UUID id);

    ResponseEntity tree();

    ResponseEntity listParents();

    ResponseEntity listChildren(UUID id);

    ResponseEntity listAllCategoryPage(IdResponseDto dto);

    ResponseEntity readInfinite(int till, int to, IdResponseDto dto);
}
