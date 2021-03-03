package express.japanese.botto;

import express.japanese.botto.core.modules.interfaces.ToBeRemoved;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class Botto {
    private static List<BotController> bots = new ArrayList<>();

    public void main() {
        StaticConfig.debug = true;
        StaticConfig.willPassWithoutToken = true;
    }

    @NotNull
    public static BotBuilder initialize() {
        //Version.CompareVersion();
        /*try {
            JDABuilder builder = JDABuilder.createDefault(Config.getToken());
            builder.addEventListeners(listener);
            if(Config.shardId != -1 && Config.shardTotal != -1)
                builder.useSharding(Config.shardId, Config.shardTotal);
            Config.jda = builder.build();
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                if (listener != null)
                    listener.onClose();
                Config.jda.getPresence().setStatus(OnlineStatus.OFFLINE);
                Config.jda.shutdownNow();
            }));
            if(listener != null)
                listener.onLoad();
            Botto.addModule(VersionCmd.class);
            new Voicing().setup();
            return BotBuilder.createBuilder();
        } catch (LoginException e) {
            e.printStackTrace();
            return null;
        }
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                if(listener == null)
                    listener = BotCmdListener.class;
                BotCmdListener listener = createListenerInstance();
            }
        }, 5000);*/
        /*try {
            JDABuilder builder = JDABuilder.createDefault(Config.getToken());
            builder.addEventListeners(listener);
            if(Config.shardId != -1 && Config.shardTotal != -1)
                builder.useSharding(Config.shardId, Config.shardTotal);
            Config.jda = builder.build();
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                if (listener != null)
                    listener.onClose();
                Config.jda.getPresence().setStatus(OnlineStatus.OFFLINE);
                Config.jda.shutdownNow();
            }));
            if(listener != null)
                listener.onLoad();
            Botto.addModule(VersionCmd.class);
            new Voicing().setup();*/
            return BotBuilder.createBuilder();
        /*} catch (LoginException e) {
            e.printStackTrace();
            return null;
        }*/
    }

    @Deprecated
    @ToBeRemoved(value = "Moved to BotBuilder")
    public static void setOwnerId(String ownerId) {
        StaticConfig.ownerId = ownerId;
    }
    @Deprecated
    @ToBeRemoved(value = "Moved to BotBuilder")
    public static String getOwnerId() {
        return StaticConfig.ownerId;
    }

    static {
        initialize();
    }
}

