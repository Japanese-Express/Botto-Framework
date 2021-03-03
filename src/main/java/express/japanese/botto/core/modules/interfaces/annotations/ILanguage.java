package express.japanese.botto.core.modules.interfaces.annotations;

import express.japanese.botto.core.modules.enums.Language;
import express.japanese.botto.core.modules.enums.MsgStyle;
import net.dv8tion.jda.api.Permission;

public @interface ILanguage {
    Language language();
    String value();
}
