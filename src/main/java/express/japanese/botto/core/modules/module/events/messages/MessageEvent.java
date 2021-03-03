package express.japanese.botto.core.modules.module.events.messages;

import express.japanese.botto.core.modules.module.events.BotEvent;
import express.japanese.botto.core.modules.module.events.BotEventType;
import net.dv8tion.jda.api.entities.Message;
import org.jetbrains.annotations.NotNull;

public interface MessageEvent extends BotEvent {
    @Override
    default BotEventType<?> getEventType() {
        return BotEventType.USER_MESSAGE;
    }
    Message getMessage();
}
