package com.at.test.activity;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AccelerateInterpolator;
import android.widget.TextView;

import com.at.test.R;
import com.at.test.utils.AnimatorUtils;


public class PageFlingActivity extends Activity {

	private TextView mPageFlingTest;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pagefling);

		mPageFlingTest = (TextView) findViewById(R.id.pagefling_test);

		mPageFlingTest.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				// Animator animation = AnimationUtils.loadAnimation(
				// PageFlingActivity.this, R.animator.page_fling1);
				// mPageFlingTest.startAnimation(animation);
				//
				// Animator animator = ObjectAnimator.ofFloat(mPageFlingTest,
				// "rotationY", 0f, -90f);
				// Animator animator1 = ObjectAnimator.ofFloat(mPageFlingTest,
				// "scaleY", mPageFlingTest.getHeight(), 0f);
				//
				// animator.setInterpolator(new AccelerateInterpolator());
				// animator.setDuration(2000);
				// animator.start();

				// AnimatorSet set = new AnimatorSet();
				// set.play(animator).with(animator1);
				// set.setDuration(2000);
				// set.start();

				mPageFlingTest.setRotation(0);
				mPageFlingTest.setRotationY(-90);
				mPageFlingTest.setRotationX(0);

				mPageFlingTest.setPivotX(0);
				mPageFlingTest.setPivotY(mPageFlingTest.getHeight() / 2);

				ObjectAnimator closeToRight = AnimatorUtils
						.rotationCloseToRight(mPageFlingTest);

				AnimatorSet fadeOutSet = AnimatorUtils.fadeOutSet(
						mPageFlingTest, mPageFlingTest.getHeight());

				AnimatorSet imageFullAnimatorSet = new AnimatorSet();
				imageFullAnimatorSet.play(closeToRight);

				AnimatorSet fullAnimatorSet = new AnimatorSet();
				fullAnimatorSet.playTogether(fadeOutSet, imageFullAnimatorSet);
				fullAnimatorSet.setDuration(2000);
				fullAnimatorSet.setInterpolator(new AccelerateInterpolator());
				fullAnimatorSet.start();
			}
		});
	}
}
