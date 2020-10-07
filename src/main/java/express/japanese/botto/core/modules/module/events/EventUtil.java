package express.japanese.botto.core.modules.module.events;

import express.japanese.botto.core.modules.interfaces.Author;
import express.japanese.botto.core.modules.module.Module;
import express.japanese.botto.core.modules.module.events.reactions.ReactionAddEvent;
import express.japanese.botto.core.modules.module.events.reactions.ReactionRemoveEvent;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Author("cutezyash#7654")
public class EventUtil {
    static Map<BotEventType<?>, List<MethodInstance<?>>> eventTypeMap = new HashMap<>();

    public static void toEventForAnnotation(Module instance, Method method) {
        Parameter[] params = method.getParameters();
        if(params.length < 1)
            return;
        Class<?> paramType = params[0].getType();
        for(BotEventType<?> type : BotEventType.values()) {
            if(isEventApplicable(type.getClassType(), paramType))
                eventTypeMap.get(BotEventType.USER_MESSAGE)
                        .add(new MethodInstance<MessageEvent>(instance, method));
        }
    }

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
