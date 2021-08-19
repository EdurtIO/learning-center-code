package io.edurt.lc.guice;

import com.google.inject.ImplementedBy;

@ImplementedBy(GuiceConstructorServiceImpl.class)
public interface GuiceConstructorService
{
    void print(String source);
}
