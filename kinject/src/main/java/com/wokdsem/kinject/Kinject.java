package com.wokdsem.kinject;

import com.wokdsem.kinject.core.Injector;
import com.wokdsem.kinject.core.KinjectRunner;

import static com.wokdsem.kinject.core.KinjectValues.DEFAULT_NAMED;

public class Kinject {

	public static Kinject instantiate(Object module) {
		Injector injector = KinjectRunner.initializeInjector(module);
		return new Kinject(injector);
	}

	private final Injector injector;

	private Kinject(Injector injector) {
		this.injector = injector;
	}

	public <T> T get(Class<T> tClass) {
		return get(tClass, DEFAULT_NAMED);
	}

	public <T> T get(Class<T> tClass, String named) {
		return injector.inject(tClass, named);
	}

}
