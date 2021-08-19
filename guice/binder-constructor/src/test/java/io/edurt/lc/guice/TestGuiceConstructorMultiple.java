package io.edurt.lc.guice;

import com.google.inject.Guice;
import com.google.inject.Inject;

public class TestGuiceConstructorMultiple
{
    private GuiceConstructorService service;
    private GuiceConstructorTwoService twoService;

    public GuiceConstructorService getService()
    {
        return service;
    }

    public void setService(GuiceConstructorService service)
    {
        this.service = service;
    }

    public GuiceConstructorTwoService getTwoService()
    {
        return twoService;
    }

    public void setTwoService(GuiceConstructorTwoService twoService)
    {
        this.twoService = twoService;
    }

    @Inject
    public TestGuiceConstructorMultiple(GuiceConstructorService service, GuiceConstructorTwoService twoService)
    {
        this.service = service;
        this.twoService = twoService;
    }

    public static void main(String[] args)
    {
        TestGuiceConstructorMultiple multiple = Guice.createInjector().getInstance(TestGuiceConstructorMultiple.class);
        multiple.getService().print("One");
        multiple.getTwoService().print();
    }
}
