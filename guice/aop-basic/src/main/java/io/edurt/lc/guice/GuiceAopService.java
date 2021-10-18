package io.edurt.lc.guice;

import com.google.inject.ImplementedBy;

@ImplementedBy(value = GuiceAopServiceImpl.class)
public interface GuiceAopService
{
    void println(String input);
}
