package express.japanese.botto.core.modules.interfaces.annotations;

import express.japanese.botto.core.modules.enums.BotCategory;

import java.lang.annotation.*;

import express.japanese.botto.core.modules.enums.Language;
import net.dv8tion.jda.api.entities.ChannelType;

@Retention(value=RetentionPolicy.RUNTIME)
@Target(value=ElementType.TYPE)
public @interface IModule {
    String[] names();

    boolean useInHelp() default true;

    ILanguage[] tinyDescription();
    ILanguage[] fullDescription() default @ILanguage(
            language = Language.UNKNOWN, value = ""
    );

    BotCategory category();
    String customCategoryName() default "";

    //@Deprecated String[] altNames() default {};
    //@Deprecated Permission[] permissions() default {Permission.UNKNOWN};
    //@Deprecated String permissionError() default "";

    ICooldown cooldown() default @ICooldown(
            useCooldown = false
    );
    IPermissions botPermissions() default @IPermissions(
            usePermissions = false, permissionsToUse = {}
    );
    IPermissions permissions() default @IPermissions(
        usePermissions = false, permissionsToUse = {}
    );

    ChannelType[] channelTypes();

    int requiredArgCount() default 0;

    String argErrorMessage() default "%cmd% requires at-least %reqArgCount% arguments";

    String[] serversById() default {};
    boolean ownerOnly() default false;
}

