package com.hibernate.crud.operations;

import org.hibernate.Session;
import org.hibernate.cfg.Configuration;
import org.hibernate.SessionFactory;

public class HibernateUtil {

    private static final SessionFactory sessionFactory;

    static {
        try {
            // Create the SessionFactory from standard (hibernate.cfg.xml)
            // config file.
            sessionFactory = new Configuration().configure().buildSessionFactory();
        } catch (Throwable ex) {
            // Log the exception.
            System.err.println("Initial SessionFactory creation failed." + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static Session getSession() {
        return sessionFactory.getCurrentSession();
        //return sessionFactory.openSession();
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public static Session beginTransaction() {
        Session session = getSession();
        session.beginTransaction();
        return session;
    }

    public static void commitTransaction() {
        getSession().getTransaction().commit();
    }

    public static void rollbackTransactiom() {
        getSession().getTransaction().rollback();
    }

    public static void closeSession() {
        getSession().close();
    }

}
