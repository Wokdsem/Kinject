package com.wokdsem.examples;

public class Thermosiphon implements Pump {

	private final Heater heater;

	public Thermosiphon(Heater heater) {
		this.heater = heater;
	}

	@Override
	public void pump() {
		if (heater.isHot()) System.out.println("=> => pumping => =>");
	}

}
