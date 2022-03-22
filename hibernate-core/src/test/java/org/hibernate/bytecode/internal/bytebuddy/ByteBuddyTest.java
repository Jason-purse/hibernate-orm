package org.hibernate.bytecode.internal.bytebuddy;

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.implementation.FixedValue;
import net.bytebuddy.matcher.ElementMatchers;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;

import java.lang.ref.SoftReference;

import static net.bytebuddy.matcher.ElementMatchers.is;

public class ByteBuddyTest {
    @Test
    public void test() throws InstantiationException, IllegalAccessException {
        Class<?> dynamicType = new ByteBuddy()
                .subclass(Object.class)
                .method(ElementMatchers.named("toString"))
                .intercept(FixedValue.value("Hello World!"))
                .make()
                .load(getClass().getClassLoader())
                .getLoaded();

        System.out.println(dynamicType);
        System.out.println(dynamicType.newInstance());
        Assert.assertThat(dynamicType.newInstance().toString(), Matchers.is("Hello World!"));
    }


    @Test
    public void softReferenceTests() {

        SoftReference<String> ref = new SoftReference<>("2");

        System.out.println(ref);

        System.gc();

        System.out.println(ref);

    }
}
