package express.japanese.botto.core.modules.enums;

import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;

public enum MsgStyle {
    CURRENT_CHANNEL, PRIVATE_CHANNEL, None;
    public TextChannel getChannelFromStyle(TextChannel channel, User user) {
        switch(this) {
            case PRIVATE_CHANNEL:
                return (TextChannel) user.openPrivateChannel().complete();
            case CURRENT_CHANNEL:
                return channel;
            default:
                return null;
        }
    }
}
