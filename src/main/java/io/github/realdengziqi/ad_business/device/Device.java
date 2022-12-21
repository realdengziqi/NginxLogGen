package io.github.realdengziqi.ad_business.device;

import io.github.realdengziqi.etl_gen.IpGen;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author: tony
 * @date: 2022-12-20 16:48
 * @description:
 */
@AllArgsConstructor
@Data
public class Device {

    private String useragent;

    private String ipv4;

    private String type;


}
