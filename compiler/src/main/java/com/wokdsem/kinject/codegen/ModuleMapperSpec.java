package com.wokdsem.kinject.codegen;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeSpec;
import com.wokdsem.kinject.codegen.domains.Graph;
import com.wokdsem.kinject.codegen.domains.Include;
import com.wokdsem.kinject.codegen.domains.Module;
import com.wokdsem.kinject.codegen.domains.Provide;
import com.wokdsem.kinject.codegen.domains.Reference;
import com.wokdsem.kinject.core.Injector;
import com.wokdsem.kinject.core.ModuleMapper;
import com.wokdsem.kinject.core.binder.Binder;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import static com.wokdsem.kinject.codegen.MapperNames.getBindMethodName;
import static com.wokdsem.kinject.codegen.MapperNames.getCanonicalAdapterModuleName;
import static com.wokdsem.kinject.codegen.MapperNames.getFieldName;
import static javax.lang.model.element.Modifier.FINAL;
import static javax.lang.model.element.Modifier.PRIVATE;
import static javax.lang.model.element.Modifier.PUBLIC;

class ModuleMapperSpec {

	static TypeSpec getModuleMapperSpec(Map<String, Module> modules, Graph graph, String moduleMapperName) {
		Map<String, Integer> depMapper = getDepMapper(graph);
		return TypeSpec.classBuilder(moduleMapperName)
			.addModifiers(PUBLIC, FINAL)
			.addSuperinterface(ModuleMapper.class)
			.addField(ParameterizedTypeName.get(Map.class, Class.class, Integer.class), "keys", PRIVATE, FINAL)
			.addFields(getModuleFields(modules, graph))
			.addMethod(getConstructor(modules, graph))
			.addMethod(getKeysInitializerMethod(depMapper))
			.addMethod(getBinderMethod(modules, graph, depMapper))
			.build();
	}

	private static Map<String, Integer> getDepMapper(Graph graph) {
		HashMap<String, Integer> mapper = new HashMap<>();
		int identifier = 0;
		for (Provide provide : graph.provides) {
			mapper.put(provide.canonicalProvideClassName, ++identifier);
		}
		return mapper;
	}

	private static Iterable<FieldSpec> getModuleFields(Map<String, Module> modules, Graph graph) {
		LinkedList<FieldSpec> fieldSpecs = new LinkedList<>();
		fieldSpecs.add(getModuleField(modules.get(graph.canonicalModuleName)));
		for (Include include : graph.includes) {
			fieldSpecs.add(getModuleField(modules.get(include.canonicalModuleName)));
		}
		return fieldSpecs;
	}

	private static FieldSpec getModuleField(Module module) {
		ClassName fieldClassName = ClassName.bestGuess(module.canonicalModuleName);
		String fieldName = getFieldName(module.canonicalModuleName);
		return FieldSpec.builder(fieldClassName, fieldName, PRIVATE, FINAL)
			.build();
	}

	private static MethodSpec getConstructor(Map<String, Module> modules, Graph graph) {
		String initStatement = "this.$L = $L";
		Module mainModule = modules.get(graph.canonicalModuleName);
		String fieldName = getFieldName(mainModule.canonicalModuleName);
		CodeBlock.Builder statementBuilder = CodeBlock.builder()
			.addStatement(initStatement, fieldName, fieldName);
		for (Include include : graph.includes) {
			Reference includeReference = include.reference;
			Module reference = modules.get(includeReference.canonicalModuleName);
			String var = getFieldName(include.canonicalModuleName);
			String value = String.format("%s.%s()", getFieldName(reference.canonicalModuleName),
										 includeReference.method);
			statementBuilder.addStatement(initStatement, var, value);
		}
		return MethodSpec.constructorBuilder()
			.addModifiers(PUBLIC)
			.addParameter(ClassName.bestGuess(mainModule.canonicalModuleName), fieldName)
			.addCode(statementBuilder.build())
			.addStatement("this.keys = new $T<>()", HashMap.class)
			.addStatement("initKeys()")
			.build();
	}

	private static MethodSpec getKeysInitializerMethod(Map<String, Integer> mapper) {
		CodeBlock.Builder block = CodeBlock.builder();
		for (Map.Entry<String, Integer> entry : mapper.entrySet()) {
			block.addStatement("keys.put($L.class, $L)", entry.getKey(), entry.getValue());
		}
		return MethodSpec.methodBuilder("initKeys")
			.addModifiers(PRIVATE)
			.addCode(block.build())
			.build();
	}

	private static MethodSpec getBinderMethod(Map<String, Module> modules, Graph graph, Map<String, Integer> mapper) {
		CodeBlock.Builder switchFlow = CodeBlock.builder()
			.beginControlFlow("switch(keys.get(tClass) + named)");
		for (Provide provide : graph.provides) {
			Reference reference = provide.reference;
			Module module = modules.get(reference.canonicalModuleName);
			switchFlow.add("case \"$L$L\":\n\t", mapper.get(provide.canonicalProvideClassName), provide.named)
				.addStatement("return $T.$L($L, injector)",
							  ClassName.bestGuess(getCanonicalAdapterModuleName(module.canonicalModuleName)),
							  getBindMethodName(provide.canonicalProvideClassName, provide.named),
							  getFieldName(module.canonicalModuleName));
		}
		CodeBlock switchBlock = switchFlow.endControlFlow()
			.build();
		return MethodSpec.methodBuilder("buildBinder")
			.addAnnotation(Override.class)
			.addModifiers(PUBLIC)
			.returns(Binder.class)
			.addParameter(Class.class, "tClass")
			.addParameter(String.class, "named")
			.addParameter(Injector.class, "injector")
			.addCode(switchBlock)
			.addStatement("return null")
			.build();
	}

}
