package com.pody.dto.responses;

import com.pody.dto.repositories.PodcastListDto;
import com.pody.model.Category;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryReadResultDto {
    private Category categoryInfo;
    private List<PodcastListDto>  categoryPodcasts;
    private Boolean isFollow;
}
