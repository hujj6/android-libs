package com.hujinwen.entity.annotation.nano;

import java.lang.annotation.*;

/**
 * Created by hu-jinwen on 12/17/20
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface Router {

    String value();

//    Method method() default Method.GET;
}
