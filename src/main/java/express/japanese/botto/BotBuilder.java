package express.japanese.botto;

import express.japanese.botto.core.modules.ModuleInfo;
import express.japanese.botto.core.modules.enums.Language;
import express.japanese.botto.core.modules.interfaces.annotations.ILanguage;
import express.japanese.botto.core.modules.interfaces.annotations.IModule;
import express.japanese.botto.core.modules.preInstalled.*;
import express.japanese.botto.core.modules.module.Module;

import javax.annotation.Nonnull;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
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
    private ArrayList<String> preloadPackages = new ArrayList<>();
    private ArrayList<Language> usedLanguages = new ArrayList<>();
    private Language defaultLanguage = Language.ENGLISH;
    private File languageLocation;
    private String token = "";
    private String ownerId = "";
    private String[] prefix = {"!"};
    private boolean hasBuilt = false;
    private Shard shard = null;
    Class<? extends UsableBotListener> listener = UsableBotListener.class;

    private static List<Class<? extends Module>> defaultModules = new ArrayList<Class<? extends Module>>(){
        {
            add(CmdVersion.class); add(CmdHelp.class); add(CmdLang.class);
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

    /**
     * Set if bot should append default commands to module list
     * @param b bool
     * @return BotBuilder
     */
    public BotBuilder setShouldCreateDefaultCmds(boolean b) {
        this.shouldCreateDefaultCmds = b;
        return this;
    }

    /**
     * Set this bot to be able to use the following languages
     * Users can switch languages with the lang command
     * @param languages
     * @return BotBuilder
     * @see ILanguage
     */
    public BotBuilder usesLanguage(@Nonnull Language... languages) {
        this.usedLanguages.addAll(Arrays.asList(languages));
        return this;
    }

    /**
     * Sets the default language to always use for the bot.
     * useful for module descriptions.
     * @param language
     * @return BotBuilder
     * @see ILanguage
     */
    public BotBuilder setDefaultLanguage(@Nonnull Language language) {
        this.defaultLanguage = language;
        return this;
    }

    /**
     * Set the bots lang file location.
     * This location will be used for each file upon `usesLaugage()`
     * @param languageLocation Directory Location
     * @return BotBuilder
     */
    public BotBuilder setLanguageLocation(File languageLocation) {
        this.languageLocation = languageLocation;
        return this;
    }

    /**
     * Initialize modules upon build, instead of after
     * <p>Initializes multiple modules based on a package
     * location, same usage as initializeModule() but
     * instead returns a list of info about each
     * module that was started.</p>
     * @param packageLocation Package location of which
     *                        all your modules are located.
     *                        For example:
     *                        "xyz.cutezy.bot.commands"
     * @return BotBuilder
     * @see ModuleInfo
     */
    public final BotBuilder initializeModulesFromPackage(String packageLocation) {
        preloadPackages.add(packageLocation);
        return this;
    }

    /**
     * Set the current listener for this bot
     * @param listener Listener class to instantiate
     * @return BotBuilder
     */
    public BotBuilder setListener(Class<? extends UsableBotListener> listener) {
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
     * Set the prefix of this bot
     * @param prefix Prefix to use
     * @return BotBuilder
     */
    public BotBuilder setPrefix(String... prefix) {
        this.prefix = prefix;
        return this;
    }

    /**
     * Sets the owner of this bot for use with
     * the ModuleInterface
     * @param ownerId Discord ID
     * @see IModule
     */
    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
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
        /*if(this.usedLanguages.isEmpty()) {
            throw new RuntimeException("There are no languages being used. Apply one with usesLanguage()");
        }
        if(this.prefix.trim().isEmpty())
            this.prefix = StaticConfig.getPrefix();*/
        if(languageLocation == null)
            languageLocation = new File("./global/lang/");
        BotController bot = new BotController(this);
        hasBuilt = true;
        return bot;
    }

    /**
     * Obtains packages to be initialized on load
     * @return ArrayList<String>
     */
    public ArrayList<String> getPreloadPackages() {
        return preloadPackages;
    }

    /**
     * Get all used languages for this bot
     * @return ArrayList<Language>
     */
    public ArrayList<Language> getUsedLanguages() {
        return usedLanguages;
    }

    /**
     * Get language files location via directory
     * @return File as Directory
     */
    public File getLanguageLocation() {
        return languageLocation;
    }

    /**
     * Gets default language for this bot
     * @return Language
     */
    public Language getDefaultLanguage() {
        return defaultLanguage;
    }

    /**
     * Get shards set for this current bot
     * @return Shard
     */
    public Shard getShard() {
        return shard;
    }

    /**
     * Returns the currently set ownerId
     * @return String value of Discord ID
     */
    public String getOwnerId() {
        return ownerId;
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
    public String[] getPrefix() {
        return prefix;
    }
}
