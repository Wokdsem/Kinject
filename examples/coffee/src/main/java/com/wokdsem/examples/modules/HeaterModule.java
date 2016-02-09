package com.wokdsem.examples.modules;

import com.wokdsem.examples.ElectricalHeater;
import com.wokdsem.examples.Heater;
import com.wokdsem.kinject.annotations.Module;
import com.wokdsem.kinject.annotations.Provides;

@Module
public class HeaterModule {

	@Provides(singleton = true)
	public Heater provideHeater() {
		return new ElectricalHeater();
	}

}
