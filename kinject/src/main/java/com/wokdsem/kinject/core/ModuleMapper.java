package com.wokdsem.kinject.core;

import com.wokdsem.kinject.core.binder.Binder;

public interface ModuleMapper {

	Binder buildBinder(Class tClass, String named, Injector injector);

}
