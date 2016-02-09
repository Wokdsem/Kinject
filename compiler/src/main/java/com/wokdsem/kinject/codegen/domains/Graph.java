package com.wokdsem.kinject.codegen.domains;

import java.util.Collection;

public class Graph {

	public final String canonicalModuleName;
	public final Collection<Include> includes;
	public final Collection<Provide> provides;

	public Graph(String canonicalModuleName, Collection<Include> includes, Collection<Provide> provides) {
		this.canonicalModuleName = canonicalModuleName;
		this.includes = includes;
		this.provides = provides;
	}

}
