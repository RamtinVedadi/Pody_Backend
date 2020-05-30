package com.pody.model;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

@Data
@MappedSuperclass
public abstract class AbstractModel implements Serializable {
    @Id
    @Column(columnDefinition = "binary(16)")
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid2")
    private UUID id;

    @Column(nullable = true)
    private Date createdDate;

    @Column
    private Date updateDate;

    @Column(length = 65535, columnDefinition = "Text")
    private String imageAddress;
}
