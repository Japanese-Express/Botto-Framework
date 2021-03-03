package express.japanese.botto.core.modules.module.events;

import express.japanese.botto.core.modules.module.Module;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class MethodInstance<T> {
    private final T type;
    private final Module clazz;
    private final Method method;
    MethodInstance(Module clazz, Method method, T type) {
        this.type = type;
        this.clazz = clazz;
        this.method = method;
    }

    public Module getOriginModule() {
        return clazz;
    }

    public void invoke(Object obj) {
        try {
            method.invoke(clazz, obj);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}
