package com.wokdsem.kinject.core;

import com.wokdsem.kinject.core.binder.Binder;
import java.util.HashMap;
import java.util.Map;

class KinjectEngine implements Injector {
	
	private final Map<String, Binder<?>> binders;
	private final Blocker blocker;
	private final ModuleMapper mapper;
	
	KinjectEngine(ModuleMapper mapper) {
		binders = new HashMap<>();
		blocker = new Blocker();
		this.mapper = mapper;
	}
	
	@Override
	@SuppressWarnings({ "unchecked", "ConstantConditions" })
	public <T> T inject(Class<T> tClass, String named) {
		assertArguments(tClass, named);
		String key = getInternalKey(tClass, named);
		Binder tBinder = binders.get(key);
		if (tBinder == null) {
			try {
				blocker.block(key);
				tBinder = binders.get(key);
				if (tBinder == null) {
					tBinder = mapper.buildBinder(tClass, named, this);
					binders.put(key, tBinder);
				}
			} catch (InterruptedException e) {
				throw new IllegalStateException(e);
			} finally {
				blocker.release(key);
			}
		}
		assertBinder(tBinder, key);
		return (T) tBinder.bind();
	}
	
	private static String getInternalKey(Class tClass, String named) {
		return String.format("%s@%s", tClass.getCanonicalName(), named);
	}
	
	private static void assertArguments(Class<?> tClass, String named) {
		if (tClass == null || named == null) {
			onArgumentError("Trying to inject with a null class or named argument.");
		}
	}
	
	private static void assertBinder(Binder binder, String key) {
		if (binder == null) {
			String errMsg = String.format("%s is not a valid injectable.", key);
			onArgumentError(errMsg);
		}
	}
	
	private static void onArgumentError(String errMsg) {
		String illegalErrorMsg = String.format("Kinject [ERROR] - %s", errMsg);
		throw new IllegalArgumentException(illegalErrorMsg);
	}
	
}
