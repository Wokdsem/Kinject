package com.wokdsem.kinject.codegen;

import com.google.testing.compile.JavaFileObjects;

import org.junit.Test;

import static com.google.common.truth.Truth.assert_;
import static com.google.testing.compile.JavaSourceSubjectFactory.javaSource;

public class ModuleRecoverTest {

	@Test
	public void moduleKind() {
		String module = "package test;" +
				"import com.wokdsem.kinject.annotations.Module;" +
				"@Module " +
				"public %s M {" +
				"}";
		for (String kind : new String[]{"Enum", "Interface"}) {
			assertCompileFail("test.M", String.format(module, kind));
		}
	}

	@Test
	public void moduleVisibility() {
		String module = "package test;" +
				"import com.wokdsem.kinject.annotations.Module;" +
				"@Module " +
				"class M {" +
				"}";
		assertCompileFail("test.M", module);
	}

	@Test
	public void moduleInheritance() {
		String module = "package test;" +
				"import com.wokdsem.kinject.annotations.Module;" +
				"@Module " +
				"public class M extend String {" +
				"}";
		assertCompileFail("test.M", module);
	}

	@Test
	public void moduleIncludes() {
		String module = "package test;" +
				"import com.wokdsem.kinject.annotations.Module;" +
				"import com.wokdsem.kinject.annotations.Include;" +
				"@Module " +
				"public class M extend String {" +
				"@Include" +
				"public String fakeInclude(){" +
				"return \"\";" +
				"}";
		assertCompileFail("test.M", module);
	}

	private void assertCompileFail(String fullQualifiedName, String module) {
		assert_().about(javaSource())
				.that(JavaFileObjects.forSourceLines(fullQualifiedName, module))
				.processedWith(new ModuleProcessor())
				.failsToCompile();
	}

}
