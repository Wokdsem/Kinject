package com.wokdsem.kinject.core;

import com.wokdsem.kinject.core.binder.Binder;
import com.wokdsem.kinject.support.Locker;

import java.util.HashMap;
import java.util.Map;

class KinjectEngine implements Injector {

	private final Map<String, Binder<?>> binders;
	private final Locker<String> locker;
	private final ModuleMapper mapper;

	public KinjectEngine(ModuleMapper mapper) {
		binders = new HashMap<>();
		locker = new Locker<>();
		this.mapper = mapper;
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T> T inject(Class<T> tClass, String named) {
		String key = getInternalKey(tClass, named);
		Binder tBinder = binders.get(key);
		if (tBinder == null) {
			try {
				locker.lock(key);
				tBinder = binders.get(key);
				if (tBinder == null) {
					tBinder = mapper.buildBinder(tClass, named, this);
					binders.put(key, tBinder);
				}
				locker.release(key);
			} catch (InterruptedException ignored) {
			}
		}
		if (tBinder == null) throw new IllegalArgumentException(String.format("Kinject [ERROR] - %s is not a valid injectable.", key));
		return (T) tBinder.bind();
	}

	private static String getInternalKey(Class tClass, String named) {
		return String.format("%s@%s", tClass.getCanonicalName(), named);
	}

}
