package com.pody.dto.responses;

import java.util.Date;
import java.util.UUID;

public abstract class AbstractDto {
    public UUID id;
    public Date createdDate;
    public Date updateDate;
}
