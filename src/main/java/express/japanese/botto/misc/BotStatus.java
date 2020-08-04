package express.japanese.botto.misc;

import express.japanese.botto.StaticConfig;
import express.japanese.botto.core.modules.interfaces.ToBeRemoved;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Activity;

@Deprecated
@ToBeRemoved("This is really bad, and really old code")
class BotStatus {
    @Deprecated
    public static void SetStatus(JDA jda, Activity.ActivityType type, String status) {
        if ((status = status.replace("%guilds", String.valueOf(jda.getGuilds().size()))).contains("%prefix") && StaticConfig.contains("prefix")) {
            if(StaticConfig.contains("prefix"))
                status = status.replace("%prefix", StaticConfig.getStr("prefix"));
        }
        jda.getPresence().setActivity(Activity.of(type, status));
        System.out.println("Set bot status to " + status);
    }
}

