package express.japanese.botto.core.modules.module.events;

import express.japanese.botto.core.modules.interfaces.Author;
import express.japanese.botto.core.modules.module.Module;
import express.japanese.botto.core.modules.module.events.messages.MessageEvent;
import express.japanese.botto.core.modules.module.events.messages.MessageGuildEvent;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Author("cutezyash#7654")
public class EventUtil {
    static Map<BotEventType<?>, List<MethodInstance<?>>> eventTypeMap = new HashMap<>();

    public static void toEventForAnnotation(Module module, Method method) {
        Parameter[] params = method.getParameters();
        if(params.length < 1)
            return;
        Class<?> paramType = params[0].getType();
        for(BotEventType<?> eventType : BotEventType.values()) {
            Class<?> eventClass = eventType.getClassType();
            if(isEventApplicable(eventClass, paramType)) {
                System.out.println("event " + eventType.getEventName() + ": "  + module.getModuleInterface().names()[0]);
                eventTypeMap.get(eventType)
                        .add(new MethodInstance<>(module, method, eventType.getClassType()));

            }
        }
    }

    /*private void add(BotEventType<?> eventType, Module module, Method method) {
        eventTypeMap.get(eventType).add(new MethodInstance<MessageEvent>(module, method));
    }*/

    public static <T> List<MethodInstance<T>> getMethodsForEvent(BotEventType<T> eventType) {
        List<MethodInstance<T>> eventList = new ArrayList<>();
        for(MethodInstance<?> methodInstance : eventTypeMap.get(eventType)) {
            MethodInstance<T> instance = (MethodInstance<T>) methodInstance;
            eventList.add(instance);
        }
        return eventList;
    }

    public static boolean isEventApplicable(Class<?> original, Class<?> substitute) {
        return original.equals(substitute);
    }

    static {
        for(BotEventType<?> type : BotEventType.values()) {
            eventTypeMap.put(type, new ArrayList<>());
        }
    }
}
