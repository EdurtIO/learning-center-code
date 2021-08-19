package io.edurt.lc.guice;

public class GuiceConstructorServiceImpl
        implements GuiceConstructorService
{
    @Override
    public void print(String source)
    {
        System.out.println(String.format("Hello Guice Binder For Constructor, %s", source));
    }
}
