package io.edurt.lc.guice;

import com.google.inject.matcher.Matcher;

public class GuiceAopJavaServiceMatcher
        implements Matcher<Class<?>>
{
    @Override
    public boolean matches(Class<?> aClass)
    {
//        return aClass == GuiceAopMatcherService.class;
        return aClass == GuiceAopMatcherServiceImpl.class;
    }

    @Override
    public Matcher<Class<?>> and(Matcher<? super Class<?>> matcher)
    {
        return null;
    }

    @Override
    public Matcher<Class<?>> or(Matcher<? super Class<?>> matcher)
    {
        return null;
    }
}
