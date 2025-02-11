package org.example.factory;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class MySessionFactory {
    private static SessionFactory sessionFactory;

    private MySessionFactory() {
    }

    public static SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            Configuration config = new Configuration();
            config.configure("hibernate-cfg.xml");
            sessionFactory = config.buildSessionFactory();
        }
        return sessionFactory;
    }


}
