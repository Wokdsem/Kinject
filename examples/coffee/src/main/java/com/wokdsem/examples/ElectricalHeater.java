package com.wokdsem.examples;

public class ElectricalHeater implements Heater {

	private boolean isHot;

	@Override
	public void heat() {
		isHot = true;
		System.out.println("~~~ Heating ~~~");
	}

	@Override
	public boolean isHot() {
		return isHot;
	}

}
