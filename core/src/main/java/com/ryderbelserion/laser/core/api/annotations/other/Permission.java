package com.ryderbelserion.laser.core.api.annotations.other;

import com.ryderbelserion.laser.core.enums.PermissionMode;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface Permission {

    PermissionMode mode() default PermissionMode.OP;

    String permission() default "";

    String description() default "";

}