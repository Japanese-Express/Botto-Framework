package express.japanese.botto.core.modules.enums;

import java.util.HashMap;
import java.util.Map;

public class CustomCategory implements ICategory {
    private static Map<String, CustomCategory> customCategoryMap = new HashMap<>();

    private final String name;
    CustomCategory(String name) {
        this.name = name;
        customCategoryMap.put(name, this);
    }

    @Override
    public boolean isCustom() {
        return true;
    }

    public String getName() {
        return name;
    }
}
