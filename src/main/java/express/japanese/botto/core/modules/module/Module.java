package express.japanese.botto.core.modules.module;

import express.japanese.botto.BotController;
import express.japanese.botto.core.modules.enums.ModuleError;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.ChannelType;
import express.japanese.botto.core.modules.enums.Category;
import express.japanese.botto.core.modules.interfaces.annotations.IModule;

import java.util.ArrayList;
import java.util.List;

@IModule(
        names = {}, tinyDescription = "", category = Category.Unknown, channelTypes = ChannelType.UNKNOWN)
public abstract class Module extends AbstractModule {
    private final List<ModuleError> moduleErrors = new ArrayList<>();
    protected BotController botControllerInst = null;
    private IModule moduleInterface;

    public final void setBotControllerInst(BotController botControllerInst) {
        this.botControllerInst = botControllerInst;
    }

    public final boolean hasErrors() {
        return moduleErrors.size() > 0;
    }
    public final List<ModuleError> getErrors() {
        return moduleErrors;
    }
    void addError(ModuleError error) {
        moduleErrors.add(error);
    }

    /*public Module(IModule moduleInstance) {
        this.moduleInstance = moduleInstance;
    }*/

    public final void setModuleInterface(IModule moduleInterface) {
        this.moduleInterface = moduleInterface;
    }

    public final IModule getModuleInterface() {
        return moduleInterface;
    }

    public final JDA getJDA() {
        return botControllerInst.getJDA();
    }

    public final String getPrefix() {
        return botControllerInst.getPrefix();
    }

    @Deprecated
    public String returnCmdHelp() {
        return null;
    }
}

