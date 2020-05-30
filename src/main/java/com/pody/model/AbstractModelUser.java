package com.pody.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;
import java.util.List;
import java.util.UUID;

@Data
@EqualsAndHashCode(of = "id")
@MappedSuperclass
public abstract class AbstractModelUser implements Serializable {
    @Id
    @Column(columnDefinition = "binary(16)")
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid2")
    private UUID id;

    @Column(nullable = false)
    private String username;

    @Column
    private String password;

    @Column
    private String address;

    @Column(length = 65535,columnDefinition="Text")
    private String profileImageAddress;

    @Column
    private String phoneNumber;

    @Column
    private String city;

    @Column
    private String province;

    @Column
    private Boolean deleteFlag = false;//0 is not delete and 1 is deleted

    @Column
    private String bio;

    public abstract List<ValidationError> validate();
}
