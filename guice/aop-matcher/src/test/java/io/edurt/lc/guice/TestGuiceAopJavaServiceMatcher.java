package io.edurt.lc.guice;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.matcher.Matchers;
import org.junit.Test;

public class TestGuiceAopJavaServiceMatcher
{
    @Test
    public void test()
    {
        Injector injector = Guice.createInjector(binder -> binder.bindInterceptor(new GuiceAopJavaServiceMatcher(),
                Matchers.any(),
                new GuiceAopMatcherMethodInterceptor()));
        GuiceAopMatcherService javaServiceMatcher = injector.getInstance(GuiceAopMatcherServiceImpl.class);
        javaServiceMatcher.println("Hello Guice!!!");
    }
}
