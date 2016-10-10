package com.wokdsem.kinject;

import com.wokdsem.kinject.core.ModuleMapper;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;

public class KinjectTest {

	@Test
	public void instantiate_withMapperModule_kinjectInstance() {
		ModuleMapper mapper = mock(ModuleMapper.class);
		Kinject kinject = Kinject.instantiate(mapper);
		assertNotNull(kinject);
	}
	
}
