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
public class UserFollow {
    @Id
    @Column(length = 16)
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid2")
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private User user; // id logined user

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private User follower; // id podcaster

    public UserFollow(UUID id) {
        this.id = id;
    }
}
