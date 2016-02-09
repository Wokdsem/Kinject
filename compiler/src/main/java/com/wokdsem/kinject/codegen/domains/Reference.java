package com.wokdsem.kinject.codegen.domains;

public class Reference {

	public final String canonicalModuleName;
	public final String method;

	public Reference(String canonicalModuleName, String method) {
		this.canonicalModuleName = canonicalModuleName;
		this.method = method;
	}

}
