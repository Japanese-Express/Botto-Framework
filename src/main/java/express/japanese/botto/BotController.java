package express.japanese.botto;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import express.japanese.botto.core.modules.ModuleInfo;
import express.japanese.botto.core.modules.enums.ICategory;
import express.japanese.botto.core.modules.enums.Language;
import express.japanese.botto.core.modules.interfaces.annotations.IModule;
import express.japanese.botto.core.modules.interfaces.annotations.Track;
import express.japanese.botto.core.modules.module.Module;
import express.japanese.botto.core.modules.module.events.EventUtil;
import express.japanese.botto.core.modules.preInstalled.*;
import express.japanese.botto.misc.LanguageConfig;
import express.japanese.botto.misc.UserConfig;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.ChunkingFilter;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import org.reflections.Reflections;

import javax.annotation.Nullable;
import javax.security.auth.login.LoginException;
import java.io.File;
import java.lang.reflect.Method;
import java.util.*;

public class BotController {

//region VARIABLES
    private final JDA jda;
    private final String ownerId;
    private final String[] prefix;
    private Language defaultLanguage;
    protected List<Language> usedLanguages;
    protected final List<Module> defaultModules;
    private final UsableBotListener listener;
    public final HashMap<ICategory, List<Module>> modulesByCategory;
    public final HashMap<String, Module> modules;
    public final List<Class<? extends Module>> failedModules;
    public final HashMap<Module, IModule> moduleInterfaces;
//endregion

//region INSTANTIATE
    /**
     * Creates instance of the bot for use in a public state
     * @param botBuilder Pre-built sharding and user things
     */
    BotController(BotBuilder botBuilder) {
        // LISTENER INITIALIZE
        if((this.listener=this.createListenerInstance(botBuilder.listener)) == null)
            throw new RuntimeException("Bot listener could not be instantiated and returned null",
                    new Throwable(botBuilder.listener.toString()));
        this.listener.setBotController(this);

        // LISTS
        defaultModules = new ArrayList<>();
        modulesByCategory = new HashMap<>();
        modules = new HashMap<>();
        failedModules = new ArrayList<>();
        moduleInterfaces = new HashMap<>();
        languageConfigs = new HashMap<>();

        // GETTERS
        this.ownerId = botBuilder.getOwnerId();
        this.prefix = botBuilder.getPrefix();
        BotBuilder.Shard shard = botBuilder.getShard();

        // JDA CREATE
        JDABuilder jdaBuilder = JDABuilder.createDefault(botBuilder.getToken());
        jdaBuilder.setChunkingFilter(ChunkingFilter.ALL);
        jdaBuilder.setMemberCachePolicy(MemberCachePolicy.ALL);
        jdaBuilder.enableIntents(GatewayIntent.GUILD_MEMBERS);
        if(shard != null)
            jdaBuilder.useSharding(shard.getShardNumber(), shard.getShardTotal());
        jdaBuilder.addEventListeners(this.listener);
        jdaBuilder.setAutoReconnect(true);

        // MODULE INITIALIZATION
        if(botBuilder.shouldCreateDefaultCmds) {
            initializeModule(CmdHelp.class);
            initializeModule(CmdVersion.class);
            initializeModule(CmdLang.class);
        }
        ArrayList<String> preload = botBuilder.getPreloadPackages();
        if(preload.size() > 0) {
            for(String s : preload)
                initializeModulesFromPackage(s);
        }

        // BUILD OR FAIL
        try {
            jda = jdaBuilder.build();
        } catch (LoginException e) {
            throw new RuntimeException(e);
        }

        // LANGUAGE INITIALIZATION
        this.defaultLanguage = botBuilder.getDefaultLanguage();
        if(botBuilder.getUsedLanguages().size() > 0) {
            this.usedLanguages = botBuilder.getUsedLanguages();
            File loc = botBuilder.getLanguageLocation();
            usedLanguages.forEach((language) -> {
                File langFile = new File(loc, language.name()+".lang");
                this.languageConfigs.put(language, new LanguageConfig(langFile));
            });
            if(globalLanguages == null) {
                globalLanguages = UserConfig.initializeConfig(new File(loc, "GlobalUsers.json").getAbsolutePath());
                grabUserLangs();
            }
        }
    }

