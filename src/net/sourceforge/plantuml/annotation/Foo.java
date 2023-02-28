package net.sourceforge.plantuml.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.SOURCE)
@Target({ ElementType.CONSTRUCTOR, ElementType.METHOD, ElementType.TYPE })
public @interface Foo {
    // ::remove folder when __HAXE__
}
