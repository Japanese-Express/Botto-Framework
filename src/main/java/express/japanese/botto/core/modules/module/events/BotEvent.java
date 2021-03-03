package express.japanese.botto.core.modules.module.events;

import net.dv8tion.jda.api.entities.Message;

import javax.annotation.Nonnull;

public interface BotEvent {
    default BotEventType<?> getEventType() {
        return BotEventType.UNKNOWN;
    }

    @Nonnull
    default String getEventName() {
        return getClass().getSimpleName();
    }

    Message getMessage();
}
