package io.edurt.lc.guice;

public class GuiceConstructorTwoServiceImpl
        implements GuiceConstructorTwoService
{
    @Override
    public void print()
    {
        System.out.println(String.format("Hello Guice Binder For Constructor Two"));
    }
}
