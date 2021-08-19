package io.edurt.lc.guice;

import com.google.inject.Inject;

public class TestGuiceConstructorNo
{
    @Inject
    private GuiceConstructorService service;

    public GuiceConstructorService getService()
    {
        return service;
    }

    public static void main(String[] args)
    {
        TestGuiceConstructorNo test = new TestGuiceConstructorNo();
        test.getService().print("Test Case 1");
    }
}
