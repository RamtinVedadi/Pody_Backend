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
public class AdsBanner extends AbstractModel {
    @Column
    private String title;

    @Column
    private String link;
}
