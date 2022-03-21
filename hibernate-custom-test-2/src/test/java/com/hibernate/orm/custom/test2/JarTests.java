package com.hibernate.orm.custom.test2;

import org.junit.jupiter.api.Test;

public class JarTests {
    @Test
    public void test() {

        System.out.println(ClassLoader.getSystemResource("META-INF/persistence.xml"));
        System.out.println(JarTests.this.getClass().getClassLoader()
                .getResource("META-INF/persistence.xml"));
    }

    public static void main(String[] args) {
        new JarTests().test();
    }
}
