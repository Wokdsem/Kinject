package com.wokdsem.kinject.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

import static com.wokdsem.kinject.annotations.Provides.Scope.NONE;
import static com.wokdsem.kinject.core.KinjectValues.DEFAULT_NAMED;

@Target(ElementType.METHOD)
public @interface Provides {
	
	String named() default DEFAULT_NAMED;
	
	Scope scope() default NONE;
	
	enum Scope {
		NONE,
		WEAK_SINGLETON,
		SINGLETON
	}
	
}
