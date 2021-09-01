package io.edurt.lc.guice;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Scopes;
import org.junit.Test;

public class TestGuiceScope
{
    @Test
    public void test()
    {
        Injector injector = Guice.createInjector(binder -> binder.bind(GuiceScopeService.class).to(GuiceScopeServiceImpl.class).in(Scopes.SINGLETON));
        GuiceScopeService service = injector.getInstance(GuiceScopeService.class);
        service.println("Singleton Scope");
    }
}
