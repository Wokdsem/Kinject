package com.wokdsem.kinject.codegen;

import com.wokdsem.kinject.codegen.domains.Dependency;
import com.wokdsem.kinject.codegen.domains.Graph;
import com.wokdsem.kinject.codegen.domains.Include;
import com.wokdsem.kinject.codegen.domains.Module;
import com.wokdsem.kinject.codegen.domains.Provide;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

class GraphBuilder {

	public static Collection<Graph> buildGraphs(Map<String, Module> modules) throws ProcessorException {
		LinkedList<Graph> graphs = new LinkedList<>();
		for (Module module : modules.values()) {
			if (module.isCompleted) {
				Graph graph = buildGraph(modules, module);
				validateGraph(graph);
				graphs.add(graph);
			}
		}
		return graphs;
	}

	private static void validateGraph(Graph graph) throws ProcessorException {
		Set<String> keys = getProvideKeys(graph);
		for (Provide provide : graph.provides) {
			for (Dependency dependency : provide.dependencies) {
				String dependencyKey = getKey(dependency.canonicalClassName, dependency.named);
				if (!keys.contains(dependencyKey)) {
					String provideKey = getKey(provide.canonicalProvideClassName, provide.named);
					throw new ProcessorException("Unknown dependency(%s) on @Provides(%s) building graph(%s).", dependencyKey, provideKey, graph.canonicalModuleName);
				}
			}
		}
	}

	private static Set<String> getProvideKeys(Graph graph) throws ProcessorException {
		HashSet<String> keys = new HashSet<>();
		for (Provide provide : graph.provides) {
			String key = getKey(provide.canonicalProvideClassName, provide.named);
			if (!keys.add(key)) throw new ProcessorException("Duplicated @Provided(%s) building graph(%s).", key, graph.canonicalModuleName);
		}
		return keys;
	}

	private static String getKey(String canonicalName, String named) {
		return String.format("%s@%s", canonicalName, named);
	}

	private static Graph buildGraph(Map<String, Module> modules, Module module) throws ProcessorException {
		HashSet<String> added = new HashSet<>();
		LinkedList<Include> includes = new LinkedList<>();
		LinkedList<Provide> provides = new LinkedList<>();
		LinkedList<Module> pendingModules = new LinkedList<>(Collections.singletonList(module));
		while (!pendingModules.isEmpty()) {
			Module pendingModule = pendingModules.remove();
			if (added.add(pendingModule.canonicalModuleName)) {
				provides.addAll(pendingModule.provides);
				for (Include include : pendingModule.includes) {
					includes.add(include);
					pendingModules.add(modules.get(include.canonicalModuleName));
				}
			} else throw new ProcessorException("Multiple @Includes(%s) building graph to @Module(%s).", pendingModule.canonicalModuleName, module.canonicalModuleName);
		}
		return new Graph(module.canonicalModuleName, includes, provides);
	}

}
