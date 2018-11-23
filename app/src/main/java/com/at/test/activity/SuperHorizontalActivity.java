package com.at.test.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.at.test.R;
import com.at.test.views.SuperHorizontalScrollView;


public class SuperHorizontalActivity extends Activity {

	Button mt;
	SuperHorizontalScrollView mScrollView;
	int index = 0;
	boolean isF = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_super_horizal);

		mScrollView = (SuperHorizontalScrollView) findViewById(R.id.superHorizontalScrollView1);
		mt = (Button) findViewById(R.id.button1);
		for(int i = 0; i < 20; i++){
			TextView view = new TextView(this);
			LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			view.setText("ENEN" + i);
			view.setPadding(2, 2, 2, 2);
			view.setLayoutParams(params);
			mScrollView.addView(view);
		}
		mt.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if (index > 20 || isF) {
					index--;
					if (index == 0) {
						isF = false;
					} else {
						isF = true;
					}

				} else {
					index++;
				}

				mScrollView.setCurrentPos(index);
			}
		});
	}

}
