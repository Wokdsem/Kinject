package com.wokdsem.kinject.codegen;

import com.wokdsem.kinject.codegen.domains.Dependency;
import com.wokdsem.kinject.codegen.domains.Graph;
import com.wokdsem.kinject.codegen.domains.Include;
import com.wokdsem.kinject.codegen.domains.Module;
import com.wokdsem.kinject.codegen.domains.Provide;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

class GraphBuilder {

	static Collection<Graph> buildGraphs(Map<String, Module> modules) throws ProcessorException {
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
		Queue<String> safeProvides = new LinkedList<>();
		Map<String, Integer> depCounts = new HashMap<>();
		Map<String, List<String>> dependsOf = new HashMap<>();
		for (Provide provide : graph.provides) {
			List<Dependency> dependencies = provide.dependencies;
			String provideKey = getKey(provide.canonicalProvideClassName, provide.named);
			if (dependencies.isEmpty()) {
				safeProvides.add(provideKey);
			} else {
				depCounts.put(provideKey, dependencies.size());
				for (Dependency dependency : dependencies) {
					String dependencyKey = getKey(dependency.canonicalClassName, dependency.named);
					getDepends(dependencyKey, dependsOf).add(provideKey);
					if (!keys.contains(dependencyKey)) {
						String errMsg = "Unknown dependency(%s) on @Provides(%s) building the graph(%s).";
						throw new ProcessorException(errMsg, dependencyKey, provideKey, graph.canonicalModuleName);
					}
				}
			}
		}
		if (!isCycleFree(safeProvides, depCounts, dependsOf)) {
			throw new ProcessorException("Cyclic graph detected building the graph(%s).", graph.canonicalModuleName);
		}
	}

	private static boolean isCycleFree(Queue<String> safeProvides, Map<String, Integer> depCounts,
									   Map<String, List<String>> dependsOf) {
		while (!safeProvides.isEmpty()) {
			String safeProvide = safeProvides.poll();
			List<String> depends = dependsOf.remove(safeProvide);
			if (depends != null) {
				for (String depend : depends) {
					int count = depCounts.remove(depend) - 1;
					if (count == 0) {
						safeProvides.add(depend);
					} else {
						depCounts.put(depend, count);
					}
				}
			}
		}
		return depCounts.isEmpty();
	}

	private static Set<String> getProvideKeys(Graph graph) throws ProcessorException {
		HashSet<String> keys = new HashSet<>();
		for (Provide provide : graph.provides) {
			String key = getKey(provide.canonicalProvideClassName, provide.named);
			if (!keys.add(key)) {
				String errMsg = "Duplicated @Provided(%s) building the graph(%s).";
				throw new ProcessorException(errMsg, key, graph.canonicalModuleName);
			}
		}
		return keys;
	}

	private static String getKey(String canonicalName, String named) {
		return String.format("%s@%s", canonicalName, named);
	}

	private static List<String> getDepends(String key, Map<String, List<String>> dependsOf) {
		List<String> depends = dependsOf.get(key);
		if (depends == null) {
			depends = new LinkedList<>();
			dependsOf.put(key, depends);
		}
		return depends;
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
			} else {
				String errMsg = "Multiple @Includes(%s) building graph to @Module(%s).";
				throw new ProcessorException(errMsg, pendingModule.canonicalModuleName, module.canonicalModuleName);
			}
		}
		return new Graph(module.canonicalModuleName, includes, provides);
	}

}
