package io.edurt.lc.guice;

public class GuiceJavaService
        implements GuiceService
{
    @Override
    public void println(String input)
    {
        System.out.println(String.format("Java %s", input));
    }
}
