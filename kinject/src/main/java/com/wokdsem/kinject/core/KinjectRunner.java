package com.wokdsem.kinject.core;

import com.wokdsem.kinject.support.Assertion;

public class KinjectRunner {

	private static final String MAPPER = "kinject.Mapper";
	private static final String ERR_NULL_MODULE = "Non null module allowed to Kinject instantiation.";
	private static final String ERR_NON_VALID_MODULE = "[%s] is a non allowed module to Kinject instantiation.";

	private static MapperProvider mapper;

	public static Injector initializeInjector(Object module) {
		Assertion.assertNonNull(module, ERR_NULL_MODULE);
		ModuleMapper moduleMapper = getMapper().getModuleMapper(module);
		Assertion.assertNonNull(moduleMapper, String.format(ERR_NON_VALID_MODULE, module.getClass().getName()));
		return new KinjectEngine(moduleMapper);
	}

	private synchronized static MapperProvider getMapper() {
		if (mapper == null) {
			try {
				Class<?> mapperClass = Class.<MapperProvider>forName(MAPPER);
				mapper = (MapperProvider) mapperClass.newInstance();
				return mapper;
			} catch (ClassNotFoundException | IllegalAccessException | InstantiationException e) {
				throw new IllegalStateException("Non available mapper to start Kinject.");
			}
		}
		return mapper;
	}

}
