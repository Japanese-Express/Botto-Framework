package express.japanese.botto.core.modules.module;

import express.japanese.botto.core.modules.enums.BotCategory;
import express.japanese.botto.core.modules.enums.Language;
import express.japanese.botto.core.modules.interfaces.annotations.ILanguage;
import express.japanese.botto.core.modules.interfaces.annotations.IModule;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;

import javax.annotation.Nonnull;
import java.util.Timer;
import java.util.TimerTask;

/**
 * <h1>Module for Commands</h1>
 * <p>This module is used specifically for commands.
 * However, it can also be used for various other
 * things such as tracking user messages with
 * the setting enabled inside of an interface module</p>
 */
@IModule(
        names = {}, tinyDescription = {@ILanguage(language = Language.ENGLISH, value = "")},
        category = BotCategory.Unknown, channelTypes = ChannelType.UNKNOWN)
public abstract class CmdModule extends Module {
    /*public CmdModule(IModule moduleInstance) {
        super(moduleInstance);
        this.moduleInstance = moduleInstance;
    }*/

    /**
     * Runs this module
     * @param cmd Name of command that was ran
     * @param args Arguments in message
     * @param msg Message artifact
     */
    public abstract void run(String cmd, @Nonnull String[] args, Message msg);

    /**
     * Runs the help command for the chosen module
     * @param forModuleName literal module name
     * @see IModule
     */
    public final void runHelpCmd(Message msg, String forModuleName) {
        CmdModule helpModule = (CmdModule) this.botControllerInst.getModule("help");
        if(helpModule == null)
            return;
        helpModule.run("cmd", new String[]{forModuleName}, msg);
    }

    /**
     * Check if self-bot has permission for channel
     * @param channel Channel for permission
     * @param permission Permission to check
     * @return successful
     */
    public final boolean botHasPerm(GuildChannel channel, Permission permission) {
        User user = getJDA().getSelfUser();
        Member member = channel.getGuild().getMember(user);
        if(member == null)
            return false;
        return member.hasPermission(channel, permission);
    }

    private static final Timer timer = new Timer();
    /**
     * Deletes a certain message after x amount of MS
     * @param msg Message artifact
     * @param ms Milliseconds
     */
    public final void deleteMessageAfter(Message msg, int ms) {
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                msg.delete().queue();
            }
        }, ms);
    }
}

