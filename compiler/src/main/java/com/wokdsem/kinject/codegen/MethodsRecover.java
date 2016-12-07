package com.wokdsem.kinject.codegen;

import com.wokdsem.kinject.annotations.Includes;
import com.wokdsem.kinject.annotations.Named;
import com.wokdsem.kinject.annotations.Provides;
import com.wokdsem.kinject.codegen.domains.Dependency;
import com.wokdsem.kinject.codegen.domains.Include;
import com.wokdsem.kinject.codegen.domains.Provide;
import com.wokdsem.kinject.codegen.domains.Reference;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.Name;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;

import static com.wokdsem.kinject.core.KinjectValues.DEFAULT_NAMED;
import static javax.lang.model.element.Modifier.ABSTRACT;
import static javax.lang.model.element.Modifier.PRIVATE;
import static javax.lang.model.element.Modifier.STATIC;

class MethodsRecover {

	static void getMethods(TypeElement module, Collection<Provide> provides,
						   Collection<Include> includes) throws ProcessorException {
		for (Element element : module.getEnclosedElements()) {
			Provides provideAnnotation = element.getAnnotation(Provides.class);
			Includes includeAnnotation = element.getAnnotation(Includes.class);
			if (provideAnnotation != null || includeAnnotation != null) {
				ExecutableElement executableElement = (ExecutableElement) element;
				assertExecutableElement(module, executableElement);
				if (provideAnnotation != null) {
					provides.add(buildProvide(module, executableElement, provideAnnotation));
				} else {
					includes.add(buildInclude(module, executableElement));
				}
			}
		}
	}

	private static Provide buildProvide(TypeElement module, ExecutableElement provideElement,
										Provides provides) throws ProcessorException {
		TypeElement returnType = getReturnType(module, provideElement);
		Name className = returnType.getQualifiedName();
		Reference reference = new Reference(module.getQualifiedName()
												.toString(), provideElement.getSimpleName()
												.toString());
		List<Dependency> dependencies = getDependencies(module, provideElement);
		return new Provide.ProvideBuilder(className.toString(), provides.named(), reference).setDependencies(
			dependencies)
			.setIsSingleton(provides.singleton())
			.build();
	}

	private static List<Dependency> getDependencies(Element module,
													ExecutableElement executableElement) throws ProcessorException {
		List<Dependency> dependencies = new LinkedList<>();
		for (VariableElement param : executableElement.getParameters()) {
			TypeMirror paramType = param.asType();
			if (paramType.getKind() != TypeKind.DECLARED) {
				String errMsg = "No valid parameter(%s) from %s.%s() method.";
				throw new ProcessorException(errMsg, param.getSimpleName(), module.getSimpleName(),
											 executableElement.getSimpleName());
			}
			String tClass = ((TypeElement) ((DeclaredType) paramType).asElement()).getQualifiedName()
				.toString();
			Named namedAnnotation = param.getAnnotation(Named.class);
			String named = namedAnnotation == null ? DEFAULT_NAMED : namedAnnotation.value();
			dependencies.add(new Dependency(tClass, named));
		}
		return dependencies;
	}

	private static Include buildInclude(TypeElement module,
										ExecutableElement includeElement) throws ProcessorException {
		List<? extends VariableElement> parameters = includeElement.getParameters();
		if (!parameters.isEmpty()) {
			String errMsg = "No valid @Includes value from %s.%s() method. @Includes must not have parameters.";
			throw new ProcessorException(errMsg);
		}
		String moduleId = getReturnType(module, includeElement).getQualifiedName()
			.toString();
		Reference reference = new Reference(module.getQualifiedName()
												.toString(), includeElement.getSimpleName()
												.toString());
		return new Include(moduleId, reference);
	}

	private static TypeElement getReturnType(Element module,
											 ExecutableElement executableElement) throws ProcessorException {
		TypeMirror returnType = executableElement.getReturnType();
		if (returnType.getKind() != TypeKind.DECLARED) {
			throw new ProcessorException("No valid @Includes value from %s.%s() method.", module.getSimpleName(),
										 executableElement.getSimpleName());
		}
		return ((TypeElement) ((DeclaredType) returnType).asElement());
	}

	private static void assertExecutableElement(Element module,
												ExecutableElement executableElement) throws ProcessorException {
		Set<Modifier> modifiers = executableElement.getModifiers();
		if (modifiers.contains(PRIVATE)) {
			String errMsg = "%s.%s() is private method. [@Provides or @Includes] must not be a private method.";
			throw new ProcessorException(errMsg, module.getSimpleName(), executableElement.getSimpleName());
		}
		if (modifiers.contains(ABSTRACT)) {
			String errMsg = "%s.%s() is abstract method. [@Provides or @Includes] must not be an abstract method.";
			throw new ProcessorException(errMsg, module.getSimpleName(), executableElement.getSimpleName());
		}
		if (modifiers.contains(STATIC)) {
			String errMsg = "%s.%s() is static method. [@Provides or @Includes] must not be a static method.";
			throw new ProcessorException(errMsg, module.getSimpleName(), executableElement.getSimpleName());
		}
		if (!executableElement.getThrownTypes()
			.isEmpty()) {
			String errMsg = "%s.%s() throws an exception. [@Provides or @Includes] must not throw exceptions.";
			throw new ProcessorException(errMsg, module.getSimpleName(), executableElement.getSimpleName());
		}
	}

}
