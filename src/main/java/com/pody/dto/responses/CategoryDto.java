package com.pody.dto.responses;

import java.util.List;

public class CategoryDto extends AbstractDto {
    public String name;
    public List<CategoryDto> children;
    public String imageAddress;
}
