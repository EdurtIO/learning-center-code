package io.edurt.lc.guice;

import com.google.inject.ImplementedBy;

@ImplementedBy(value = PrintlnServiceImpl.class)
public interface PrintlnService
{
    void println(String input);
}
