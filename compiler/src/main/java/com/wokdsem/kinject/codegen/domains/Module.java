package com.wokdsem.kinject.codegen.domains;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class Module {

	public final String canonicalModuleName;
	public final List<Provide> provides;
	public final List<Include> includes;
	public final boolean isCompleted;

	private Module(ModuleBuilder builder) {
		this.canonicalModuleName = builder.canonicalModuleName;
		this.provides = builder.provides;
		this.includes = builder.includes;
		this.isCompleted = builder.isCompleted;
	}

	public static class ModuleBuilder {

		private final String canonicalModuleName;
		private final List<Provide> provides;
		private final List<Include> includes;
		private boolean isCompleted;

		public ModuleBuilder(String canonicalModuleName) {
			this.canonicalModuleName = canonicalModuleName;
			this.provides = new LinkedList<>();
			this.includes = new LinkedList<>();
		}

		public ModuleBuilder setProvides(Collection<Provide> provides) {
			this.provides.clear();
			this.provides.addAll(provides);
			return this;
		}

		public ModuleBuilder setIncludes(Collection<Include> includes) {
			this.includes.clear();
			this.includes.addAll(includes);
			return this;
		}

		public ModuleBuilder setIsCompleted(boolean isCompleted) {
			this.isCompleted = isCompleted;
			return this;
		}

		public Module build() {
			return new Module(this);
		}

	}

}
