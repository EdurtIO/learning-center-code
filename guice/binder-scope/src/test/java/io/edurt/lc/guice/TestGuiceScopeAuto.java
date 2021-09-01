package io.edurt.lc.guice;

import com.google.inject.Guice;
import com.google.inject.Injector;
import org.junit.Test;

public class TestGuiceScopeAuto
{
    @Test
    public void test()
    {
        Injector injector = Guice.createInjector(binder -> binder.bind(GuiceScopeService.class).to(GuiceScopeServiceAutoImpl.class));
        GuiceScopeService service = injector.getInstance(GuiceScopeService.class);
        service.println("Singleton Auto Scope");
    }
}
