package express.japanese.botto.core.modules.module.events.messages;

import net.dv8tion.jda.api.entities.Guild;

public interface MessageGuildEvent extends MessageEvent {
    Guild getGuild();
}
