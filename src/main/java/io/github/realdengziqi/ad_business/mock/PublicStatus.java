package io.github.realdengziqi.ad_business.mock;

import cn.hutool.core.util.RandomUtil;
import com.sun.security.ntlm.Server;
import io.github.realdengziqi.ad_business.device.Device;
import io.github.realdengziqi.ad_business.entity.Ads;
import io.github.realdengziqi.ad_business.entity.ServerHost;
import io.github.realdengziqi.ad_business.tools.DatabaseTool;
import io.github.realdengziqi.etl_gen.IpGen;
import org.hibernate.Session;
import org.hibernate.graph.RootGraph;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

/**
 * @author: tony
 * @date: 2022-12-19 17:14
 * @description:
 */
public class PublicStatus {

    public static final ArrayList<String> SPIDER_AGENT = new ArrayList<>();

    public static final ArrayList<String> BOT_AGENT =  new ArrayList<>();

    public static final ArrayList<String> NORMAL_AGENT = new ArrayList<>();

    public static final HashSet<String> USED_IPV4 = new HashSet<>();

    public static final ArrayList<ServerHost> ALL_SERVER_HOST = new ArrayList<>();

    public static final ArrayList<Ads> ALL_ADS = new ArrayList<>();

    public static final Random random = new Random();

    public static final ArrayList<Device> ALL_DEVICE = new ArrayList<>();

    public static final IpGen ipGen = new IpGen();

    public static void loadAllServerHost() {
        Session session = DatabaseTool.getSession();
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<ServerHost> query = criteriaBuilder.createQuery(ServerHost.class);
        query.from(ServerHost.class);
        List<ServerHost> resultList = session.createQuery(query).getResultList();
        ALL_SERVER_HOST.addAll(resultList);
        session.close();
    }

    public static void loadAllAds() {
        Session session = DatabaseTool.getSession();
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<Ads> query = criteriaBuilder.createQuery(Ads.class);
        query.from(Ads.class);
        List<Ads> resultList = session.createQuery(query).getResultList();
        ALL_ADS.addAll(resultList);
        session.close();
    }

    public static ServerHost getServerHost() {
        return RandomUtil.randomEle(ALL_SERVER_HOST);
    }

    public static Ads getOneAd() {
        return RandomUtil.randomEle(ALL_ADS);
    }

    public static String getOneSpiderAgent(){
        return SPIDER_AGENT.get(random.nextInt(SPIDER_AGENT.size()));
    }

    public static String getOneNormalAgent() {
        return NORMAL_AGENT.get(random.nextInt(NORMAL_AGENT.size()));
    }

    public static String getOneBotAgent() {
        return BOT_AGENT.get(random.nextInt(BOT_AGENT.size()));
    }

    public static String getOneIpv4() {
        boolean ipv4flag = true;
        String ipv4 = null;
        while (ipv4flag) {
            ipv4 = ipGen.genOne();
            if (!PublicStatus.USED_IPV4.contains(ipv4)) {
                PublicStatus.USED_IPV4.add(ipv4);
                ipv4flag = false;
            }
        }
        return ipv4;
    }
}
