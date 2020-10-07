package express.japanese.botto.core.modules.module.events;

import express.japanese.botto.core.modules.module.events.reactions.*;

import java.util.ArrayList;
import java.util.List;

public class BotEventType <T> {
    private static List<BotEventType<?>> eventList = new ArrayList<>();

    public static BotEventType<MessageEvent> USER_MESSAGE = new BotEventType<>(MessageEvent.class);
    public static BotEventType<ReactionAddEvent> REACTION_ADD = new BotEventType<>(ReactionAddEvent.class);
    public static BotEventType<ReactionRemoveEvent> REACTION_REMOVE = new BotEventType<>(ReactionRemoveEvent.class);

    private Class<T> classType;
    BotEventType(Class<T> type) {
        this.classType = type;
        eventList.add(this);
    }

    public Class<T> getClassType() {
        return classType;
    }

    public static List<BotEventType<?>> values() {
        return eventList;
    }
}
