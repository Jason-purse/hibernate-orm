package org.hibernate.orm.custom.test.propertiesTests;

import java.util.Properties;

public class PropertiesTests {


    public static void main(String[] args) throws InterruptedException, ClassNotFoundException {
        new Thread(() -> {
            for (int i = 0; i < 100; i++) {
                System.getProperties().put("k3","k5");
            }
        }).start();
        Class.forName("org.hibernate.orm.custom.test.propertiesTests.TestStaticClass",true,PropertiesTests.class.getClassLoader());
    }
}

class TestStaticClass {
    public final static Properties global;
    static  {
        System.out.println("init");
        try {
            Thread.sleep(300);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        normal();
        global = new Properties();
        global.putAll(System.getProperties());

    }

    private static void normal() {
        throw new RuntimeException("123123");
    }
}
