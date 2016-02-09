package com.wokdsem.kinject.codegen;

import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.TypeSpec;
import com.wokdsem.kinject.codegen.domains.Graph;
import com.wokdsem.kinject.codegen.domains.Module;

import java.io.IOException;
import java.util.Collection;
import java.util.Map;

import javax.annotation.Generated;
import javax.annotation.processing.Filer;

import static com.wokdsem.kinject.codegen.MapperSpec.getMapperSpec;
import static com.wokdsem.kinject.codegen.ModuleAdapterSpec.getModuleAdapterSpec;
import static com.wokdsem.kinject.codegen.ModuleMapperSpec.getModuleMapperSpec;

class CodeGen {

	private static final String CODE_GEN = CodeGen.class.getCanonicalName();
	private static final String MAPPER_PACKAGE = "kinject";
	private static final String MAPPER_NAME = "Mapper";

	public static void generateCode(Map<String, Module> modules, Collection<Graph> graphs, Filer filer) throws ProcessorException {
		generateModuleAdapters(modules.values(), filer);
		generateModuleMappers(modules, graphs, filer);
		generateMapper(graphs, filer);
	}

	private static void generateModuleAdapters(Collection<Module> modules, Filer filer) throws ProcessorException {
		for (Module module : modules) {
			String adapterName = MapperNames.getSimpleAdapterModuleName(module.canonicalModuleName);
			TypeSpec adapterClass = getModuleAdapterSpec(module, adapterName);
			writeClassFile(getPackageName(module.canonicalModuleName), adapterClass, filer);
		}
	}

	private static void generateModuleMappers(Map<String, Module> modules, Collection<Graph> graphs, Filer filer) throws ProcessorException {
		for (Graph graph : graphs) {
			Module mainModule = modules.get(graph.canonicalModuleName);
			String moduleMapperName = MapperNames.getSimpleMapperModuleName(mainModule.canonicalModuleName);
			TypeSpec moduleMapperClass = getModuleMapperSpec(modules, graph, moduleMapperName);
			writeClassFile(getPackageName(mainModule.canonicalModuleName), moduleMapperClass, filer);
		}
	}

	private static void generateMapper(Collection<Graph> graphs, Filer filer) throws ProcessorException {
		TypeSpec mapperClass = getMapperSpec(graphs, MAPPER_NAME);
		writeClassFile(MAPPER_PACKAGE, mapperClass, filer);
	}

	private static String getPackageName(String baseName) {
		return baseName.substring(0, baseName.lastIndexOf("."));
	}

	private static void writeClassFile(String packageName, TypeSpec classSpec, Filer filer) throws ProcessorException {
		try {
			AnnotationSpec generatedAnnotation = AnnotationSpec.builder(Generated.class).addMember("value", "$S", CODE_GEN).build();
			classSpec = classSpec.toBuilder().addAnnotation(generatedAnnotation).build();
			JavaFile javaFile = JavaFile.builder(packageName, classSpec).skipJavaLangImports(true).build();
			javaFile.writeTo(filer);
		} catch (IOException e) {
			throw new ProcessorException("Can not generate code file %s.%s.", packageName, classSpec.name);
		}
	}

}
