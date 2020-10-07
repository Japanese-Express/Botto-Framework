package pink.cutezy.example.commands;

import express.japanese.botto.core.modules.enums.BotCategory;
import express.japanese.botto.core.modules.enums.Language;
import express.japanese.botto.core.modules.interfaces.annotations.ILanguage;
import express.japanese.botto.core.modules.interfaces.annotations.IModule;
import express.japanese.botto.core.modules.module.CmdModule;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.Message;
import org.jetbrains.annotations.NotNull;

@IModule(
        names = {"google", "g"},
        tinyDescription = {
                @ILanguage(language = Language.ENGLISH,
                        value = "Returns a google link for what you requested")},
        category = BotCategory.Utility,
        channelTypes = { ChannelType.TEXT, ChannelType.PRIVATE, ChannelType.GROUP }
)
public class CmdGoogle extends CmdModule {
    @Override
    public void run(String cmd, @NotNull String[] args, Message msg) {
        StringBuilder builder = new StringBuilder();
        int i = 0;
        for(String string : args) {
            builder.append(string);
            if(i < args.length-1)
                builder.append("%20");
            i++;
        }
        msg.getChannel().sendMessage("https://www.google.com/search?q=" + builder.toString()).queue();
    }
}
