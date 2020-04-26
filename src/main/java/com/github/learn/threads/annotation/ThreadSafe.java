package com.github.learn.threads.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * @author zhang.zzf
 * @date 2020-04-17
 */
@Target({ElementType.TYPE})
public @interface ThreadSafe {

    //String[] authors() default "";
    String[] authors() default "";


}
