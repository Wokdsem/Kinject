package com.wokdsem.kinject.codegen.domains;

public class Include {

	public final String canonicalModuleName;
	public final Reference reference;

	public Include(String canonicalModuleName, Reference reference) {
		this.canonicalModuleName = canonicalModuleName;
		this.reference = reference;
	}

}
