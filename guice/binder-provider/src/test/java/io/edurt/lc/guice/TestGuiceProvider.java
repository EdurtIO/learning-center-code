package io.edurt.lc.guice;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;

public class TestGuiceProvider
{
    @Inject
    private GuiceProviderService providerService;

    public static void main(String[] args)
    {
        Injector injector = Guice.createInjector(binder -> binder.bind(GuiceProviderService.class).toProvider(GuiceProvider.class));
        TestGuiceProvider application = injector.getInstance(TestGuiceProvider.class);
        application.providerService.println("Hello Guice Provider");
    }
}
