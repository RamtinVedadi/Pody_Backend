package com.pody.dto.responses;

import com.pody.dto.repositories.PodcastListDto;
import com.pody.model.Category;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TrendingResponseDto {
    private List<PodcastListDto> podcasts;
    private List<Category> categories;
}
