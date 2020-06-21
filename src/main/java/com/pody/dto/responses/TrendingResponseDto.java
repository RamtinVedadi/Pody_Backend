package com.pody.dto.responses;

import com.pody.dto.repositories.CategorySearchDto;
import com.pody.dto.repositories.PodcastListDto;
import com.pody.dto.repositories.PodcastTrendingDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TrendingResponseDto {
    private List<PodcastTrendingDto> podcasts;
    private List<CategorySearchDto> categories;
}
