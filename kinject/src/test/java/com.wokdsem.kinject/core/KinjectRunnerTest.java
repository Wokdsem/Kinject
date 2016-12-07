package com.wokdsem.kinject.core;

import org.junit.Test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;

public class KinjectRunnerTest {

	@Test
	public void initializeInjector_withModuleMapper_injectorInitialized() {
		ModuleMapper mapper = mock(ModuleMapper.class);
		Injector injector = KinjectRunner.initializeInjector(mapper);
		assertNotNull(injector);
	}

	@Test
	public void initializeInjector_withNullMapper_exceptionThrows() {
		try {
			KinjectRunner.initializeInjector(null);
			fail();
		} catch (Exception ignored) {
		}
	}
	
}
