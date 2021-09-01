package io.edurt.lc.guice;

import com.google.inject.Guice;
import com.google.inject.Injector;
import org.junit.Test;

public class TestGuiceCustomScope
{
    @Test
    public void test()
    {
        Injector injector = Guice.createInjector(binder -> {
            binder.bind(GuiceScopeService.class).to(GuiceScopeServiceImpl.class).in(new GuiceCustomScopeImpl());
        });
        for (int i = 0; i < 3; i++) {
            System.out.println(injector.getInstance(GuiceScopeService.class).hashCode());
        }
    }
}
