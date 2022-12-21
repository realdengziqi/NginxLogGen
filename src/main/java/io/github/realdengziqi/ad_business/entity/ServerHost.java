package io.github.realdengziqi.ad_business.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

/**
 * @author: tony
 * @date: 2022-12-07 16:03
 * @description:
 */
@Entity
@Table(name = "ServerHost")
@Setter
@Getter
@NoArgsConstructor
public class ServerHost {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "ipv4")
    private String ipv4;

    public ServerHost(String ipv4) {
        this.ipv4 = ipv4;
    }

    @Override
    public String toString() {
        return "ServerHost{" +
                "id=" + id +
                ", ipv4='" + ipv4 + '\'' +
                '}';
    }
}
