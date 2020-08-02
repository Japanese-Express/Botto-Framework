package express.japanese.botto.core.modules.preInstalled;

import express.japanese.botto.StaticConfig;
import express.japanese.botto.Version;
import express.japanese.botto.core.modules.module.CmdModule;
import express.japanese.botto.core.modules.interfaces.Author;
import express.japanese.botto.core.modules.interfaces.annotations.IModule;
import express.japanese.botto.core.modules.enums.Category;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.Message;
import express.japanese.botto.core.modules.interfaces.IsDefault;
import express.japanese.botto.misc.RichEmbed;

import java.awt.*;

@IModule(
        names={"ver", "version", "versioninfo", "vinfo", "self"},
        tinyDescription = "Gets self information",
        category= Category.Bot,
        channelTypes={
                ChannelType.TEXT, ChannelType.PRIVATE, ChannelType.GROUP
        })
@IsDefault
@Author("cutezyash#7654")
public class VersionCmd extends CmdModule {
    @Override
    public void run(String cmd, String[] args, Message msg) {
        int[] ver = Version.current;
        String fullVer = ver[0] + "." + ver[1] + "." + ver[2];
        RichEmbed richEmbed = new RichEmbed()
                .setDescription("**ボット (Botto) Framework, <https://japanese.express>**")
                .addField("Current Version", fullVer, true)
                //.addField("Newest Version", Version.latest, true)
                //.addField("Loaded modules", Botto.getLoadedModuleSize() + " modules", true)
                //.addField("Failed modules", Botto.getUnloadedModuleSize()+ " modules", true)
                .addField("What is Japanese.express", ""+
                                "<http://japanese.express> is a website, with the intention of creating an enjoyable " +
                                "environment to find everything Japanese, with Japanese culture Discord servers " +
                                "and more! If you'd like to see more information on how to use this bot, please " +
                                "use `"+ StaticConfig.getStr("prefix") +"help`",
                        false)
                .addField("In collaboration with:", "**The OldHaven Network <https://www.oldhaven.net>**", false)
                .setImage("https://www.oldhaven.net/assets/img/Vector3.png")
                .setColor(Color.PINK)
                .setFooterAsCurrentTime();
        msg.getChannel().sendMessage(richEmbed.build()).queue();
    }
}

