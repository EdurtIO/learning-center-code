package io.edurt.lc.guice;

import com.google.inject.ProvidedBy;

@ProvidedBy(value = GuiceAutoProvider.class)
public interface GuiceAutoProviderService
{
    void println(String input);
}
