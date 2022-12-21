package io.github.realdengziqi.ad_business.tools;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.reflections.Reflections;

import javax.persistence.Entity;

import java.util.Set;

/**
 * @author: tony
 * @date: 2022-12-07 13:56
 * @description:
 */
public class DatabaseTool {

    public static final SessionFactory sessionFactory ;

    static {
        Configuration configuration = new Configuration();
        Reflections reflections = new Reflections("io.github.realdengziqi.ad_business");
        Set<Class<?>> typesAnnotatedWith = reflections.getTypesAnnotatedWith(Entity.class);
        for (Class<?> aClass : typesAnnotatedWith) {
            configuration.addAnnotatedClass(aClass);
        }
        sessionFactory = configuration.configure().buildSessionFactory();
    }

    public static Session getSession() {
        return sessionFactory.openSession();
    }


}
