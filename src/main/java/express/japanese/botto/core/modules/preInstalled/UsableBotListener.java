package express.japanese.botto.core.modules.preInstalled;

import express.japanese.botto.BotController;
import express.japanese.botto.core.modules.AbstractListener;
import express.japanese.botto.core.modules.interfaces.Author;
import express.japanese.botto.core.modules.interfaces.IsDefault;
import express.japanese.botto.core.modules.interfaces.annotations.IModule;
import express.japanese.botto.core.modules.module.Module;
import express.japanese.botto.core.modules.module.events.BotEventType;
import express.japanese.botto.core.modules.module.events.EventUtil;
import express.japanese.botto.core.modules.module.events.MessageEvent;
import express.japanese.botto.core.modules.module.events.MethodInstance;
import express.japanese.botto.core.modules.module.events.reactions.ReactionAddEvent;
import express.japanese.botto.core.modules.module.events.reactions.ReactionRemoveEvent;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageReaction;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.ShutdownEvent;
import net.dv8tion.jda.api.events.channel.text.TextChannelCreateEvent;
import net.dv8tion.jda.api.events.channel.text.TextChannelDeleteEvent;
import net.dv8tion.jda.api.events.channel.text.update.TextChannelUpdateTopicEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceJoinEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceLeaveEvent;
import net.dv8tion.jda.api.events.message.MessageDeleteEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.MessageUpdateEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionRemoveEvent;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

@IsDefault
@Author("cutezyash#7654")
public abstract class UsableBotListener extends AbstractListener {
    private List<IModule> loadedModules = new ArrayList<>();
    private List<IModule> shutdownModules = new ArrayList<>();
    private BotController botController = null;
    public final void setBotController(BotController botController) {
        this.botController = botController;
    }

//region START/STOP
    @Override
    public void onReady(@Nonnull ReadyEvent event) {
        super.onReady(event);
        System.out.println("Loading...");
        // TODO: Bot load events
        System.out.println("Loading modules...");
        botController.moduleInterfaces.forEach((module, iModule) -> {
            if(loadedModules.contains(iModule))
                return;
            module.onReady();
            loadedModules.add(iModule);
        });
        System.out.println("Bot loaded!");
    }

    @Override
    public void onShutdown(@Nonnull ShutdownEvent event) {
        super.onShutdown(event);
        System.out.println("Shutting down...");
        // TODO: Bot shutdown events
        System.out.println("Shutting down modules...");
        botController.moduleInterfaces.forEach((module, iModule) -> {
            if(shutdownModules.contains(iModule))
                return;
            module.onClose();
            shutdownModules.add(iModule);
        });
        System.out.println("Exiting...");
    }
//endregion

//region EVENTS
/*  TODO:
       Update events with proper sub-event classes
       and modules*/
    <T> List<MethodInstance<T>> getEventList(BotEventType<T> type) {
        return EventUtil.getMethodsForEvent(type);
    }
//region CHANNEL_EVENTS
    @Override
    public void onTextChannelCreate(@Nonnull TextChannelCreateEvent event) {
        super.onTextChannelCreate(event);
    }
    @Override
    public void onTextChannelUpdateTopic(@Nonnull TextChannelUpdateTopicEvent event) {
        super.onTextChannelUpdateTopic(event);
    }
    @Override
    public void onTextChannelDelete(@Nonnull TextChannelDeleteEvent event) {
        super.onTextChannelDelete(event);
    }
    //endregion
//region VOICE_EVENTS
    @Override
    public void onGuildVoiceJoin(@Nonnull GuildVoiceJoinEvent event) {
        super.onGuildVoiceJoin(event);
    }
    @Override
    public void onGuildVoiceLeave(@Nonnull GuildVoiceLeaveEvent event) {
        super.onGuildVoiceLeave(event);
    }
//endregion
//region MESSAGE_EVENTS
    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        super.onMessageReceived(event);
        Message msg = event.getMessage();
        for(MethodInstance<MessageEvent> method : getEventList(BotEventType.USER_MESSAGE)) {
            method.invoke(new MessageEvent() {
                @Override
                public Message getMessage() {
                    return msg;
                }
            });
        }
        moduleCheck(event.getMessage());
    }
    @Override
    public void onMessageUpdate(@Nonnull MessageUpdateEvent event) {
        super.onMessageUpdate(event);
    }
    @Override
    public void onMessageDelete(@Nonnull MessageDeleteEvent event) {
        super.onMessageDelete(event);
    }
//endregion
//region REACTION_EVENTS
    @Override
    public void onMessageReactionAdd(@NotNull MessageReactionAddEvent event) {
        event.getReaction();
        for(MethodInstance<ReactionAddEvent> method : getEventList(BotEventType.REACTION_ADD)) {
            method.invoke(new ReactionAddEvent() {
                @Override
                public Message getMessage() {
                    return event.getChannel().retrieveMessageById(event.getMessageId()).complete();
                }
                @Override
                public Member getMember() {
                    return event.getMember();
                }
                @Override
                public MessageReaction getReaction() {
                    return event.getReaction();
                }
            });
        }
    }
    @Override
    public void onMessageReactionRemove(@Nonnull MessageReactionRemoveEvent event) {
        for(MethodInstance<?> method : getEventList(BotEventType.REACTION_REMOVE)) {
            MethodInstance<ReactionRemoveEvent> methodInstance = (MethodInstance<ReactionRemoveEvent>) method;
            methodInstance.invoke(new ReactionRemoveEvent() {
                @Override
                public Message getMessage() {
                    return event.getChannel().retrieveMessageById(event.getMessageId()).complete();
                }
                @Override
                public Member getMember() {
                    return event.getMember();
                }
                @Override
                public MessageReaction getReaction() {
                    return event.getReaction();
                }
            });
        }
    }
//endregion
//endregion

//region MODULE_RUN
    public final boolean moduleCheck(@Nonnull final Message msg) {
        if (msg.getAuthor().isBot())
            return false;
        String[] prefixes = botController.getPrefix();
        if(prefixes == null || prefixes.length < 1) {
            System.err.println("PREFIX IS INVALID FOR BOT");
            return false;
        }
        String prefix = "";
        for(String pfx : prefixes) {
            if(msg.getContentRaw().startsWith(pfx))
                prefix = pfx;
        }
        if(prefix.isEmpty())
            return false;
        String content = msg.getContentRaw().substring(prefix.length());
        String[] args;
        if(content.contains(" ")) {
            String contentPrepare = content.substring(content.indexOf(" ") + 1);
            args = contentPrepare.split(" ");
        } else
            args = new String[0];
        String cmd = msg.getContentRaw().trim().toLowerCase().split(" ")[0].replace(prefix, "");
        Module module = botController.getModule(cmd);
        if (module != null) {
            IModule annotation = module.getModuleInterface();
            this.runModule(cmd, args, msg, module, annotation);
            return true;
        }
        /*else if (BotCore.getHelpInstanced() != null) {
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
        try {
            module.run(cmd, args, msg);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
//endregion
}

