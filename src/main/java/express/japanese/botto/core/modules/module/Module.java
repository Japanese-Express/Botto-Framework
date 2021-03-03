package express.japanese.botto.core.modules.module;

import express.japanese.botto.BotController;
import express.japanese.botto.core.modules.ModuleInfo;
import express.japanese.botto.core.modules.enums.Language;
import express.japanese.botto.core.modules.enums.ModuleError;
import express.japanese.botto.core.modules.interfaces.annotations.ILanguage;
import express.japanese.botto.misc.LanguageConfig;
import express.japanese.botto.misc.modulistic.RichEmbed;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.ChannelType;
import express.japanese.botto.core.modules.enums.BotCategory;
import express.japanese.botto.core.modules.interfaces.annotations.IModule;
import net.dv8tion.jda.api.entities.User;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

@IModule(
        names = {}, tinyDescription = {@ILanguage(language = Language.ENGLISH, value = "")},
        category = BotCategory.Unknown, channelTypes = ChannelType.UNKNOWN)
public abstract class Module extends AbstractModule {
    private static final class ModulisticError {
        ModuleError originalError;
    }
    private final List<ModuleError> moduleErrors = new ArrayList<>();
    protected BotController botControllerInst = null;
    private ModuleInfo moduleInfo;
    private IModule moduleInterface;

    /**
     * Sets own bot controller for JDA and other things
     * @param botControllerInst Instance of BotController
     * @see BotController
     */
    public final void setBotControllerInst(BotController botControllerInst) {
        this.botControllerInst = botControllerInst;
    }

    /* -- MODULE ERRORS START -- */
    /**
     * Does this module have errors?
     * @return boolean
     */
    public final boolean hasErrors() {
        return moduleErrors.size() > 0;
    }

    /**
     * Gets modules error list, if any
     * @return List of ModuleError
     * @see ModuleError
     */
    public final List<ModuleError> getErrors() {
        return moduleErrors;
    }

    /**
     * <p>Should only be used if the module has errors
     * during initialization, as it's checked in the
     * framework of the bot</p>
     * @param error Error to trigger during phase
     * @see ModuleError
     */
    public void addError(ModuleError error) {
        moduleErrors.add(error);
    }
    public void addError(ModuleError error, Class<?> cause) {

    }
    /* -- MODULE ERRORS END -- */

    public void onReady(){}
    public void onClose(){}

    /**
     * To be used for the users own modules
     * @return RichEmbed
     * @see RichEmbed
     */
    public final RichEmbed createEmbed() {
        return new RichEmbed();
    }

    /**
     * Sets the modules interface class for reading
     * @param moduleInterface Modules interface
     * @see IModule
     */
    public final void setModuleInterface(IModule moduleInterface) {
        this.moduleInterface = moduleInterface;
    }

    public final void setModuleInfo(ModuleInfo moduleInfo) {
        this.moduleInfo = moduleInfo;
    }

    /**
     * Gets full language file
     * @param language
     * @return
     */
    //public final LanguageConfig getLanguage(Language language) {

    //}

    /**
     * Gets self interface of this module, if any
     * @return IModule
     * @see IModule
     */
    public final IModule getModuleInterface() {
        return moduleInterface;
    }

    public ModuleInfo getModuleInfo() {
        return moduleInfo;
    }

    /**
     * Get used language for a particular user.
     * Requires languages to be setup in BotBuilder
     * @param user user
     * @return Language ENUM
     * @see express.japanese.botto.BotBuilder
     */
    public final Language getUserLang(User user) {
        return botControllerInst.getUserLang(user);
    }

    public final LanguageConfig getLang(Language language) {
        return botControllerInst.languageConfigs.get(language);
    }

    public final void setUsedLanguageFor(User user, Language language) {
        botControllerInst.updateLangFor(user, language);
    }

    /**
     * Gets module tiny description for language
     * @return ILanguage
     */
    @Nullable
    public final ILanguage getTinyDescByLang(Language language) {
        for (ILanguage iLanguage : getModuleInterface().tinyDescription()) {
            if(iLanguage.language() == language)
                return iLanguage;
        }
        return null;
    }

    /**
     * Gets module tiny description for language
     * @return ILanguage
     */
    @Nullable
    public final ILanguage getFullDescByLang(Language language) {
        for (ILanguage iLanguage : getModuleInterface().fullDescription()) {
            if(iLanguage.language() == language)
                return iLanguage;
        }
        return null;
    }

    /**
     * Gets JDA from this module's controller
     * @return JDA
     * @see BotController
     */
    public final JDA getJDA() {
        return botControllerInst.getJDA();
    }

    /**
     * Returns the modules public prefix, if any is set
     * @return String of prefix
     */
    public final String[] getPrefix() {
        return botControllerInst.getPrefix();
    }
}

