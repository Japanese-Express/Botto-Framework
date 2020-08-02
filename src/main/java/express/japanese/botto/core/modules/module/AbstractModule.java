package express.japanese.botto.core.modules.module;

import net.dv8tion.jda.api.entities.Message;

abstract class AbstractModule {
    public abstract void run(String cmd, String[] args, final Message msg);
}
