package com.pody.dto.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.pody.dto.repositories.CategorySearchDto;
import com.pody.dto.repositories.PodcastListDto;
import com.pody.dto.repositories.UserSearchDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
//@JsonPropertyOrder({"users", "hashtags", "categories", "podcasts"})
@JsonPropertyOrder({"users", "categories", "podcasts"})
public class SearchResponseDto {
    @JsonProperty("users")
    List<UserSearchDto> users;
    @JsonProperty("categories")
    List<CategorySearchDto> categories;
    @JsonProperty("podcasts")
    List<PodcastListDto> podcasts;
//    @JsonProperty("hashtags")
//    List<HashtagSearchDto> hashtags;
}
