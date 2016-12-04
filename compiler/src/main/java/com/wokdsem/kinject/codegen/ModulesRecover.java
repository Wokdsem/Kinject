package com.wokdsem.kinject.codegen;

import com.wokdsem.kinject.codegen.domains.Include;
import com.wokdsem.kinject.codegen.domains.Module;
import com.wokdsem.kinject.codegen.domains.Provide;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.Name;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;

class ModulesRecover {

	private static final Class<com.wokdsem.kinject.annotations.Module> moduleClass = com.wokdsem.kinject.annotations.Module.class;

	static Map<String, Module> getModules(Set<? extends Element> moduleElements) throws ProcessorException {
		HashMap<String, Module> modules = new HashMap<>();
		for (Element element : moduleElements) {
			assertAnnotatedModule(element);
			Module module = buildModule((TypeElement) element);
			modules.put(module.canonicalModuleName, module);
		}
		assertIncludes(modules);
		return modules;
	}

	private static void assertAnnotatedModule(Element element) throws ProcessorException {
		assertKind(element);
		assertModifiers(element);
		assertInheritance((TypeElement) element);
	}

	private static void assertKind(Element element) throws ProcessorException {
		ElementKind kind = element.getKind();
		if (kind != ElementKind.CLASS) {
			String errMsg = "%s(%s) must not be annotated as @%s.";
			throw new ProcessorException(errMsg, element.getSimpleName(), kind.name(), moduleClass.getSimpleName());
		}
	}

	private static void assertModifiers(Element element) throws ProcessorException {
		Set<Modifier> modifiers = element.getModifiers();
		if (modifiers.contains(Modifier.PRIVATE) || modifiers.contains(Modifier.ABSTRACT)) {
			throw new ProcessorException("@Module(%s) must not be a [private or abstract] class.",
										 element.getSimpleName());
		}
	}

	private static void assertInheritance(TypeElement element) throws ProcessorException {
		DeclaredType superclass = (DeclaredType) element.getSuperclass();
		Name superClass = ((TypeElement) (superclass.asElement())).getQualifiedName();
		if (!"java.lang.Object".equals(superClass.toString())) {
			throw new ProcessorException("@Module(%s) must not extend from other classes.", element.getSimpleName());
		}
	}

	private static Module buildModule(TypeElement module) throws ProcessorException {
		Name moduleName = module.getQualifiedName();
		Collection<Provide> provides = new LinkedList<>();
		Collection<Include> includes = new LinkedList<>();
		MethodsRecover.getMethods(module, provides, includes);
		com.wokdsem.kinject.annotations.Module moduleAnnotation = module.getAnnotation(moduleClass);
		return new Module.ModuleBuilder(moduleName.toString()).setProvides(provides)
			.setIncludes(includes)
			.setIsCompleted(moduleAnnotation != null && moduleAnnotation.completed())
			.build();
	}

	private static void assertIncludes(Map<String, Module> modules) throws ProcessorException {
		for (Module module : modules.values()) {
			for (Include include : module.includes) {
				if (!modules.containsKey(include.canonicalModuleName)) {
					String errMsg = "@Module(%s) includes an unknown <%s> module.";
					throw new ProcessorException(errMsg, module.canonicalModuleName, include.canonicalModuleName);
				}
			}
		}
	}

}
