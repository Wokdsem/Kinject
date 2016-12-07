package com.wokdsem.examples.modules;

import android.content.Context;

import com.wokdsem.examples.DemoApplication;
import com.wokdsem.kinject.annotations.Includes;
import com.wokdsem.kinject.annotations.Module;
import com.wokdsem.kinject.annotations.Provides;

import static com.wokdsem.examples.DemoValues.APP_CONTEXT;

@Module(completed = true)
public class AppModule {

	private final DemoApplication application;

	public AppModule(DemoApplication application) {
		this.application = application;
	}

	@Includes
	ToolModule includeToolModule() {
		return new ToolModule();
	}

	@Provides(named = APP_CONTEXT)
	Context provideApplicationContext() {
		return application;
	}

}
