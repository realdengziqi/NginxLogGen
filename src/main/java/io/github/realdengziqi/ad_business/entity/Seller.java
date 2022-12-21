package io.github.realdengziqi.ad_business.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

/**
 * @author: tony
 * @date: 2022-12-19 15:07
 * @description:
 */
@Entity
@Setter
@Getter
public class Seller {
    @Id
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

}
