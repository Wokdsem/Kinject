package com.wokdsem.examples.modules;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.wokdsem.examples.tools.Display;
import com.wokdsem.examples.tools.Logger;
import com.wokdsem.kinject.annotations.Module;
import com.wokdsem.kinject.annotations.Named;
import com.wokdsem.kinject.annotations.Provides;

import static com.wokdsem.examples.DemoValues.APP_CONTEXT;

@Module
public class ToolModule {

	@Provides(singleton = true)
	public Display provideDisplay(@Named(APP_CONTEXT) final Context context) {
		return new Display() {
			@Override
			public void showMessage(String msg) {
				Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
			}
		};
	}

	@Provides(singleton = true)
	public Logger provideLogger() {
		return new Logger() {
			@Override
			public void log(String msg) {
				Log.d("Kinject!!", msg);
			}
		};
	}

}
