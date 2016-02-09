package com.wokdsem.kinject.core;

public interface Injector {

	<T> T inject(Class<T> tClass, String named);

}
