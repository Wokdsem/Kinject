package com.wokdsem.examples;

import com.wokdsem.examples.modules.CoffeeMakerModule;
import com.wokdsem.examples.modules.CoffeeMakerModuleMapper;
import com.wokdsem.kinject.Kinject;

public class CoffeeMaker {

	public static void main(String... args) {
		// Create injector
		CoffeeMakerModule module = new CoffeeMakerModule();                                // Root graph module
		Kinject kinject = Kinject.instantiate(CoffeeMakerModuleMapper.from(module));      // Instantiate injector
		// Injecting
		CoffeeMaker coffeeMaker = kinject.get(CoffeeMaker.class);						// Inject dependency
		coffeeMaker.makeCoffee();
	}

	private final Heater heater;
	private final Pump pump;

	public CoffeeMaker(Heater heater, Pump pump) {
		this.heater = heater;
		this.pump = pump;
	}

	private void makeCoffee() {
		heater.heat();
		pump.pump();
		System.out.println(" [_]P coffee! ");
	}

}
