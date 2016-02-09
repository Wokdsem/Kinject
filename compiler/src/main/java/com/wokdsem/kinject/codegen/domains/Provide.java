package com.wokdsem.kinject.codegen.domains;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class Provide {

	public final String canonicalProvideClassName;
	public final String named;
	public final Reference reference;
	public final List<Dependency> dependencies;
	public final boolean isSingleton;

	private Provide(ProvideBuilder builder) {
		this.canonicalProvideClassName = builder.canonicalProvideClassName;
		this.named = builder.named;
		this.reference = builder.reference;
		this.dependencies = builder.dependencies;
		this.isSingleton = builder.isSingleton;
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof Provide) {
			Provide otherProvide = (Provide) o;
			return canonicalProvideClassName.equals(otherProvide.canonicalProvideClassName)
					&& named.equals(otherProvide.named);
		}
		return false;
	}

	@Override
	public int hashCode() {
		int result = canonicalProvideClassName.hashCode();
		result = 31 * result + named.hashCode();
		return result;
	}

	public static class ProvideBuilder {

		private final String canonicalProvideClassName;
		private final String named;
		private final Reference reference;
		private final List<Dependency> dependencies;
		private boolean isSingleton;

		public ProvideBuilder(String canonicalProvideClassName, String named, Reference reference) {
			this.canonicalProvideClassName = canonicalProvideClassName;
			this.named = named;
			this.reference = reference;
			this.dependencies = new LinkedList<>();
		}

		public ProvideBuilder setDependencies(Collection<Dependency> dependencies) {
			this.dependencies.clear();
			this.dependencies.addAll(dependencies);
			return this;
		}

		public ProvideBuilder setIsSingleton(boolean isSingleton) {
			this.isSingleton = isSingleton;
			return this;
		}

		public Provide build() {
			return new Provide(this);
		}

	}

}
