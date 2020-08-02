package express.japanese.botto.core.modules.interfaces.annotations;

import express.japanese.botto.core.modules.enums.MsgStyle;

public @interface ICooldown {
    IMsgErr cooldownMsg() default @IMsgErr(msg="", msgStyle=MsgStyle.None);
    boolean useCooldown() default true;
    long cooldownMillis() default 1500;
}
