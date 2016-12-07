package com.wokdsem.examples.modules;

import com.wokdsem.examples.ElectricalHeater;
import com.wokdsem.examples.Heater;
import com.wokdsem.kinject.annotations.Module;
import com.wokdsem.kinject.annotations.Provides;

@Module
class HeaterModule {

	@Provides(singleton = true)
	Heater provideHeater() {
		return new ElectricalHeater();
	}

}
