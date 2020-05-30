package com.pody.model;

import lombok.*;

import javax.persistence.*;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table
public class Podcast extends AbstractModel {
    @Column(nullable = false)
    private String title;

    @Column(length = 150)
    private String shortDescription;

    @Column(length = 65535,columnDefinition="Text")
    private String description;

    @Column
    private int likeCount = 0;

    @Column
    private int disLikeCount = 0;

    @Column
    private int viewCount = 0;

    @Column(nullable = true, length = 65535,columnDefinition="Text")
    private String audioAddress;

    @Column
    private String duration;

    @Column
    private Boolean listenLater = false;

    @Column(nullable = true)
    private Integer episodeNumber;

    @Column(nullable = true)
    private Integer seasonNumber;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private User user;

    public Podcast(UUID id, String title, String imageAddress, String shortDescription, int viewCount) {
        setId(id);
        this.title = title;
        setImageAddress(imageAddress);
        this.shortDescription = shortDescription;
        this.viewCount = viewCount;
    }

    public Podcast(UUID id) {
        setId(id);
    }
}
