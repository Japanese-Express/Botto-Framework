package express.japanese.botto.core.modules.enums;

public enum BotCategory implements ICategory {
    General,
    OwnerOnly,
    Admin,
    User,
    Media,
    Action,
    Game,
    Image,
    Fun,
    Info,
    Utility,
    Misc,
    Website,
    Tags,
    NSFW,
    Roles,
    Tracking,
    Bot,
    Any,
    Unknown;
    BotCategory() { }

    @Override
    public boolean isCustom() {
        return false;
    }

    @Override
    public String getName() {
        return this.name();
    }
}

