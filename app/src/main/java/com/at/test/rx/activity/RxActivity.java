package com.at.test.rx.activity;

import android.app.Activity;
import android.os.Bundle;

public class RxActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		Observable.just(1, 2, 3, 4).subscribeOn(Schedulers.io())
//				.observeOn(AndroidSchedulers.mainThread())
//				.subscribe(new Action1<Integer>() {
//
//					@Override
//					public void call(Integer num) {
//						Toast.makeText(RxActivity.this, String.valueOf(num),
//								Toast.LENGTH_SHORT).show();
//					}
//				});

	}
}
