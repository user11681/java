package user11681.projectfabrok.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Target;
import user11681.projectfabrok.ProjectFabrok;

/**
 * Mark the annotated element as an entrypoint target.
 */
@Target({ElementType.TYPE, ElementType.METHOD, ElementType.FIELD})
@Repeatable(Entrypoint.Entrypoints.class)
public @interface Entrypoint {
    String DEFAULT_ID = ProjectFabrok.ID;
    String DEFAULT_ADAPTER = "default";

    /**
     * @return the identifiers of the entrypoints to receive.
     */
    String[] value();

    /**
     * @return the identifier of the mod to which the annotated element belongs.
     */
    String id() default DEFAULT_ID;

    /**
     * @return the language adapter that should be used.
     */
    String adapter() default DEFAULT_ADAPTER;

    @Target({ElementType.TYPE, ElementType.METHOD, ElementType.FIELD})
    @interface Entrypoints {
        Entrypoint[] value();
    }
}
