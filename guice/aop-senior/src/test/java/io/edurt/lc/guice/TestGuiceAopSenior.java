package io.edurt.lc.guice;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.matcher.Matchers;
import com.google.inject.name.Names;
import org.junit.Test;

public class TestGuiceAopSenior
{
    @Inject
    private PrintlnService printlnService;

    @Test
    public void test()
    {
        Injector injector = Guice.createInjector(binder -> {
            GuiceAopInjectionMethodInterceptor injectionMethodInterceptor = new GuiceAopInjectionMethodInterceptor();
            binder.requestInjection(injectionMethodInterceptor);
            binder.bindInterceptor(Matchers.any(), Matchers.annotatedWith(Names.named("println")), injectionMethodInterceptor);
        });
        TestGuiceAopSenior application = injector.getInstance(TestGuiceAopSenior.class);
        application.printlnService.println("Hello Guice AOP");
    }
}
