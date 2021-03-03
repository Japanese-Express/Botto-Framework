package express.japanese.botto.core.modules.module.events;

import express.japanese.botto.core.modules.module.events.messages.*;
import express.japanese.botto.core.modules.module.events.reactions.*;

import java.util.ArrayList;
import java.util.List;

public class BotEventType <T> {
    private static List<BotEventType<?>> eventList = new ArrayList<>();

    public static BotEventType<MessageEvent> USER_MESSAGE = new BotEventType<>(MessageEvent.class);
    public static BotEventType<MessageGuildEvent> USER_MESSAGE_GUILD = new BotEventType<>(MessageGuildEvent.class);
    public static BotEventType<MessagePrivateEvent> USER_MESSAGE_PRIVATE = new BotEventType<>(MessagePrivateEvent.class);
    public static BotEventType<ReactionAddEvent> REACTION_ADD = new BotEventType<>(ReactionAddEvent.class);
    public static BotEventType<ReactionRemoveEvent> REACTION_REMOVE = new BotEventType<>(ReactionRemoveEvent.class);
    public static BotEventType<?> UNKNOWN = new BotEventType<>();

    private boolean isUnknown = false;
    private Class<T> classType;
    BotEventType(Class<T> type) {
        this.classType = type;
        eventList.add(this);
    }
    BotEventType() {
        this.isUnknown = true;
    }

    public String getEventName() {
        return classType.getName();
    }

    public boolean isUnknown() {
        return isUnknown;
    }

    public Class<T> getClassType() {
        return classType;
    }

    public static List<BotEventType<?>> values() {
        return eventList;
    }
}
