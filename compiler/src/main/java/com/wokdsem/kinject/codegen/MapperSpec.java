package com.wokdsem.kinject.codegen;

import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeSpec;
import com.wokdsem.kinject.codegen.domains.Graph;
import com.wokdsem.kinject.core.MapperProvider;
import com.wokdsem.kinject.core.ModuleMapper;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static javax.lang.model.element.Modifier.FINAL;
import static javax.lang.model.element.Modifier.PRIVATE;
import static javax.lang.model.element.Modifier.PUBLIC;

class MapperSpec {

	private static final String MAPPER_FIELD = "mapper";
	private static final String MAPPER_INIT_METHOD = "initMapper";

	public static TypeSpec getMapperSpec(Collection<Graph> graphs, String mapperName) {
		Map<String, Integer> moduleMapper = getModuleMapper(graphs);
		return TypeSpec.classBuilder(mapperName)
				.addAnnotation(AnnotationSpec.builder(SuppressWarnings.class).addMember("value", "$S", "unchecked").build())
				.addModifiers(PUBLIC, FINAL)
				.addSuperinterface(MapperProvider.class)
				.addField(ParameterizedTypeName.get(Map.class, Class.class, Integer.class), MAPPER_FIELD, PRIVATE, FINAL)
				.addMethod(getConstructor())
				.addMethod(getMapperInitializerMethod(moduleMapper))
				.addMethod(getModuleMapperMethod(moduleMapper))
				.build();
	}

	private static Map<String, Integer> getModuleMapper(Collection<Graph> graphs) {
		HashMap<String, Integer> moduleMapper = new HashMap<>();
		int mapperValue = 0;
		for (Graph graph : graphs) {
			moduleMapper.put(graph.canonicalModuleName, ++mapperValue);
		}
		return moduleMapper;
	}

	private static MethodSpec getConstructor() {
		return MethodSpec.constructorBuilder()
				.addModifiers(PUBLIC)
				.addStatement("this.$L = new $T()", MAPPER_FIELD, HashMap.class)
				.addStatement("$L()", MAPPER_INIT_METHOD)
				.build();
	}

	private static MethodSpec getMapperInitializerMethod(Map<String, Integer> moduleMapper) {
		MethodSpec.Builder builder = MethodSpec.methodBuilder(MAPPER_INIT_METHOD);
		for (Map.Entry<String, Integer> entry : moduleMapper.entrySet()) {
			builder.addStatement("$L.put($T.class, $L)", MAPPER_FIELD, ClassName.bestGuess(entry.getKey()), entry.getValue());
		}
		return builder.build();
	}

	private static MethodSpec getModuleMapperMethod(Map<String, Integer> moduleMapper) {
		CodeBlock.Builder switchFlow = CodeBlock.builder()
				.beginControlFlow("if(module != null)")
				.beginControlFlow("switch($L.get($L.getClass()))", MAPPER_FIELD, "module");
		for (Map.Entry<String, Integer> entry : moduleMapper.entrySet()) {
			switchFlow
					.add("case $L:\n\t", entry.getValue())
					.addStatement("return new $T(($T)$L)",
							ClassName.bestGuess(MapperNames.getCanonicalMapperModuleName(entry.getKey())),
							ClassName.bestGuess(entry.getKey()),
							"module");
		}
		CodeBlock switchBlock = switchFlow.endControlFlow().endControlFlow().build();
		return MethodSpec.methodBuilder("getModuleMapper")
				.addAnnotation(Override.class)
				.addModifiers(PUBLIC)
				.returns(ModuleMapper.class)
				.addParameter(Object.class, "module")
				.addCode(switchBlock)
				.addStatement("return null")
				.build();
	}

}
