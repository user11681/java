package user11681.projectfabrok.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Target;

/**
 * Register a callback to an event. If applied to a method, then a lambda will be made;<br>
 * if applied to a class, then it will be instantiated and used as the callback object.
 */
@Repeatable(Callback.Callbacks.class)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface Callback {
    /**
     * @return the owning class of the {@linkplain #field event field}.
     */
    Class<?> owner();

    /**
     * @return the name of the event field in {@linkplain #owner the specified class}. The field must be static.
     */
    String field();

    @Target(ElementType.TYPE)
    @interface Callbacks {
        Callback[] value();
    }
}
