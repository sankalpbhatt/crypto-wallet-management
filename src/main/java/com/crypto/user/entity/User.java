package com.crypto.user.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import org.hibernate.annotations.Generated;

import java.time.LocalDateTime;

@Entity(name = "user")
public class User {
    @Id
    @Generated
    private String id;
    private String firstName;
    private String lastName;
    private String phone;
    private String email;
    private String password;
    private LocalDateTime createdDate = LocalDateTime.now();;
    private LocalDateTime updatedDate = null;
    private LocalDateTime deletedDate = null;

    public User(){
        // Do Nothing
    }

    public User(String firstName, String lastName, String phone, String email, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.email = email;
        this.password = password;
    }
}
