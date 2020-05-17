package com.pody.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table
public class News extends AbstractModel {
    @Column
    private String title;

    @Column
    private String description;

    @Column
    private Boolean deleteFlag;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private User user;

    @Column
    private int likeCount = 0;

    @Column
    private int disLikeCount = 0;
}
