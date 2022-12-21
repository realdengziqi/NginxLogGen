package io.github.realdengziqi.ad_business.mock;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.WeightRandom;
import cn.hutool.core.util.RandomUtil;
import com.mysql.cj.log.Log;
import com.sun.org.apache.xml.internal.security.Init;
import io.github.realdengziqi.ad_business.device.Device;
import io.github.realdengziqi.ad_business.entity.Ads;
import io.github.realdengziqi.ad_business.entity.ServerHost;
import io.github.realdengziqi.ad_business.tools.DatabaseTool;
import javafx.util.Pair;
import org.apache.commons.lang3.RandomUtils;
import org.hibernate.Session;
import org.hibernate.graph.RootGraph;
import org.hibernate.query.Query;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.swing.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * @author: tony
 * @date: 2022-12-20 16:47
 * @description:
 */
public class MockNginxLog implements Runnable {

    public ArrayList<Device> devicesPool;

    public Device device;

    public Random random;

    public static Logger logger = Logger.getLogger("test");


    @Override
    public void run() {
    }

    public static void main(String[] args) {


        PublicStatus.loadAllServerHost();

        InIt inIt = new InIt();
        inIt.initDeviceAgent();
        PublicStatus.loadAllAds();
        PublicStatus.loadAllServerHost();

        WeightRandom.WeightObj<String>[] weightObjs = new WeightRandom.WeightObj[]{
                new WeightRandom.WeightObj<>("spider",1),
                new WeightRandom.WeightObj<>("bot",1),
                new WeightRandom.WeightObj<>("normal",500),
        };

        WeightRandom<String> deviceTypeRandom = RandomUtil.weightRandom(weightObjs);
        for (int i = 0; i < 16000; i++) {
            String deviceType = deviceTypeRandom.next();
            Device device = null;
            if ("normal".equals(deviceType)) {
                device = new Device(
                        PublicStatus.getOneNormalAgent(),
                        PublicStatus.getOneIpv4(),
                        deviceType
                );
            } else if ("spider".equals(deviceType)) {
                device = new Device(
                        PublicStatus.getOneSpiderAgent(),
                        PublicStatus.getOneIpv4(),
                        deviceType
                );
            } else {
                device = new Device(
                        PublicStatus.getOneBotAgent(),
                        PublicStatus.getOneIpv4(),
                        deviceType
                );
            }
            PublicStatus.ALL_DEVICE.add(device);
        }

        TimeLine timeLine = new TimeLine(
                "2022-12-15 00:00:00",
                "2022-12-21 00:00:00"
        );
        while (timeLine.hasNext()) {
            DateTime next = timeLine.next();
            Set<Device> devices = RandomUtil.randomEleSet(PublicStatus.ALL_DEVICE, 1);
            devices.
                    parallelStream()
                    .filter(new Predicate<Device>() {
                        @Override
                        public boolean test(Device device) {
                            if ("normal".equals(device.getType())){
                                return TimeLine.ifUse(next);
                            }
                            return true;
                        }
                    })
                    .forEach(
                            new Consumer<Device>() {
                                @Override
                                public void accept(Device device) {
                                    new OneThread(
                                            device,
                                            PublicStatus.getServerHost(),
                                            PublicStatus.getOneAd(),
                                            next
                                            ).run();
                                }
                            }
                    );
        }

    }

    public static class OneThread implements Runnable {

        private Device device;

        private ServerHost serverHost;

        private Ads ad;

        private DateTime startTime;

        public OneThread(Device device, ServerHost serverHost, Ads ad, DateTime startTime) {
            this.device = device;
            this.serverHost = serverHost;
            this.ad = ad;
            this.startTime = startTime;
        }



        @Override
        public void run() {
            if("spider".equals(device.getType())){
                long cum = 0;
                long timestamp = startTime.toTimestamp().getTime();
                for (int i = 0; i < RandomUtil.randomInt(100,200); i++) {
                    cum = cum + RandomUtil.randomLong(1000,5000);
                    System.out.println("\""+DateTime.of(timestamp+cum) + "\",\"" + device.getUseragent() + "\",\"" + device.getIpv4() + "\",\"GET\"," + ad.getExposePath() + "\",\"200\",\"" + serverHost.getIpv4() + "\"");
                }
            }else if("normal".equals(device.getType())) {
                System.out.println("\""+DateTime.of(startTime) + "\",\"" + device.getUseragent() + "\",\"" + device.getIpv4() + "\",\"GET\"," + ad.getExposePath() + "\",\"200\",\"" + serverHost.getIpv4() + "\"");
            }
        }
    }
}
