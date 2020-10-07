package express.japanese.botto.core.modules.preInstalled;

import express.japanese.botto.core.modules.enums.BotCategory;
import express.japanese.botto.core.modules.enums.Language;
import express.japanese.botto.core.modules.interfaces.annotations.ILanguage;
import express.japanese.botto.core.modules.interfaces.annotations.IModule;
import express.japanese.botto.core.modules.module.CmdModule;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.Message;

import javax.annotation.Nonnull;

@IModule(
        names={"lang", "language", "げんご", "言語", "gengo", "japanese", "english"},
        tinyDescription = {
                @ILanguage(language = Language.ENGLISH,
                        value = "Switch the language of the bot, only for you.")},
        category= BotCategory.Bot,
        channelTypes = {
                ChannelType.TEXT, ChannelType.GROUP, ChannelType.PRIVATE
        })
public class CmdLang extends CmdModule {
    @Override
    public void run(String cmd, @Nonnull String[] args, Message msg) {
        if(cmd.equals("japanese")) {
            setLanguage(Language.JAPANESE, msg);
            return;
        } else if(cmd.equals("english")) {
            setLanguage(Language.ENGLISH, msg);
            return;
        }
        if(args.length < 1) {
            String pfx = getPrefix()[0];
            msg.getChannel().sendMessage(""+
                    "You need to say a language after the command\n" +
                    "`"+pfx+"lang [name]` OR `"+pfx+"japanese`").queue();
            return;
        }
        Language language = Language.valueOf(args[0].toLowerCase());
        if(language != null)
        switch(args[0].toLowerCase()) {
            case "english":
                setLanguage(Language.ENGLISH, msg);break;
            case "japanese":
                setLanguage(Language.JAPANESE, msg);break;
        }
    }

    private void setLanguage(Language lang, Message msg) {
        setUsedLanguageFor(msg.getAuthor(), lang);
        msg.getChannel().sendMessage(getLang(getUserLang(msg.getAuthor())).get("language.set")).queue();
    }
}
