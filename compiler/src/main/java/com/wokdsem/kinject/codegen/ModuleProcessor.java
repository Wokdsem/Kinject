package com.wokdsem.kinject.codegen;

import com.wokdsem.kinject.codegen.domains.Graph;
import com.wokdsem.kinject.codegen.domains.Module;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Messager;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;

import static javax.tools.Diagnostic.Kind.ERROR;

public class ModuleProcessor extends AbstractProcessor {

	@Override
	public SourceVersion getSupportedSourceVersion() {
		return SourceVersion.latestSupported();
	}

	@Override
	public Set<String> getSupportedAnnotationTypes() {
		LinkedHashSet<String> supportedAnnotationTypes = new LinkedHashSet<>();
		supportedAnnotationTypes.add(com.wokdsem.kinject.annotations.Module.class.getCanonicalName());
		return supportedAnnotationTypes;
	}

	@Override
	public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
		try {
			Set<? extends Element> annotatedModules = roundEnv.getElementsAnnotatedWith(com.wokdsem.kinject.annotations.Module.class);
			if (!annotatedModules.isEmpty()) {
				Map<String, Module> modules = ModulesRecover.getModules(annotatedModules);
				Collection<Graph> graphs = GraphBuilder.buildGraphs(modules);
				CodeGen.generateCode(modules, graphs, processingEnv.getFiler());
			}
		} catch (ProcessorException e) {
			error(e.getMessage());
		}
		return true;
	}

	private void error(String msg) {
		Messager messager = processingEnv.getMessager();
		messager.printMessage(ERROR, String.format("[[ KINJECT : ERROR ]] %s", msg));
	}

}
