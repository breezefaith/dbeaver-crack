package com.dbeaver.agent.intercepter;

import net.bytebuddy.implementation.bind.annotation.Origin;
import net.bytebuddy.implementation.bind.annotation.RuntimeType;
import net.bytebuddy.implementation.bind.annotation.SuperCall;

import java.lang.reflect.Method;
import java.util.concurrent.Callable;

public class GenericInterceptor {
    private final Object returnValue;

    public GenericInterceptor(Object returnValue) {
        this.returnValue = returnValue;
    }

    @RuntimeType
    public Object intercept(@Origin Method method, @SuperCall Callable<?> callable) {
        return this.returnValue;
    }
}
