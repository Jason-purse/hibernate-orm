package org.hibernate.orm.custom.test;

import org.hibernate.SessionFactory;
import org.hibernate.Version;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.BootstrapServiceRegistryBuilder;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.junit.jupiter.api.Test;

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
                .build();

        try {
            SessionFactory  sessionFactory = new MetadataSources( registry ).buildMetadata().buildSessionFactory();
        }
        catch (Exception e) {
            // The registry would be destroyed by the SessionFactory, but we had trouble building the SessionFactory
            // so destroy it manually.
            StandardServiceRegistryBuilder.destroy( registry );
        }

    }
}
