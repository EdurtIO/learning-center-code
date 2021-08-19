package io.edurt.lc.guice;

import com.google.inject.Guice;
import com.google.inject.Inject;

public class TestGuiceConstructor
{
    private GuiceConstructorService service;

    @Inject
    public TestGuiceConstructor(GuiceConstructorService service)
    {
        this.service = service;
    }

    public GuiceConstructorService getService()
    {
        return service;
    }

    public static void main(String[] args)
    {
        TestGuiceConstructor test = Guice.createInjector().getInstance(TestGuiceConstructor.class);
        test.getService().print("Test Case 1");
    }
}
