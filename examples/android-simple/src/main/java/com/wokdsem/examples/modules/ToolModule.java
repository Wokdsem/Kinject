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
import static com.wokdsem.kinject.annotations.Provides.Scope.SINGLETON;

@Module
class ToolModule {
	
	@Provides(scope = SINGLETON)
	Display provideDisplay(@Named(APP_CONTEXT) final Context context) {
		return new Display() {
			@Override
			public void showMessage(String msg) {
				Toast toast = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
				toast.show();
			}
		};
	}
	
	@Provides(scope = SINGLETON)
	Logger provideLogger() {
		return new Logger() {
			@Override
			public void log(String msg) {
				Log.d("Kinject!!", msg);
			}
		};
	}
	
}
