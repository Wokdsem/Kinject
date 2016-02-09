package com.wokdsem.examples.ui;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.wokdsem.examples.R;
import com.wokdsem.examples.tools.Display;

public class HomeActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
		findViewById(R.id.fab).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Display display = injector.get(Display.class);
				display.showMessage("Message from injected display!");
			}
		});
	}

}
