package com.wokdsem.examples.modules;

import com.wokdsem.examples.ElectricalHeater;
import com.wokdsem.examples.Heater;
import com.wokdsem.kinject.annotations.Module;
import com.wokdsem.kinject.annotations.Provides;

import static com.wokdsem.kinject.annotations.Provides.Scope.SINGLETON;

@Module
class HeaterModule {
	
	@Provides(scope = SINGLETON)
	Heater provideHeater() {
		return new ElectricalHeater();
	}
	
}
