package net.sourceforge.plantuml.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Indicates that the annotated class, method, constructor, or field is a duplication
 * of another part of the code. This can be used to mark areas of the codebase that
 * require refactoring or special attention.
 *
 * @author Whoever wrote it
 */
@Retention(RetentionPolicy.SOURCE)
@Target({ElementType.TYPE, ElementType.METHOD, ElementType.CONSTRUCTOR, ElementType.FIELD})
public @interface DuplicateCode {
    /**
     * Optional comment to explain more about the duplication, e.g., where the original
     * code is or why this duplication exists.
     */
    String comment() default "";

    /**
     * A reference to the original occurrence of the code to help in tracking and managing duplicates.
     */
    String reference() default "";
}
