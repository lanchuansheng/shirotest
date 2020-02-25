package com.lcs.entity;

import lombok.Data;

import java.util.Set;
@Data
public class User {
    private Long id;

    private String username;

    private String password;

    private Set<Role> roles;



    public User(Long id, String username, String password) {
        this.id = id;
        this.username = username;
        this.password = password;
    }

    private static User instance = new User();

    private User(){};

    public static User getInstance(){
        return instance;
    }

    public void showMessage(){
        System.out.println("Hello World!");
    }

}