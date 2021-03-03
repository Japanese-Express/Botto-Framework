package express.japanese.botto.core;

import express.japanese.botto.core.modules.ModuleInfo;

public class ModuleException {
    public static RuntimeException Throw(ModuleInfo moduleInfo) {
        return new RuntimeException(moduleInfo.getError().getErrorMsg(),
                new Throwable(moduleInfo.getExtractedClass().toGenericString())
        );
    }

    private static final String space = "    ";
    public static void SilentThrowModule(ModuleInfo moduleInfo, String... errMessages) {
        System.err.println("!!Module \"" + moduleInfo.getExtractedClass().getSimpleName() + "\" had an error!!! ");
        System.err.println(space +"Reason: " + moduleInfo.getError().getErrorMsg());
        for (String errMessage : errMessages)
            System.err.println(space+"- " + errMessage);
    }
}
