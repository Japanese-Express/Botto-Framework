package express.japanese.botto.core.modules.interfaces.annotations;

import express.japanese.botto.core.modules.enums.MsgStyle;
import net.dv8tion.jda.api.Permission;

public @interface IPermissions {
    IMsgErr permErrorMsg() default @IMsgErr(msgStyle = MsgStyle.None, msg = "");
    boolean usePermissions() default true;
    Permission[] permissionsToUse();
}
