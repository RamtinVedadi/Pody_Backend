package com.pody.dto.responses;

import com.pody.dto.repositories.PodcastListDto;
import com.pody.model.Category;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryInfoDto {
    private UUID id;
    private String name;
    private String description;
    private String imageAddress;
    private List<PodcastListDto> podcasts;
}
