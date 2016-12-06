package com.wokdsem.examples.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.wokdsem.examples.DemoApplication;
import com.wokdsem.examples.tools.Logger;
import com.wokdsem.kinject.Kinject;

abstract class BaseActivity extends AppCompatActivity {

	protected Kinject injector;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		injector = ((DemoApplication) getApplication()).injector;
		injector.get(Logger.class)
			.log("Kinject is ready!");
	}

}
