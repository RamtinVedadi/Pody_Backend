package com.pody.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table
public class CommentHashtag {
    @Id
    @Column(length = 16)
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid2")
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private Hashtag hashtag;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private Comment comment;
}
