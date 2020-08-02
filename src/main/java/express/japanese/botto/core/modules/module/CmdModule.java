package express.japanese.botto.core.modules.module;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import express.japanese.botto.core.modules.enums.Category;
import express.japanese.botto.core.modules.enums.ModuleError;
import express.japanese.botto.core.modules.interfaces.annotations.IModule;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

@IModule(
        names = {}, tinyDescription = "", category = Category.Unknown, channelTypes = ChannelType.UNKNOWN)
public abstract class CmdModule extends Module {
    private final List<ModuleError> errors = new ArrayList<>();

    /*public CmdModule(IModule moduleInstance) {
        super(moduleInstance);
        this.moduleInstance = moduleInstance;
    }*/

    public abstract void run(String cmd, String[] args, Message msg);

    public void triggerError(ModuleError error) {
        this.errors.add(error);
    }

    public boolean onUserMessage(String[] args, Message msg) { return false; }

    /**
     * Runs the help command for the chosen module
     * @param forModuleName literal module name
     * @see IModule
     */
    public void runHelpCmd(Message msg, String forModuleName) {
        CmdModule helpModule = (CmdModule) this.botControllerInst.getModule("help");
        if(helpModule == null)
            return;
        helpModule.run("cmd", new String[]{forModuleName}, msg);
    }

    public final boolean botHasPerm(GuildChannel channel, Permission permission) {
        User user = getJDA().getSelfUser();
        Member member = channel.getGuild().getMember(user);
        if(member == null)
            return false;
        return member.hasPermission(channel, permission);
    }

    private static final Timer timer = new Timer();
    public final void deleteMessageAfter(Message msg, int ms, boolean isSeconds) {
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                msg.delete().queue();
            }
        }, isSeconds ? ms*1000 : ms);
    }
}

