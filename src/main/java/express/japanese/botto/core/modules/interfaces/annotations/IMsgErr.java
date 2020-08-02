package express.japanese.botto.core.modules.interfaces.annotations;

import express.japanese.botto.core.modules.enums.MsgStyle;

public @interface IMsgErr {
    boolean isBeingUsed() default true;
    boolean showInConsole() default false;
    String msg();
    MsgStyle msgStyle();
}
