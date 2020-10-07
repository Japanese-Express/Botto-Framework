package express.japanese.botto.core.modules.module.events;

import net.dv8tion.jda.api.entities.Message;
import org.jetbrains.annotations.NotNull;

public interface MessageEvent extends BotEvent {
    Message getMessage();
}
