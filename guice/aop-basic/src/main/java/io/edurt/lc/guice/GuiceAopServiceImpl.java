package io.edurt.lc.guice;

public class GuiceAopServiceImpl
        implements GuiceAopService
{
    @Override
    public void println(String input)
    {
        System.out.println("Guice AOP Service : " + input);
    }
}
