package com.wokdsem.kinject.annotations;

import com.wokdsem.kinject.core.KinjectValues;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

@Target(ElementType.PARAMETER)
public @interface Named {

	String value() default KinjectValues.DEFAULT_NAMED;

}
