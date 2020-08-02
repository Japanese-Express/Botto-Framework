package express.japanese.botto.core.modules.interfaces.annotations;

import express.japanese.botto.core.modules.enums.Category;

import java.lang.annotation.*;

import net.dv8tion.jda.api.entities.ChannelType;

@Retention(value=RetentionPolicy.RUNTIME)
@Target(value=ElementType.TYPE)
public @interface IModule {
    String[] names();

    boolean trackUserMessages() default false;
    boolean useInHelp() default true;

    String tinyDescription();
    String fullDescription() default "";

    Category category();

    //@Deprecated String[] altNames() default {};
    //@Deprecated Permission[] permissions() default {Permission.UNKNOWN};
    //@Deprecated String permissionError() default "";

    ICooldown cooldown() default @ICooldown(
            useCooldown = false
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

