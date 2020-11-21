package user11681.projectfabrok.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * Mark a method as a proxy for a field or a method. The annotated element is transformed at runtime to invoke the target method.<br>
 * Annotations of this type are intended for accessing restricted fields and methods.<br><br>
 * <p>
 * If the annotated method is an intance method, then {@link #ownerName} may be left empty; in that case,
 */
@Target({ElementType.FIELD, ElementType.METHOD})
public @interface Proxy {
    Class<?> DEFAULT_OWNER = Object.class;
    String DEFAULT_OWNER_NAME = "this";
    String DEFAULT_DESCRIPTOR = "automatic";

    /**
     * @return the name of the target.
     */
    String value();

    /**
     * @return the target's declaring type. When {@link #DEFAULT_OWNER}, search the current class and its superclasses recursively.
     */
    Class<?> owner() default DEFAULT_OWNER;

    /**
     * @return the name of the target's declaring type. When {@link #DEFAULT_OWNER_NAME}, search the current class and its superclasses recursively.
     */
    String ownerName() default DEFAULT_OWNER_NAME;

    /**
     * @return the descriptor of the target method (optional unless multiple matching methods are found;<br>
     * in that case, an {@link IllegalArgumentException} exception will be thrown).
     */
    String descriptor() default DEFAULT_DESCRIPTOR;
}
