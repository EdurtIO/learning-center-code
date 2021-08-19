package io.edurt.lc.guice;

import com.google.inject.Guice;
import com.google.inject.Inject;

public class TestGuiceNonStatic
{
    @Inject
    private GuiceConstructorService service;

    public static void main(String[] args)
    {
        TestGuiceNonStatic applicationBinder = new TestGuiceNonStatic();
        Guice.createInjector(binder -> binder.requestInjection(applicationBinder));
        applicationBinder.service.print("Non Static");
    }
}
