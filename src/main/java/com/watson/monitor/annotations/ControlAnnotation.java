package com.watson.monitor.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface ControlAnnotation {
    String value() default "";
    String[] values() default {};
}
