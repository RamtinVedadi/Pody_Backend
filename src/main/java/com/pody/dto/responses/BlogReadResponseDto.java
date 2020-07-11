package com.pody.dto.responses;

import com.pody.dto.repositories.BlogListDto;
import com.pody.dto.repositories.BlogReadDto;
import com.pody.dto.repositories.PodcastListDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BlogReadResponseDto {
    private BlogReadDto blogInfo;
    private Boolean userFollow;
    private List<PodcastListDto> podcast;
    private List<BlogListDto> suggestionBlogs;
}
