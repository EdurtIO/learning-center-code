package io.edurt.lc.guice;

import com.google.inject.Guice;
import com.google.inject.Inject;

public class TestGuiceStatic
{
    @Inject
    private static GuiceConstructorService service;

    public static void main(String[] args)
    {
        Guice.createInjector(binder -> binder.requestStaticInjection(TestGuiceStatic.class));
        TestGuiceStatic.service.print("Static");
    }
}
