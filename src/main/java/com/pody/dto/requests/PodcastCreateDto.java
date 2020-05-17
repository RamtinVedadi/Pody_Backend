package com.pody.dto.requests;


import com.pody.model.Podcast;
import com.pody.model.PodcastCategory;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PodcastCreateDto {
    private Podcast podcast;
    private PodcastCategory podcastCategory;
}
