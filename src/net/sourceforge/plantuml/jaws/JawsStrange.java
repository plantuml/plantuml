package net.sourceforge.plantuml.jaws;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.SOURCE)
@Target({ ElementType.CONSTRUCTOR, ElementType.METHOD, ElementType.TYPE, ElementType.FIELD })
public @interface JawsStrange {
	
	String comment() default "";
}
