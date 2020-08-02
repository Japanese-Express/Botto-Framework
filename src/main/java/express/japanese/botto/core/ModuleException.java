package express.japanese.botto.core;

import express.japanese.botto.core.modules.ModuleInfo;

public class ModuleException {
    public static RuntimeException Throw(ModuleInfo moduleInfo) {
        return new RuntimeException(
                "ModuleException on " + moduleInfo.getExtractedClass().getSimpleName(),
                new Throwable(moduleInfo.getError().getErrorMsg())
        );
    }
    public static void Err(ModuleInfo moduleInfo) {
        System.err.println("!!Exception on ModuleInfo!!! ");
        System.err.println("    Module \"" + moduleInfo.getExtractedClass().getSimpleName() + "\" failed to load");
        System.err.println("    " + moduleInfo.getError().getErrorMsg());
    }
}
