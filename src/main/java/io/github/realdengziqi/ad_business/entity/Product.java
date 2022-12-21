package io.github.realdengziqi.ad_business.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

/**
 * @author: tony
 * @date: 2022-12-19 17:32
 * @description:
 */
@Entity
@Setter
@Getter
public class Product {
    @Id
    @Column(name = "id", nullable = false)
    private Long id;

    @Column
    private String name;

    @Column
    private Double price;

    @Column(name = "seller_id")
    private Long sellerId;

}
