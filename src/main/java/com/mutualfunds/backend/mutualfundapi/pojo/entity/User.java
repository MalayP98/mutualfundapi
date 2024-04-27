package com.mutualfunds.backend.mutualfundapi.pojo.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(name = "users")
public class User extends AuditingEntity{

    public User(String username, Long phoneNumber) {
        this.username = username;
        this.phoneNumber = phoneNumber;
    }

    @Column(name = "username")
    private String username;

    @Column(name = "phone_number")
    private Long phoneNumber;
}
