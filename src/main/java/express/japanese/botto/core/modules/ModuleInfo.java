package express.japanese.botto.core.modules;

import express.japanese.botto.core.modules.interfaces.Author;
import express.japanese.botto.core.modules.interfaces.annotations.IModule;
import express.japanese.botto.core.modules.interfaces.IsDefault;
import express.japanese.botto.core.modules.enums.ModuleError;
import express.japanese.botto.core.modules.module.CmdModule;
import express.japanese.botto.core.modules.module.Module;
import express.japanese.botto.core.ModuleException;

import java.lang.reflect.InvocationTargetException;

public class ModuleInfo {
    private ModuleError error = null;
    private final IsDefault isDefault;
    private final String moduleAuthor;
    private final Class<? extends Module> extractedClass;
    private final Module module;
    private final IModule moduleAnnotation;

    public ModuleInfo(final Class<? extends Module> moduleClass, final IModule moduleAnnotation) {
        this.extractedClass = moduleClass;
        this.moduleAnnotation = moduleAnnotation;
        if(moduleAnnotation == null) {
            error = ModuleError.NO_ANNOTATION;
            ModuleException.Err(this);
            this.isDefault = null;
            this.module = null;
            this.moduleAuthor = null;
            return;
        }
        if(moduleClass.getName().equals(CmdModule.class.getName())) {
            this.isDefault = null;
            this.module = null;
            this.moduleAuthor = null;
            return;
        }
        try {
            this.module = moduleClass.getConstructor().newInstance();
            this.module.setModuleInterface(moduleAnnotation);
        } catch (Exception e) {
            e.printStackTrace();
            error = ModuleError.FAILED_TO_INSTANCE;
            throw ModuleException.Throw(this);
        }
        if(this.module.hasErrors()) {
            error = this.module.getErrors().get(0);
            throw ModuleException.Throw(this);
        }

        Author authorAnnotation = moduleClass.getAnnotation(Author.class);
        if(authorAnnotation != null) {
            this.moduleAuthor = authorAnnotation.value();
        } else
            this.moduleAuthor = "Unknown";

        this.isDefault = moduleClass.getAnnotation(IsDefault.class);
    }

    public final boolean isModuleDefault() {
        return this.isDefault != null;
    }

    public final String getModuleAuthor() {
        return this.moduleAuthor;
    }

    public final Module getModule() {
        return module;
    }
    public final IModule getModuleAnnotation() {
        return moduleAnnotation;
    }

    public final Class<? extends Module> getExtractedClass() {
        return extractedClass;
    }

    public final boolean hasErrors() {
        return error != null;
    }
    public final ModuleError getError() {
        return error;
    }
}
