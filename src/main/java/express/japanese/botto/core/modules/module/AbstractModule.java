package express.japanese.botto.core.modules.module;

import net.dv8tion.jda.api.entities.Message;

import javax.annotation.Nonnull;

abstract class AbstractModule {
    public abstract void run(String cmd, @Nonnull String[] args, final Message msg);
}
