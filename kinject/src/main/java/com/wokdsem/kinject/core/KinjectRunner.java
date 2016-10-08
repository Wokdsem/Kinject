package com.wokdsem.kinject.core;

public class KinjectRunner {

	private static final String MAPPER = "kinject.Mapper";

	private static MapperProvider mapper;

	public static Injector initializeInjector(Object module) {
		assertModule(module);
		ModuleMapper moduleMapper = recoverModuleMapper(module);
		return new KinjectEngine(moduleMapper);
	}

	private static void assertModule(Object module) {
		if (module == null) {
			throw new IllegalArgumentException("Non null module allowed to Kinject instantiation.");
		}
	}

	private static ModuleMapper recoverModuleMapper(Object module) {
		MapperProvider mapper = getMapper();
		ModuleMapper moduleMapper = mapper.getModuleMapper(module);
		assertModuleMapper(moduleMapper, module);
		return moduleMapper;
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

	private static void assertModuleMapper(ModuleMapper moduleMapper, Object module) {
		if (moduleMapper == null) {
			Class<?> moduleClass = module.getClass();
			String moduleName = moduleClass.getName();
			String errMsg = String.format("[%s] is a non allowed module to Kinject instantiation.", moduleName);
			throw new IllegalArgumentException(errMsg);
		}
	}

}
