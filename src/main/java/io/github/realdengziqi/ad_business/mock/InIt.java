package io.github.realdengziqi.ad_business.mock;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import io.github.realdengziqi.ad_business.entity.Ads;
import io.github.realdengziqi.ad_business.entity.Product;
import io.github.realdengziqi.ad_business.entity.Seller;
import io.github.realdengziqi.ad_business.entity.ServerHost;
import io.github.realdengziqi.ad_business.tools.DatabaseTool;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.Session;

import java.io.InputStream;
import java.util.Scanner;

/**
 * @author: tony
 * @date: 2022-12-07 16:53
 * @description:
 */
public class InIt {

    /**
     * 初始化
     */
    public void initDeviceAgent() {
        // 0. crawler-user-agents.json文件中放的是各种搜索引擎和爬虫程序常用的user-agent
        InputStream resourceAsStream = getClass().getClassLoader().getResourceAsStream("crawler-user-agents.json");
        assert resourceAsStream != null;
        Scanner scanner = new Scanner(resourceAsStream);
        String content = scanner.useDelimiter("\\A").next();
        JSONArray agentArray = JSONObject.parseArray(content);
        for (int i = 0; i < agentArray.size(); i++) {
            JSONObject jsonObject = agentArray.getJSONObject(i);
            JSONArray instances = jsonObject.getJSONArray("instances");
            String url = jsonObject.getString("url");
            if (StringUtils.isEmpty(url)) {
                for (int j = 0; j < instances.size(); j++) {
                    String agent = instances.getString(j);
                    if(agent.contains("bot")){
                        PublicStatus.BOT_AGENT.add(agent);
                    } else {
                        PublicStatus.SPIDER_AGENT.add(agent);
                    }
                }
            }else {
                for (int j = 0; j < instances.size(); j++) {
                    PublicStatus.BOT_AGENT.add(instances.getString(j));
                }
            }
        }

        // 1. useragent_cache里放的是普通用户浏览器常用的user-agent
        InputStream useragentCache = getClass().getClassLoader().getResourceAsStream("useragent_cache");
        Scanner normalUseragentScanner = new Scanner(useragentCache);
        while (normalUseragentScanner.hasNextLine()){
            String oneAgent = normalUseragentScanner.nextLine();
            PublicStatus.NORMAL_AGENT.add(oneAgent);
        }
    }

    /**
     * 初始化卖家数据
     */
    public void initSeller() {
        InputStream resourceAsStream = getClass().getClassLoader().getResourceAsStream("all_seller_data.csv");
        Scanner scanner = new Scanner(resourceAsStream);
        // 越过首行
        scanner.nextLine();
        Session session = DatabaseTool.getSession();
        session.beginTransaction();
        while (scanner.hasNextLine()){
            String row = scanner.nextLine();
            String[] split = row.split(",");
            Seller seller = new Seller();
            seller.setId(new Long(split[0]));
            seller.setName(split[1]);
            session.save(seller);
        }
        session.getTransaction().commit();
        session.close();
    }

    /**
     * 初始化产品数据
     */
    public void initProducts() {
        InputStream resourceAsStream = getClass().getClassLoader().getResourceAsStream("all_product_data.csv");
        Scanner scanner = new Scanner(resourceAsStream);
        // 越过首行
        scanner.nextLine();
        Session session = DatabaseTool.getSession();
        session.beginTransaction();
        while (scanner.hasNextLine()){
            String row = scanner.nextLine();
            String[] split = row.split(",");
            Product product = new Product();
            product.setId(new Long(split[0]));
            product.setName(split[1]);
            product.setPrice(StringUtils.isEmpty(split[2])?null:new Double(split[2]) );
            product.setSellerId(new Long(split[3]));
            session.save(product);
        }
        session.getTransaction().commit();
        session.close();
    }

    public void initAds() {
        InputStream resourceAsStream = getClass().getClassLoader().getResourceAsStream("ads.csv");
        Scanner scanner = new Scanner(resourceAsStream);
        // 越过首行
        scanner.nextLine();
        Session session = DatabaseTool.getSession();
        session.beginTransaction();
        while (scanner.hasNextLine()) {
            String s = scanner.nextLine();
            String[] split = s.split(",");
            Ads ads = new Ads();
            ads.setId(new Long(split[0]));
            ads.setProductId(new Long(split[1]));
            ads.setSellerId(new Long(split[2]));
            session.save(ads);
        }
        session.getTransaction().commit();
        session.close();
    }

    public void initServerHost() {
        Session session = DatabaseTool.getSession();
        session.beginTransaction();
        for (int i = 0; i < 20; i++) {
            ServerHost serverHost = new ServerHost(PublicStatus.getOneIpv4());
            session.save(serverHost);
        }
        session.getTransaction().commit();
        session.close();

    }



    public static void main(String[] args) {
        InIt inIt = new InIt();
        inIt.initSeller();
        inIt.initProducts();
        inIt.initAds();
        inIt.initServerHost();
    }


}
