package io.edurt.lc.guice;

import com.google.inject.ImplementedBy;

@ImplementedBy(value = GuiceAopMatcherServiceImpl.class)
public interface GuiceAopMatcherService
{
    void println(String input);
}
