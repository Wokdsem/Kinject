package com.wokdsem.kinject.codegen;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeSpec;
import com.wokdsem.kinject.codegen.domains.Dependency;
import com.wokdsem.kinject.codegen.domains.Module;
import com.wokdsem.kinject.codegen.domains.Provide;
import com.wokdsem.kinject.core.Injector;
import com.wokdsem.kinject.core.Provider;
import com.wokdsem.kinject.core.binder.Binder;
import com.wokdsem.kinject.core.binder.SimpleBinder;
import com.wokdsem.kinject.core.binder.SingletonBinder;

import java.util.LinkedList;
import java.util.List;

import static javax.lang.model.element.Modifier.FINAL;
import static javax.lang.model.element.Modifier.PUBLIC;
import static javax.lang.model.element.Modifier.STATIC;

class ModuleAdapterSpec {

	static TypeSpec getModuleAdapterSpec(Module module, String adapterName) {
		List<MethodSpec> methods = getMethods(module.canonicalModuleName, module.provides);
		return TypeSpec.classBuilder(adapterName)
			.addModifiers(PUBLIC, FINAL)
			.addMethods(methods)
			.build();
	}

	private static List<MethodSpec> getMethods(String moduleName, List<Provide> provides) {
		List<MethodSpec> methods = new LinkedList<>();
		for (Provide provide : provides) {
			ClassName className = ClassName.bestGuess(provide.canonicalProvideClassName);
			String methodName = MapperNames.getBindMethodName(provide.canonicalProvideClassName, provide.named);
			MethodSpec.Builder builder = MethodSpec.methodBuilder(methodName)
				.addModifiers(PUBLIC, STATIC)
				.returns(ParameterizedTypeName.get(ClassName.get(Binder.class), className))
				.addParameter(ClassName.bestGuess(moduleName), "module", FINAL)
				.addParameter(Injector.class, "injector", FINAL)
				.addStatement("return new $T($L)", ParameterizedTypeName.get(
					ClassName.get(provide.isSingleton ? SingletonBinder.class : SimpleBinder.class), className),
							  addInternalStatement(provide));
			methods.add(builder.build());
		}
		return methods;
	}

	private static TypeSpec addInternalStatement(Provide provide) {
		ClassName className = ClassName.bestGuess(provide.canonicalProvideClassName);
		List<Dependency> dependencies = provide.dependencies;
		int size = dependencies.size();
		StringBuilder code = new StringBuilder("return module.$L(");
		for (int i = 0; i < size; i++) {
			Dependency dependency = dependencies.get(i);
			if (i > 0) {
				code.append(", ");
			}
			code.append("\ninjector.inject(")
				.append(dependency.canonicalClassName)
				.append(".class, \"")
				.append(dependency.named)
				.append("\")");
		}
		String statement = code.append(")")
			.toString();
		return TypeSpec.anonymousClassBuilder("")
			.addSuperinterface(ParameterizedTypeName.get(ClassName.get(Provider.class), className))
			.addMethod(MethodSpec.methodBuilder("provide")
						   .addAnnotation(Override.class)
						   .addModifiers(PUBLIC)
						   .returns(className)
						   .addStatement(statement, provide.reference.method)
						   .build())
			.build();
	}

}
