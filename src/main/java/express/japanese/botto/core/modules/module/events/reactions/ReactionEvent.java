package express.japanese.botto.core.modules.module.events.reactions;

import express.japanese.botto.core.modules.module.events.BotEvent;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageReaction;

public interface ReactionEvent extends BotEvent {
    MessageReaction getReaction();
    Member getMember();
}
