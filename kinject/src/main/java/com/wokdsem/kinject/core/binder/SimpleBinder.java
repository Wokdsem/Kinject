package com.wokdsem.kinject.core.binder;

import com.wokdsem.kinject.core.Provider;

public class SimpleBinder<T> implements Binder<T> {

	private final Provider<T> provider;

	public SimpleBinder(Provider<T> provider) {
		this.provider = provider;
	}

	@Override
	public T bind() {
		return provider.provide();
	}

}
