package com.pody.model;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

//this class is for each podcast daily view count

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table
public class PodcastView {
    @Id
    @Column(length = 16)
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid2")
    private UUID id;

    @Column(nullable = false)
    private Date date;

    @Column(nullable = false)
    private Integer count;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private Podcast podcast;

    public PodcastView(UUID id) {
        this.id = id;
    }
}
