package com.at.test.activity;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;

import com.at.test.R;


public class FrescoActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_fresco);
		Uri uri = Uri
				.parse("http://avatar.csdn.net/3/0/D/1_theone10211024.jpg");
		// SimpleDraweeView draweeView = (Simpled)
		// findViewById(R.id.my_image_view);
		// draweeView.setImageURI(uri);
	}

}
