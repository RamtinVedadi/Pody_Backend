package com.pody.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table
public class BlogLike {
    @Id
    @Column(columnDefinition = "binary(16)")
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid2")
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private Blog blog;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private User user;

    @Column
    private Date createdDate;

    public BlogLike(UUID id) {
        this.id = id;
    }
}
