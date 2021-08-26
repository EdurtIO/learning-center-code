package io.edurt.lc.guice;

import com.google.inject.Guice;
import com.google.inject.Inject;
import io.edurt.lc.guice.annotation.Java;
import io.edurt.lc.guice.annotation.Python;

public class TestGuiceMultiple
{
    @Inject
    @Java
    public GuiceService java;

    @Inject
    @Python
    public GuiceService python;

    public static void main(String[] args)
    {
        TestGuiceMultiple application = Guice.createInjector(binder -> {
            binder.bind(GuiceService.class).annotatedWith(Java.class).to(GuiceJavaService.class);
            binder.bind(GuiceService.class).annotatedWith(Python.class).to(GuicePythonService.class);
        }).getInstance(TestGuiceMultiple.class);
        application.java.println("Hello Java Implement");
        application.python.println("Hello Python Implement");
    }
}
