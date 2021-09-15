package io.edurt.lc.guice;

import com.google.inject.Guice;
import com.google.inject.Injector;
import org.junit.Test;

public class TestGuiceEagerlyLoadingBindings
{
    @Test
    public void test()
    {
        Injector injector = Guice.createInjector(binder -> binder.bind(GuiceEagerlyLoadingBindingsService.class).asEagerSingleton());
        injector.getInstance(GuiceEagerlyLoadingBindingsService.class).println();
    }
}
