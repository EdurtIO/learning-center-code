package io.edurt.lc.guice;

import com.google.inject.Key;
import com.google.inject.Provider;
import com.google.inject.Scope;

public class GuiceCustomScopeImpl
        implements Scope
{
    ThreadLocal<Object> threadLocal = new ThreadLocal<>();

    @Override
    public <T> Provider<T> scope(Key<T> key, Provider<T> unscoped)
    {
        return () -> {
            T instance = (T) threadLocal.get();
            if (instance == null) {
                instance = unscoped.get();
                threadLocal.set(instance);
            }
            return instance;
        };
    }
}
