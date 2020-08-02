package express.japanese.botto.core.modules.preInstalled;

import express.japanese.botto.BotController;
import express.japanese.botto.StaticConfig;
import express.japanese.botto.core.modules.AbstractListener;
import express.japanese.botto.core.modules.interfaces.Author;
import express.japanese.botto.core.modules.interfaces.IsDefault;
import express.japanese.botto.core.modules.interfaces.annotations.IModule;
import express.japanese.botto.core.modules.module.CmdModule;
import express.japanese.botto.core.modules.module.Module;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;

@IsDefault
@Author("cutezyash#7654")
public abstract class BotCmdListener extends AbstractListener {
    public void onLoad() {
        System.out.println("Bot loaded!");
    }
    public void onClose() {
        System.out.println("Shutting down!");
    }

    private BotController botController = null;
    public final void setBotController(BotController botController) {
        this.botController = botController;
    }

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        super.onMessageReceived(event);
        moduleCheck(event.getMessage());
    }

    public final boolean moduleCheck(@Nonnull Message msg, @Nonnull String... customPrefixes) {
        if (msg.getAuthor().isBot())
            return false;
        String prefix = StaticConfig.getPrefix();
        for(String pfx : customPrefixes) {
            if(msg.getContentRaw().startsWith(pfx)) {
                prefix = pfx;
                break;
            }
        }
        if(prefix == null)
            return false;
        if(!msg.getContentRaw().startsWith(prefix)) {
            boolean ret = false;
            for(Module module : botController.modulesTrackMessages) {
                if(!(module instanceof CmdModule))
                    continue;
                CmdModule cmdModule = (CmdModule) module;
                IModule inst = cmdModule.getModuleInterface();
                if(inst.serversById().length > 0) {
                    for (String sId : inst.serversById()) {
                        if (sId.equals(msg.getGuild().getId()))
                            ret = cmdModule.onUserMessage(msg.getContentRaw().split(" "), msg);
                    }
                } else {
                    ret = cmdModule.onUserMessage(msg.getContentRaw().split(" "), msg);
                }
            }
            return ret;
        }
        String content = msg.getContentRaw().substring(prefix.length());
        String[] args = null;
        if (content.contains(" ")) {
            String contentPrepare = content.substring(content.indexOf(" ") + 1);
            args = contentPrepare.split(" ");
        }
        String cmd = msg.getContentRaw().trim().toLowerCase().split(" ")[0].replace(prefix, "");
        Module module = botController.getModule(cmd);
        if (module != null) {
            IModule annotation = module.getModuleInterface();
            this.runModule(cmd, args, msg, module, annotation);
            return true;
        } /*else if (BotCore.getHelpInstanced() != null) {
            Object[] helpCmd = BotCore.getHelpInstanced();
            module = (Module)helpCmd[0];
            IModule annotation = (IModule)helpCmd[1];
            for (String ready : annotation.names()) {
                if (!ready.equals(cmd))
                    continue;
                this.runModule(cmd, args, msg, module, annotation);
                return true;
            }
        }*/
        return false;
    }

    @Override
    public void runModule(String cmd, String[] args, Message msg, Module module, IModule annotation) {
        if(this.isAnnotationOwnerOnly(msg.getAuthor().getId(), annotation))
            return;
        if(annotation.serversById().length > 0) {
            if(!msg.isFromGuild())
                return;
            boolean go = false;
            for(int i =0;i < annotation.serversById().length;i++) {
                if(msg.getGuild().getId().equals(annotation.serversById()[i])) {
                    go = true;
                    break;
                }
            }
            if(!go)
                return;
        }
        if(!channelIsCorrectFor(msg, annotation.channelTypes()))
            return;
        int argReq = annotation.requiredArgCount();
        if (argReq > 0) {
            if (annotation.argErrorMessage().trim().equalsIgnoreCase("")) {
                System.err.println(cmd + " does not have an arg error, and will not return");
            } else {
                if (args == null) {
                    msg.getChannel().sendMessage(annotation.argErrorMessage()).queue();
                    return;
                }
                if (args.length < argReq) {
                    msg.getChannel().sendMessage(annotation.argErrorMessage()).queue();
                    return;
                }
            }
        }
        if(this.userIsInCooldown(msg, annotation.cooldown()))
            return;
        if(!userHasPermissionsFor(msg, annotation.permissions()))
            return;
        module.run(cmd, args, msg);
    }
}

