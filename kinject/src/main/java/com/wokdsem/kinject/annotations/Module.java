package com.wokdsem.kinject.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
public @interface Module {

	boolean completed() default false;

}
