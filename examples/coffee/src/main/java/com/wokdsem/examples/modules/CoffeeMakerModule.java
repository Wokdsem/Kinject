package com.wokdsem.examples.modules;

import com.wokdsem.examples.CoffeeMaker;
import com.wokdsem.examples.Heater;
import com.wokdsem.examples.Pump;
import com.wokdsem.examples.Thermosiphon;
import com.wokdsem.kinject.annotations.Includes;
import com.wokdsem.kinject.annotations.Module;
import com.wokdsem.kinject.annotations.Provides;

@Module(completed = true)
public class CoffeeMakerModule {

	@Includes
	public HeaterModule includeHeaterModule() {
		return new HeaterModule();
	}

	@Provides()
	public Pump providePump(Heater heater) {
		return new Thermosiphon(heater);
	}

	@Provides(singleton = true)
	public CoffeeMaker provideCoffeeMaker(Heater heater, Pump pump) {
		return new CoffeeMaker(heater, pump);
	}

}
