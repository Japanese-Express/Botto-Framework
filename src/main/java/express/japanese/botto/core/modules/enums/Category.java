package express.japanese.botto.core.modules.enums;

public enum Category implements ICategory {
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
    Category() { }

    @Override
    public boolean isCustom() {
        return false;
    }

    @Override
    public String getName() {
        return this.name();
    }
}

