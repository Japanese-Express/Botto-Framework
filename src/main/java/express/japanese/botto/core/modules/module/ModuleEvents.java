package express.japanese.botto.core.modules.module;

import express.japanese.botto.core.modules.module.events.MessageEvent;
import express.japanese.botto.core.modules.module.events.reactions.ReactionAddEvent;
import express.japanese.botto.core.modules.module.events.reactions.ReactionRemoveEvent;

public interface ModuleEvents {
    default boolean onReactionAdded(ReactionAddEvent event) {return true;}
    default boolean onReactionRemoved(ReactionRemoveEvent event) {return true;}
    default boolean onUserMessage(MessageEvent event){return true;}
}
