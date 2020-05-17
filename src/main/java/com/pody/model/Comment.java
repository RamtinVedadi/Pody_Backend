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
public class Comment {
    @Id
    @Column(length = 16)
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid2")
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private Podcast podcast;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    private Comment parent;

    @Column(length = 300, nullable = false)
    private String description;

    @Column(nullable = true)
    private Date createdDate;

    @Column(nullable = false)
    private int likeCount = 0;

    @Column
    private Boolean isApprove = false;
}
