package pink.cutezy.example;

import express.japanese.botto.BotController;
import express.japanese.botto.Botto;
import express.japanese.botto.StaticConfig;
import express.japanese.botto.core.modules.enums.Language;
import net.dv8tion.jda.api.entities.Activity;

public class Main {
    public static void main(String[] args) {
        BotController controller = Botto.initialize()
                .setDefaultLanguage(Language.ENGLISH)
                .setToken(StaticConfig.getStr("token"))
                .setPrefix("!")
                .build();
        controller.initializeModulesFromPackage("xyz.cutezy.example.commands");

        String prefix = controller.getPrefix()[0]+"help";
        Activity activity = Activity.of(Activity.ActivityType.WATCHING, prefix);
        controller.getJDA().getPresence().setPresence(activity, true);
    }
}
