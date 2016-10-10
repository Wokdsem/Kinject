package com.wokdsem.kinject.core;

public class KinjectRunner {

	public static Injector initializeInjector(ModuleMapper mapper) {
		assertModule(mapper);
		return new KinjectEngine(mapper);
	}

	private static void assertModule(Object module) {
		if (module == null) {
			throw new IllegalArgumentException("Non null module allowed to Kinject instantiation.");
		}
	}

}
