package express.japanese.botto.core.modules.module.events;

import express.japanese.botto.core.modules.module.Module;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class MethodInstance<T> {
    private Module clazz;
    private Method method;
    MethodInstance(Module clazz, Method method) {
        this.clazz = clazz;
        this.method = method;
    }
    public void invoke(T t) {
        try {
            method.invoke(clazz, t);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}
