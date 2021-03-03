package express.japanese.botto.core.modules.module;

import express.japanese.botto.core.modules.interfaces.annotations.Track;
import express.japanese.botto.core.modules.module.events.messages.*;
import express.japanese.botto.core.modules.module.events.reactions.*;

public interface ModuleEvents {
    default boolean onReactionAdded(ReactionAddEvent event) {return true;}
    default boolean onReactionRemoved(ReactionRemoveEvent event) {return true;}
    default boolean onUserMessageGuild(MessageGuildEvent event){return true;}
    default boolean onUserMessagePrivate(MessagePrivateEvent event){return true;}
}
