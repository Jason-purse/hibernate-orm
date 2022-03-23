package org.hibernate.orm.custom.test.base;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Version;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.orm.custom.test.base.model.Event;
import org.junit.jupiter.api.Test;

import java.util.Date;

public class BaseTests {
    @Test
    public void test() {
        System.out.println(ClassLoader.getSystemResource("META-INF/persistence.xml"));
    }

    @Test
    public void version() {
        System.out.println(Version.getVersionString());
    }

    @Test
    public  void findResource() {
        System.out.println(Thread.currentThread().getContextClassLoader().getName());
        System.out.println(Thread.currentThread().getContextClassLoader().getParent().getName());
        System.out.println(Thread.currentThread().getContextClassLoader().getResource("b.txt"));
        System.out.println(BaseTests.class.getResource("a.txt"));
        System.out.println(BaseTests.class.getClassLoader().getResource("a.txt"));
        System.out.println(ClassLoader.getSystemResource("a.txt"));
        System.out.println(ClassLoader.getSystemClassLoader().getName());
        System.out.println(ClassLoader.getPlatformClassLoader().getName());
        System.out.println(ClassLoader.getSystemClassLoader().getParent().getName());
        System.out.println(ClassLoader.getPlatformClassLoader().getResource("b.txt"));
        System.out.println(ClassLoader.getSystemClassLoader().getResource("org.hibernate.orm.custom.test.b.txt"));
    }


    // start
    @Test
    public void started() {
        StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure()
                .build(); // 构建完毕, 还需要以此创建会话工厂

        try {
            SessionFactory  sessionFactory = new MetadataSources( registry ).buildMetadata().buildSessionFactory();


            // 开启一个会话
            Session session = sessionFactory.openSession();
            session.beginTransaction();
            session.persist( new Event( "Our very first event!", new Date() ) );
            session.persist( new Event( "A follow up event", new Date() ) );
            session.getTransaction().commit();
            session.close();
        }
        catch (Exception e) {
            // The registry would be destroyed by the SessionFactory, but we had trouble building the SessionFactory
            // so destroy it manually.

            StandardServiceRegistryBuilder.destroy( registry );
        }

    }
}
