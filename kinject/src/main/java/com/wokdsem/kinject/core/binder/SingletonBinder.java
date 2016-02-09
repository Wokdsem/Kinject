package com.wokdsem.kinject.core.binder;

import com.wokdsem.kinject.core.Provider;

public class SingletonBinder<T> implements Binder<T> {

	private final Provider<T> provider;
	private T t;

	public SingletonBinder(Provider<T> provider) {
		this.provider = provider;
	}

	@Override
	public final T bind() {
		if (t == null) {
			synchronized (this) {
				if (t == null) t = provider.provide();
			}
		}
		return t;
	}
}
