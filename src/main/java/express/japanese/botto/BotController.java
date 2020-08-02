package express.japanese.botto;

import express.japanese.botto.core.modules.enums.Category;
import express.japanese.botto.core.modules.ModuleInfo;
import express.japanese.botto.core.modules.interfaces.annotations.IModule;
import express.japanese.botto.core.modules.preInstalled.BotCmdListener;
import express.japanese.botto.core.modules.preInstalled.HelpCmd;
import express.japanese.botto.core.modules.preInstalled.VersionCmd;
import express.japanese.botto.core.modules.module.Module;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import org.reflections.Reflections;

import javax.annotation.Nullable;
import javax.security.auth.login.LoginException;
import java.util.*;

public class BotController {
    private final JDA jda;
    private String prefix;
    protected final List<Module> defaultModules;
    private final BotCmdListener listener;
    public final HashMap<Category, List<Module>> modulesByCategory;
    public final List<Module> modulesTrackMessages;
    public final HashMap<String, Module> modules;
    public final List<Class<? extends Module>> failedModules;
    public final HashMap<Module, IModule> moduleInterfaces;
    /**
     * Creates instance of the bot for use in a public state
     * @param botBuilder Pre-built sharding and user things
     */
    BotController(BotBuilder botBuilder) {
        if((this.listener=this.createListenerInstance(botBuilder.listener)) == null)
            throw new RuntimeException("Bot listener could not be instantiated and returned null",
                    new Throwable(botBuilder.listener.toString()));
        this.listener.setBotController(this);
        this.listener.onLoad();
        defaultModules = new ArrayList<>();
        modulesByCategory = new HashMap<>();
        modulesTrackMessages = new ArrayList<>();
        modules = new HashMap<>();
        failedModules = new ArrayList<>();
        moduleInterfaces = new HashMap<>();
        this.prefix = botBuilder.getPrefix();
        BotBuilder.Shard shard = botBuilder.getShard();
        JDABuilder jdaBuilder = JDABuilder.createDefault(botBuilder.getToken());
        if(shard != null)
            jdaBuilder.useSharding(shard.getShardNumber(), shard.getShardTotal());
        jdaBuilder.addEventListeners(this.listener);
        jdaBuilder.setAutoReconnect(true);

        if(botBuilder.shouldCreateDefaultCmds) {
            initializeModule(HelpCmd.class);
            initializeModule(VersionCmd.class);
        }
        //initializeModulesFromPackage()
        try {
            jda = jdaBuilder.build();
        } catch (LoginException e) {
            throw new RuntimeException(e);
        }
    }

    @Nullable
    private BotCmdListener createListenerInstance(Class<? extends BotCmdListener> listener) {
        try {
            return listener.newInstance();
        } catch(Exception e) {
            return null;
        }
    }

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

    /**
     * Initialize multiple modules at the same time
     * @throws express.japanese.botto.core.ModuleException if a module can't be initialized
     * @param packageLocation Package location such as "express.japanese.modules.preInstalled"
     * @return ModuleInfo list for ease of use
     */
    public final List<ModuleInfo> initializeModulesFromPackage(String packageLocation) {
        Reflections reflections = new Reflections(packageLocation);
        Set<Class<? extends Module>> allClasses = reflections.getSubTypesOf(Module.class);

        List<ModuleInfo> moduleInfoList = new ArrayList<>();
        for(Class<? extends Module> clz : allClasses)
            moduleInfoList.add(initializeModule(clz));
        return moduleInfoList;
    }
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
        if(annotation.trackUserMessages())
            modulesTrackMessages.add(info.getModule());

        List<Module> list;
        if(annotation.trackUserMessages())
            modulesTrackMessages.add(instance);
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

    public String getPrefix() {
        return prefix;
    }

    public JDA getJDA() {
        return jda;
    }

    public void removeDefaultModules() {

    }
}
