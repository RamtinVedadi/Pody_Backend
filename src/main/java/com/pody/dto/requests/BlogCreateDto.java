package com.pody.dto.requests;

import com.pody.model.Blog;
import com.pody.model.BlogCategory;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BlogCreateDto {
    private Blog blog;
    private BlogCategory blogCategory;
}
