package express.japanese.botto.core.modules;

import express.japanese.botto.BotController;
import express.japanese.botto.Botto;
import express.japanese.botto.core.modules.interfaces.annotations.ICooldown;
import express.japanese.botto.core.modules.interfaces.annotations.IModule;
import express.japanese.botto.core.modules.interfaces.annotations.IMsgErr;
import express.japanese.botto.core.modules.interfaces.annotations.IPermissions;
import express.japanese.botto.core.modules.module.Module;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public abstract class AbstractListener extends ListenerAdapter {
    public abstract void runModule(String cmd, String[] args, Message msg, Module module, IModule annotation);
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        super.onMessageReceived(event);
    }

    // this is kind of dumb, but whatever
    private static final Map<String/*userId*/, Long> cooldownMap = new HashMap<>();
    public boolean userIsInCooldown(Message msg, ICooldown iCooldown) {
        if(!iCooldown.useCooldown())
            return false;
        IMsgErr msgErr = iCooldown.cooldownMsg();
        String userId = msg.getAuthor().getId();
        long addUp = iCooldown.cooldownMillis();
        if(cooldownMap.containsKey(userId) && cooldownMap.get(userId) < (System.currentTimeMillis() + addUp)) {
            if(msgErr.isBeingUsed()) {
                TextChannel channel = msgErr.msgStyle().getChannelFromStyle(
                        (TextChannel)msg.getChannel(), msg.getAuthor());
                if(channel != null && channel.canTalk())
                    channel.sendMessage(msgErr.msg()).queue();
                if(msgErr.showInConsole())
                    System.out.println(msgErr.msg());
            }
            cooldownMap.remove(userId);
            cooldownMap.put(userId, System.currentTimeMillis());
            return true;
        }
        cooldownMap.put(userId, System.currentTimeMillis());
        return false;
    }

    public boolean channelIsCorrectFor(Message msg, ChannelType[] channelTypes) {
        if (channelTypes[0] != ChannelType.UNKNOWN) {
            for (ChannelType channelType : channelTypes) {
                if (msg.getChannelType() != channelType)
                    continue;
                return true;
            }
        }
        return false;
    }

    public boolean userHasPermissionsFor(TextChannel channel, Guild guild, User user, IPermissions iPermissions) {
        if(iPermissions.usePermissions())
            return permissionCheckFor(channel, guild, user, iPermissions);
        return true;
    }
    public boolean userHasPermissionsFor(Message msg, IPermissions iPermissions) {
        if(iPermissions.usePermissions() && msg.getChannelType() == ChannelType.TEXT) {
            return permissionCheckFor(
                    (TextChannel) msg.getChannel(),
                    msg.getGuild(), msg.getAuthor(), iPermissions);
        }
        return true;
    }
    private boolean permissionCheckFor(TextChannel textChannel, Guild guild, User user, IPermissions iPermissions) {
        Permission[] permissions = iPermissions.permissionsToUse();
        for(Permission perm : permissions) {
            Member member = guild.getMember(user);
            if (member != null && member.getPermissions().contains(perm))
                continue;
            IMsgErr iMsgErr = iPermissions.permErrorMsg();
            if (iMsgErr.isBeingUsed()) {
                TextChannel channel = iMsgErr.msgStyle().getChannelFromStyle(textChannel, user);
                if (channel != null && channel.canTalk())
                    channel.sendMessage(iMsgErr.msg()).queue();
                if (iMsgErr.showInConsole())
                    System.out.println(iMsgErr.msg());
                return false;
            }
        }
        return true;
    }

    public boolean isAnnotationOwnerOnly(String userId, IModule annotation, BotController botController) {
        return annotation.ownerOnly() && !botController.getOwnerId().equals(userId);
    }
}
