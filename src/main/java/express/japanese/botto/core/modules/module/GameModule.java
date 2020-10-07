package express.japanese.botto.core.modules.module;

import express.japanese.botto.core.modules.enums.BotCategory;
import express.japanese.botto.core.modules.enums.Language;
import express.japanese.botto.core.modules.enums.ModuleError;
import express.japanese.botto.core.modules.interfaces.annotations.ILanguage;
import express.japanese.botto.core.modules.interfaces.annotations.IModule;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.Message;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

@IModule(
        names = {}, tinyDescription = @ILanguage(language = Language.ENGLISH, value = ""),
        category = BotCategory.Unknown, channelTypes = ChannelType.UNKNOWN)
public abstract class GameModule extends CmdModule {
    private final List<ModuleError> errors = new ArrayList<>();

    /*public CmdModule(IModule moduleInstance) {
        super(moduleInstance);
        this.moduleInstance = moduleInstance;
    }*/

    public abstract void run(String cmd, @NotNull String[] args, Message msg);
}

