package io.edurt.lc.guice;

import com.google.inject.ImplementedBy;

@ImplementedBy(GuiceConstructorTwoServiceImpl.class)
public interface GuiceConstructorTwoService
{
    void print();
}
