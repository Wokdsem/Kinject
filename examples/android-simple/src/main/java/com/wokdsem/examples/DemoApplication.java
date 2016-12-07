package com.wokdsem.examples;

import android.app.Application;
import com.wokdsem.examples.modules.AppModule;
import com.wokdsem.kinject.Kinject;

import static com.wokdsem.examples.modules.AppModuleMapper.from;

public class DemoApplication extends Application {

	public Kinject injector;

	@Override
	public void onCreate() {
		super.onCreate();
		AppModule appModule = new AppModule(this);
		injector = Kinject.instantiate(from(appModule));
	}

}
