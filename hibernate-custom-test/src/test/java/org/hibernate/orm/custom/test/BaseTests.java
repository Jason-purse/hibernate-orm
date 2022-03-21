package org.hibernate.orm.custom.test;

import org.junit.jupiter.api.Test;

public class BaseTests {
    @Test
    public void test() {
        System.out.println(ClassLoader.getSystemResource("META-INF/persistence.xml"));
    }
}
