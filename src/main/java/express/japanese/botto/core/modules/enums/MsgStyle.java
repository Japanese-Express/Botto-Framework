package express.japanese.botto.core.modules.enums;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;

public enum MsgStyle {
    CURRENT_CHANNEL, DMUser, None;
    public TextChannel getChannelFromStyle(TextChannel channel, User user) {
        switch(this) {
            case DMUser:
                return (TextChannel) user.openPrivateChannel().complete();
            case CURRENT_CHANNEL:
                return channel;
            default:
                return null;
        }
    }
}
