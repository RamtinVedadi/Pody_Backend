package com.pody.dto.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SpecialPodcastsDto {
    public UUID id;
    public String image;
    public String title;
    public Boolean disable;
}
