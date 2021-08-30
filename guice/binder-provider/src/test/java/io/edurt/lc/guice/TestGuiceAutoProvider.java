package io.edurt.lc.guice;

import com.google.inject.Guice;
import com.google.inject.Inject;

public class TestGuiceAutoProvider
{
    @Inject
    private GuiceAutoProviderService autoProviderService;

    public static void main(String[] args)
    {
        TestGuiceAutoProvider application = Guice.createInjector().getInstance(TestGuiceAutoProvider.class);
        application.autoProviderService.println("Hello Auto Guice Provider");
    }
}
