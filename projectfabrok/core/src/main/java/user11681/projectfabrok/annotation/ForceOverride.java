package user11681.projectfabrok.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * Interface instance methods with a <b>{@code ForceOverride}</b> annotation will override methods inherited<br>
 * from superclasses of the owning interface's implementations.
 */
@Target(ElementType.METHOD)
public @interface ForceOverride {}
