package com.jessewo.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by wangzhx on 2020/3/23.
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.CLASS)
public @interface JsBridgeWidget {

    String methodName() default "";
}
