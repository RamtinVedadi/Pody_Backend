package com.pody.dto.responses;

import com.pody.dto.repositories.CategorySearchDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserSubscriptionsListDto {
    private List<SubscriptionsDto> followingChannels;
    private List<CategorySearchDto> followingCategories;
}
