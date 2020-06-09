package com.pody.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@Entity
@Table
public class User extends AbstractModelUser {
    @Column
    private String firstName;

    @Column
    private String lastName;

    @Column(nullable = false)
    private String title = "";

    @Column
    private Date birthDate;

    @Column
    private String email;

    @Column
    private Boolean isPremium = false;

    @Column
    private int gender;//0 for women 1 for men

    @Column
    private String rssUrl;

    @Column
    private int followerCount = 0;

    @Column
    private int followingCount = 0;

    @Column
    private String instagramUrl = null;

    @Column
    private String telegramUrl = null;

    @Column
    private String facebookUrl = null;

    @Column
    private String twitterUrl = null;

    @Column
    private String youtubeUrl = null;

    @Column
    private String discordUrl = null;

    @Column
    private String websiteUrl = null;

    @Column
    private String language = "";

    @Column
    private String channelImage = "http://pody.ir/defaultImages/default.jpg";//image of channel page top for v-parallax

    @Column
    private Boolean isChannel = false;

    public User() {
    }

    public User(UUID id) {
        this.setId(id);
    }

    public User(UUID id, String rssUrl) {
        this.setId(id);
        this.rssUrl = rssUrl;
    }


    @Override
    public List<ValidationError> validate() {
        List<ValidationError> errors = new ArrayList<>();
        if (email == null) {
            errors.add(new ValidationError("email", "empty"));
        }
        if (getUsername() == null) {
            errors.add(new ValidationError("username", "empty"));
        }
        if (getPassword() == null) {
            errors.add(new ValidationError("password", "empty"));
        }
        return errors;
    }
}
