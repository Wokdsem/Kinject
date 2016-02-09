package com.wokdsem.examples;

import android.app.Application;
import android.util.Log;

import com.wokdsem.examples.modules.AppModule;
import com.wokdsem.kinject.Kinject;

public class DemoApplication extends Application {

	public Kinject injector;

	@Override
	public void onCreate() {
		super.onCreate();
		long t = System.nanoTime();
		AppModule appModule = new AppModule(this);
		injector = Kinject.instantiate(appModule);
		t = System.nanoTime() - t;
		Log.d("kinject", "T: " + t);
	}

}
