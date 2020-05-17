package com.pody.dto.repositories;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryParentListDto {
    private UUID id;
    private String name;
    private String imageAddress;
}
