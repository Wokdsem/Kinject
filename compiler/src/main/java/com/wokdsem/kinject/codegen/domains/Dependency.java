package com.wokdsem.kinject.codegen.domains;

public class Dependency {

	public final String canonicalClassName;
	public final String named;

	public Dependency(String canonicalClassName, String named) {
		this.canonicalClassName = canonicalClassName;
		this.named = named;
	}

}
