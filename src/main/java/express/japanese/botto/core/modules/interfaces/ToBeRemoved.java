package express.japanese.botto.core.modules.interfaces;

import java.lang.annotation.*;

/** TODO: Check removals */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD, ElementType.FIELD, ElementType.CONSTRUCTOR})
public @interface ToBeRemoved {
    /**
     * Usually a reason for removal
     * @return String
     */
    String value();
}
