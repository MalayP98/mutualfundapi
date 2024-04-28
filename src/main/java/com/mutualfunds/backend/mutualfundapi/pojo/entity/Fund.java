package com.mutualfunds.backend.mutualfundapi.pojo.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Column;
import javax.persistence.GenerationType;


@Entity
@Table(name = "fund")
@Getter
@Setter
public class Fund extends AuditingEntity{

    @Column(name = "name")
    private String name;

    @Column(name = "percentage")
    private Integer percentage;
}
