package com.wokdsem.kinject.codegen;

import com.wokdsem.kinject.codegen.domains.Dependency;
import com.wokdsem.kinject.codegen.domains.Graph;
import com.wokdsem.kinject.codegen.domains.Include;
import com.wokdsem.kinject.codegen.domains.Module;
import com.wokdsem.kinject.codegen.domains.Provide;
import com.wokdsem.kinject.codegen.domains.Reference;

import org.junit.Test;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import static java.util.Collections.singleton;
import static java.util.Collections.singletonMap;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class GraphBuilderTest {

	@Test
	public void graphBuilder_nonCompletedInput_emptyGraph() {
		try {
			Collection<Graph> graphs = GraphBuilder.buildGraphs(Collections.<String, Module>emptyMap());
			assertTrue(graphs.isEmpty());
		} catch (ProcessorException e) {
			fail();
		}
	}

	@Test
	public void graphBuilder_emptyModule_graph() throws ProcessorException {
		Module module = getFakeModule("ModuleName", null, null, true);
		Collection<Graph> graphs = GraphBuilder.buildGraphs(singletonMap(module.canonicalModuleName, module));
		assertEquals(graphs.size(), 1);
	}

	@Test
	public void graphBuilder_repeatInclude_throwError() {
		try {
			String moduleName = "ModuleName";
			String includeModuleName = "IncludeModule";
			Include include = getFakeInclude(includeModuleName);
			Module includeModule = getFakeModule(includeModuleName, null, null, false);
			Module fakeModule = getFakeModule(moduleName, null, Arrays.asList(include, include), true);
			Map<String, Module> modules = new HashMap<>();
			modules.put(fakeModule.canonicalModuleName, fakeModule);
			modules.put(includeModule.canonicalModuleName, includeModule);
			GraphBuilder.buildGraphs(modules);
			fail();
		} catch (ProcessorException ignored) {
		}
	}

	@Test
	public void graphBuilder_repeatProvide_throwError() {
		try {
			String moduleName = "ModuleName";
			String includeModuleName = "IncludeModule";
			Collection<Provide> repeatProvide = singleton(getFakeProvide("P", "n", null));
			Module includeModule = getFakeModule(includeModuleName, repeatProvide, null, false);
			Collection<Include> includes = singleton(getFakeInclude(includeModuleName));
			Module fakeModule = getFakeModule(moduleName, repeatProvide, includes, true);
			Map<String, Module> modules = new HashMap<>();
			modules.put(fakeModule.canonicalModuleName, fakeModule);
			modules.put(includeModule.canonicalModuleName, includeModule);
			GraphBuilder.buildGraphs(modules);
			fail();
		} catch (ProcessorException ignored) {
		}
	}

	@Test
	public void graphBuilder_unknownDependency_throwError() {
		try {
			String moduleName = "ModuleName";
			Collection<Provide> provides = singleton(getFakeProvide("P", "n", singleton(new Dependency("Unknown", "?"))));
			Module fakeModule = getFakeModule(moduleName, provides, null, true);
			GraphBuilder.buildGraphs(singletonMap(moduleName, fakeModule));
			fail();
		} catch (ProcessorException ignored) {
		}
	}

	private static Provide getFakeProvide(String provideName, String named, Collection<Dependency> dependencies) {
		if (dependencies == null) dependencies = new LinkedList<>();
		return new Provide.ProvideBuilder(provideName, named, DF_REFERENCE)
				.setDependencies(dependencies)
				.build();
	}

	private static Include getFakeInclude(String includeName) {
		return new Include(includeName, DF_REFERENCE);
	}

	private static Module getFakeModule(String name, Collection<Provide> provides, Collection<Include> includes, boolean isCompleted) {
		if (provides == null) provides = new LinkedList<>();
		if (includes == null) includes = new LinkedList<>();
		return new Module.ModuleBuilder(name)
				.setProvides(provides)
				.setIncludes(includes)
				.setIsCompleted(isCompleted)
				.build();
	}

	private static final Reference DF_REFERENCE = new Reference("Ref", "met");

}
