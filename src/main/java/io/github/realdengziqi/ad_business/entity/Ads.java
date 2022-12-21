package io.github.realdengziqi.ad_business.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * @author: tony
 * @date: 2022-12-07 16:03
 * @description:
 */
@Entity
@Setter
@Getter
public class Ads {
    @Id
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "product_id")
    private Long productId;

    @Column(name = "seller_id")
    private Long sellerId;

    @Override
    public String toString() {
        return "Ads{" +
                "id=" + id +
                ", productId=" + productId +
                ", sellerId=" + sellerId +
                '}';
    }

    public String getExposePath() {
        return "/ad/tencent/expose?id=" + this.getId();
    }

    /**
     * 落地页
     * @return
     */
    public String getTargetPath() {
        return "ad/tencent/click?id=" + this.getId();
    }
}
