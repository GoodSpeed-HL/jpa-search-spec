package com.example.demo.domain;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;


@Entity
@Data
public class User {
    @javax.persistence.Id
    private Long Id;

    private String username;
}
