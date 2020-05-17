package com.pody.dto.responses;

import java.util.List;
import java.util.UUID;

public class CommentListDto  {
    public UUID id;
    public String description;
    public List<CommentListDto> children;
}
