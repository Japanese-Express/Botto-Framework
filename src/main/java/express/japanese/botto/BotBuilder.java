package express.japanese.botto;

import express.japanese.botto.core.modules.ModuleInfo;
import express.japanese.botto.core.modules.preInstalled.BotCmdListener;
import express.japanese.botto.core.modules.preInstalled.HelpCmd;
import express.japanese.botto.core.modules.preInstalled.VersionCmd;
import express.japanese.botto.core.modules.module.Module;

import java.util.ArrayList;
import java.util.List;

public class BotBuilder {
    private static final List<BotBuilder> bots = new ArrayList<>();

    public static class Shard {
        Shard(int num, int total) {
            this.shardNumber = num;
            this.shardTotal = total;
        }
        private Integer shardNumber;
        private Integer shardTotal;
        public Integer getShardNumber() {
            return shardNumber;
        }
        public Integer getShardTotal() {
            return shardTotal;
        }
    }

    boolean shouldCreateDefaultCmds = true;
    private String token = "";
    private String prefix = "";
    private boolean hasBuilt = false;
    private ArrayList<ModuleInfo> moduleInfoList = new ArrayList<>();
    private Shard shard = null;
    Class<? extends BotCmdListener> listener = BotCmdListener.class;

    private static List<Class<? extends Module>> defaultModules = new ArrayList<Class<? extends Module>>(){
        {
            add(VersionCmd.class); add(HelpCmd.class);
        }
    };
    BotBuilder() {
        bots.add(this);
    }

    /**
     * Create a new builder, in which that builder
     * will be added to an ArrayList for getAllBots
     * @return BotBuilder
     */
    public static BotBuilder createBuilder() {
        return new BotBuilder();
    }

    /**
     * Gets all the bots that you have created so far
     * @return BotBuilder list
     */
    public static List<BotBuilder> getAllBots() {
        return bots;
    }

    /**
     * Set the current shard of this bot
     * @param shardNumber this current shard
     * @param shardTotal total amount of shards
     * @return BotBuilder
     */
    public BotBuilder setShard(int shardNumber, int shardTotal) {
        shard = new Shard(shardNumber, shardTotal);
        return this;
    }

    public BotBuilder setShouldCreateDefaultCmds(boolean b) {
        this.shouldCreateDefaultCmds = b;
        return this;
    }

    /**
     * Set the current listener for this bot
     * @param listener Listener class to instantiate
     * @return BotBuilder
     */
    public BotBuilder setListener(Class<? extends BotCmdListener> listener) {
        this.listener = listener;
        return this;
    }

    /**
     * Sets the token for this bot
     * @param token Token to use on this bot
     * @return BotBuilder
     */
    public BotBuilder setToken(String token) {
        this.token = token;
        return this;
    }

    /**
     * Has the builder already built? If so then the
     * code won't trigger again if you build again.
     * @return boolean
     */
    public boolean hasBuilt() {
        return hasBuilt;
    }

    /**
     * Build the bot, giving you access to a
     * controller of the bot you've just built.
     * @return BotController
     */
    public BotController build() {
        if(this.token.isEmpty()) {
            throw new RuntimeException("Invalid token listed for Bot");
        }
        if(this.prefix.trim().isEmpty())
            this.prefix = StaticConfig.getPrefix();
        BotController bot = new BotController(this);
        hasBuilt = true;
        return bot;
    }

    /**
     * Get shards set for this current bot
     * @return Shard
     */
    public Shard getShard() {
        return shard;
    }

    /**
     * Gets the set token for this bot
     * @return String
     */
    public String getToken() {
        return token;
    }

    /**
     * Gets the set prefix for this bot
     * @return String
     */
    public String getPrefix() {
        return prefix;
    }
}
