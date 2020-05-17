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
public class CategoryFollow {
    @Id
    @Column(length = 16)
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid2")
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private Category category; //category id

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private User follower; //user id which want to follow the category

    public CategoryFollow(UUID id) {
        this.id = id;
    }
}
