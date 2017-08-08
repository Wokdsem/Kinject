package com.wokdsem.kinject.core.binder;

import com.wokdsem.kinject.core.Provider;
import java.lang.ref.WeakReference;

public class WeakSingletonBinder<T> implements Binder<T> {
	
	private final Provider<T> provider;
	private WeakReference<T> weakReference;
	
	public WeakSingletonBinder(Provider<T> provider) {
		this.provider = provider;
	}
	
	@Override
	public final T bind() {
		T t = getValue();
		if (t == null) {
			synchronized (this) {
				t = getValue();
				if (t == null) {
					t = provider.provide();
					weakReference = new WeakReference<>(t);
				}
			}
		}
		return t;
	}
	
	private synchronized T getValue() {
		return weakReference != null ? weakReference.get() : null;
	}
	
}
