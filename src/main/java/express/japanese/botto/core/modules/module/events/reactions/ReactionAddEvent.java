package express.japanese.botto.core.modules.module.events.reactions;

import express.japanese.botto.core.modules.module.events.BotEventType;

public interface ReactionAddEvent extends ReactionEvent {
    default BotEventType<?> getEventType() {
        return BotEventType.REACTION_ADD;
    }
}
