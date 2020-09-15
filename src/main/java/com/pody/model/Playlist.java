package com.pody.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table
public class Playlist extends AbstractModel {
    @Column(nullable = false)
    private String title;

    @Column(length = 65535, columnDefinition = "Text")
    private String description;

    public Playlist(UUID id) {
        this.setId(id);
    }
}
