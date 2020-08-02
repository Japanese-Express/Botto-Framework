package express.japanese.botto.core.modules.enums;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

public enum MsgStyle {
    CURRENT_CHANNEL, DMUser, None;
    public TextChannel getChannelFromStyle(Message ofMsg) {
        switch(this) {
            case DMUser:
                return (TextChannel) ofMsg.getAuthor().openPrivateChannel().complete();
            case CURRENT_CHANNEL:
                return (TextChannel) ofMsg.getChannel();
            default:
                return null;
        }
    }
}
