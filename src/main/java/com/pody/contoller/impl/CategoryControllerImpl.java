package com.pody.contoller.impl;

import com.pody.contoller.CategoryController;
import com.pody.dto.requests.TwoIDRequestDto;
import com.pody.dto.responses.IdResponseDto;
import com.pody.model.Category;
import com.pody.service.managers.CategoryManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@CrossOrigin(origins = {"*", "http://pody.ir", "http://www.pody.ir"}, maxAge = 3600)
public class CategoryControllerImpl implements CategoryController {

    @Autowired
    private CategoryManager categoryManager;

    @Override
    public ResponseEntity create(@RequestBody Category category) {
        return categoryManager.create(category);
    }

    @Override
    public ResponseEntity update(@RequestBody Category category, @PathVariable UUID id) {
        return categoryManager.update(category, id);
    }

    @Override
    public ResponseEntity delete(@PathVariable UUID id) {
        return categoryManager.delete(id);
    }

    @Override
    public ResponseEntity tree() {
        return categoryManager.tree();
    }

    @Override
    public ResponseEntity listParents() {
        return categoryManager.listParents();
    }

    @Override
    public ResponseEntity listChildren(@PathVariable UUID id) {
        return categoryManager.listChildren(id);
    }

    @Override
    public ResponseEntity read(@RequestBody TwoIDRequestDto dto) {
        return categoryManager.read(dto);
    }

    @Override
    public ResponseEntity listAllCategoryPage(@RequestBody IdResponseDto dto) {
        return categoryManager.listAllCategoryPage(dto);
    }

    @Override
    public ResponseEntity readInfinite(@PathVariable int till, @PathVariable int to, @RequestBody IdResponseDto dto) {
        return categoryManager.readInfinite(till, to, dto);
    }
}
