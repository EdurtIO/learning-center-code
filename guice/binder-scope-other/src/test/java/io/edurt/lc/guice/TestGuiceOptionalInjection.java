package io.edurt.lc.guice;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import org.junit.Test;

public class TestGuiceOptionalInjection
{
    @Inject(optional = true)
    private GuiceService service = new GuiceOptionalInjectionDefaultService();

    @Test
    public void testDefault()
    {
        Injector injector = Guice.createInjector(binder -> {
        });
        injector.getInstance(TestGuiceOptionalInjection.class).service.println();
    }

    @Test
    public void testCustom()
    {
        Injector injector = Guice.createInjector(binder -> {
            binder.bind(GuiceService.class).to(GuiceOptionalInjectionCustomService.class);
        });
        injector.getInstance(TestGuiceOptionalInjection.class).service.println();
    }
}
