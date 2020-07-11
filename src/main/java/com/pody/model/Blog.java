package com.pody.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table
public class Blog {
    @Id
    @Column(columnDefinition = "binary(16)")
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid2")
    private UUID id;

    @Column(length = 65535, columnDefinition = "Text")
    private String description;

    @Column(length = 200)
    private String shortDescription;

    @Column
    private String title;

    @Column
    private Date createdDate;

    @Column
    private Date updateDate;

    @Column(length = 65535, columnDefinition = "Text")
    private String imageAddress;

    @Column
    private int likeCount = 0;

    @Column
    private int viewCount = 0;

    @Column(nullable = true)
    private Boolean isPublish = false;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private User user;

    public Blog(UUID id) {
        this.id = id;
    }
}
