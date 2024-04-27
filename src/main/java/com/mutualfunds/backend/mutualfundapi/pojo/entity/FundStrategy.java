package com.mutualfunds.backend.mutualfundapi.pojo.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "fund_strategy")
@Getter
@Setter
public class FundStrategy {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "description", length = 500)
    private String description;

    @OneToMany(cascade = CascadeType.ALL)
    private List<Fund> funds;
}
