package com.pody.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table
public class Category extends AbstractModel {
    @Column
    private String name;

    @Column
    private String englishName;

    @Column(length = 65535, columnDefinition = "Text")
    private String description;

    @Column
    private Boolean deleteFlag = false;

    @ManyToOne(fetch = FetchType.LAZY)
    private Category parent;

    public Category(UUID id, String name) {
        setId(id);
        this.name = name;
    }

    public Category(UUID id, String name, String imageAddress) {
        setId(id);
        this.name = name;
        setImageAddress(imageAddress);
    }
}
