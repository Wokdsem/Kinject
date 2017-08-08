package com.wokdsem.examples.modules;

import com.wokdsem.examples.CoffeeMaker;
import com.wokdsem.examples.Heater;
import com.wokdsem.examples.Pump;
import com.wokdsem.examples.Thermosiphon;
import com.wokdsem.kinject.annotations.Includes;
import com.wokdsem.kinject.annotations.Module;
import com.wokdsem.kinject.annotations.Provides;

import static com.wokdsem.kinject.annotations.Provides.Scope.SINGLETON;

@Module(completed = true)
public class CoffeeMakerModule {
	
	@Includes
	HeaterModule includeHeaterModule() {
		return new HeaterModule();
	}
	
	@Provides()
	Pump providePump(Heater heater) {
		return new Thermosiphon(heater);
	}
	
	@Provides(scope = SINGLETON)
	CoffeeMaker provideCoffeeMaker(Heater heater, Pump pump) {
		return new CoffeeMaker(heater, pump);
	}
	
}
