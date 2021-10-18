package io.edurt.lc.guice;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.matcher.Matchers;
import org.junit.Test;

public class TestGuiceAOP
{
    @Test
    public void test()
    {
        Injector injector = Guice.createInjector(binder -> binder.bindInterceptor(Matchers.any(), Matchers.any(), new GuiceAopLoggerMethodInterceptor()));
        GuiceAopService aopService = injector.getInstance(GuiceAopService.class);
        aopService.println("AOP Demo");
    }
}
