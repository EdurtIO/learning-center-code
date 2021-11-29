package io.edurt.lc.guice;

public class GuiceAopMatcherServiceImpl
        implements GuiceAopMatcherService
{
    @Override
    public void println(String input)
    {
        System.out.println("Matcher input : " + input);
    }
}