    @Nullable
    private UsableBotListener createListenerInstance(Class<? extends UsableBotListener> listener) {
        try {
            return listener.newInstance();
        } catch(Exception e) {
            return null;
        }
    }
//endregion

//region LANGUAGES
    private static class UserLang {
        JsonElement arrayReplacement;
        Language userLang;
        String userId;
    }
    private static UserConfig globalLanguages;
    public final HashMap<Language, LanguageConfig> languageConfigs;
    private static HashMap<String/*userId*/, UserLang> userLangs = new HashMap<>();
    public static void grabUserLangs() {
        if(globalLanguages.get("users") == null)
            return;
        JsonArray users = globalLanguages.get("users").getAsJsonArray();
        users.forEach((jsonElement) -> {
            JsonObject obj = jsonElement.getAsJsonObject();
            String userId = obj.get("userId").getAsString();
            String userLang = obj.get("userLang").getAsString();
            UserLang userLangClass = new UserLang();
            userLangClass.arrayReplacement = obj;
            userLangClass.userLang = Language.valueOf(userLang);
            userLangClass.userId = userId;
            userLangs.put(userId, userLangClass);
        });
    }
    public LanguageConfig getConfigFrom(Language language)  {
        return languageConfigs.get(language);
    }
    public Language getUserLang(User user) {
        if(userLangs.containsKey(user.getId()))
            return userLangs.get(user.getId()).userLang;
        if(!usedLanguages.isEmpty())
            return usedLanguages.get(0);
        return Language.ENGLISH;
    }
    // I have yet to figure out a good way to do this.
    public final void updateLangFor(User user, Language language) {
        JsonArray users;
        if(globalLanguages.get("users") != null)
            users = globalLanguages.get("users").getAsJsonArray();
        else
            users = new JsonArray();
        UserLang userLang;
        if(userLangs.containsKey(user.getId())) {
            userLang = userLangs.get(user.getId());
            users.remove(userLang.arrayReplacement);
        } else {
            userLang = new UserLang();
            userLang.userId = user.getId();
            userLangs.put(user.getId(), userLang);
        }
        userLang.userLang = language;
        JsonObject newJElement = new JsonObject();
        newJElement.addProperty("userLang", userLang.userLang.name());
        newJElement.addProperty("userId", user.getId());
        userLang.arrayReplacement = newJElement;
        users.add(newJElement);
        JsonObject a = new JsonObject();
        a.add("users", users);
        globalLanguages.updateJsonObj(a);
    }
//endregion

//region MODULES
//region MODULE_INSTANTIATION
    /**
     * Initializes multiple modules based on a package
     * location, same usage as initializeModule() but
     * instead returns a list of info about each
     * module that was started.
     * @param packageLocation Package location of which
     *                        all your modules are located.
     *                        For example:
     *                        "xyz.cutezy.bot.commands"
     * @return Module Info list
     * @see ModuleInfo
     */
    @SuppressWarnings("UnusedReturnValue") // ty <3
    public final List<ModuleInfo> initializeModulesFromPackage(String packageLocation) {
        Reflections reflections = new Reflections(packageLocation);
        Set<Class<? extends Module>> allClasses = reflections.getSubTypesOf(Module.class);

        List<ModuleInfo> moduleInfoList = new ArrayList<>();
        for(Class<? extends Module> clz : allClasses)
            moduleInfoList.add(initializeModule(clz));
        return moduleInfoList;
    }


    /**
     * Initialize a module, with it's ModuleInterface, and
     * returns module information based on how the module
     * reacted.
     * @param c Class of Module
     * @return Module Info
     * @see ModuleInfo
     */
    public final ModuleInfo initializeModule(Class<? extends Module> c) {
        IModule annotation = c.getAnnotation(IModule.class);
        ModuleInfo info = new ModuleInfo(c, annotation);
        if(info.hasErrors())
            return info;
        if(StaticConfig.debug)
            System.out.println("Added " + c.getSimpleName() + " to class list");
        Module instance = info.getModule();
        if(instance == null)
            return info;
        instance.setBotControllerInst(this);

        List<Module> list;
        for(Method method : c.getMethods()) {
            Track track = method.getAnnotation(Track.class);
            if(track == null || !track.enabled())
                continue;
            EventUtil.toEventForAnnotation(instance, method);
        }
        /*if(annotation.trackUserMessages()) {
            modulesTrackMessages.add(instance);
            System.out.println("Track Messages Add: " + instance.getModuleInterface().names()[0]);
        }
        if(annotation.trackUserReactions()) {
            modulesTrackReactions.add(instance);
            System.out.println("Track Reactions Add: " + instance.getModuleInterface().names()[0]);
        }*/
        moduleInterfaces.put(instance, annotation);
        if (modulesByCategory.containsKey(annotation.category())) {
            list = modulesByCategory.get(annotation.category());
            list.add(instance);
            modulesByCategory.replace(annotation.category(), list);
        } else {
            list = new ArrayList<>();
            list.add(instance);
            modulesByCategory.put(annotation.category(), list);
        }
        String[] moduleNames = annotation.names();
        if (moduleNames.length > 0) {
            for (String altCmd : moduleNames)
                modules.put(altCmd, instance);
        }
        return info;
    }
//endregion
//region MODULE_TRACKING

//endregion
    @Nullable
    public final Module getModule(String name) {
        if(modules.get(name) != null)
            return modules.get(name);
        for (Map.Entry<String, Module> moduleEntry : modules.entrySet()) {
            if(moduleEntry.getKey().equalsIgnoreCase(name))
                return moduleEntry.getValue();
        }
        return null;
    }
//endregion

    public String[] getPrefix() {
        return prefix;
    }

    public JDA getJDA() {
        return jda;
    }

    public void removeDefaultModules() {

    }
}
