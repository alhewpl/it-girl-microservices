package com.itGirl.ToDo.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Date;

@Entity
@Getter
@Setter
@Table(name="USERS")
public class User {
    @Column(name="USER_NAME", nullable = false, length = 100)
    private String userName;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="USER_ID", nullable = false, insertable = false)
    private int userId;

    @Column(name="EMAIL_ADDRESS", nullable = false, length = 100)
    private String emailAddress;

    @Column(name="PASSWORD", nullable = false, length = 16)
    private String password;

    @Column(name="ROLE", nullable = false, length = 10)
    private String role;
}
