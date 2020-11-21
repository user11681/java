package user11681.projectfabrok.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * This annotation interface marks methods for inlining.
 */
@Target(ElementType.METHOD)
public @interface Inline {}
