package io.edurt.lc.guice;

import com.google.inject.Guice;
import com.google.inject.Injector;
import org.junit.Test;

public class TestGuiceBasicModule
{

    @Test
    public void test()
    {
        Guice.createInjector(new GuiceBasicModule());
    }

    @Test
    public void test_service()
    {
        Injector injector = Guice.createInjector(new GuiceBasicModule());
        GuiceBasicService service = injector.getInstance(GuiceBasicService.class);
        service.print("Hello Guice");
    }
}
