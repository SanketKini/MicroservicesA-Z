package com.awd.feature.toggle.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.FIELD, ElementType.METHOD})
public @interface FeatureFlag {
    String value();
    boolean inverse() default false;
    boolean throwToggleOffError() default false;
}
