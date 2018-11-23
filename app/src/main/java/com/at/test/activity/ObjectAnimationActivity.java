package com.at.test.activity;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.animation.AccelerateInterpolator;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.at.test.R;


public class ObjectAnimationActivity extends Activity implements
		OnClickListener {

	private TextView mtestTextView;
	private TextView mBtn;
	ProgressBar mProgressBar;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_object);

//		 Animation animation = AnimationUtils.loadAnimation(this,
//		 R.anim.test);
//
//		mtestTextView = (TextView) findViewById(R.id.test_iiii);
//		mBtn = (TextView) findViewById(R.id.test_btn);
//		 mtestTextView.startAnimation(animation);
//		  animation.startNow();
//
//
//		 // mProgressBar.setVisibility(View.VISIBLE);
//		  findViewById(R.id.progressBar1).setVisibility(View.GONE);
//
//		//  findViewById(R.id.progressBar1).setVisibility(View.VISIBLE);
//		// mtestTextView.setAnimation(animation);
//
//		mBtn.setOnClickListener(this);
//		 test();
	}

	public void onRun(View v) {
		// ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(0f, 10f);
		// objectAnimator.set
		// ObjectAnimator.ofFloat(v, "alpha", 0.0f, 1.0f).setDuration(500)
		// .start();
		 
	}

	private void test() {
		// ViewTreeObserver vto = mtestTextView.getViewTreeObserver();
		//
		// vto.addOnPreDrawListener(new
		//
		// ViewTreeObserver.OnPreDrawListener() {
		//
		// @Override
		// public boolean onPreDraw() {
		//
		// int height = mtestTextView.getMeasuredHeight();
		//
		// int width = mtestTextView.getMeasuredWidth();
		//
		// Log.v("TAGW", width + "");
		// ObjectAnimator.ofInt(mtestTextView, "width", 0, width)
		// .setDuration(500).start();
		// return true;
		// }
		//
		// });

		

//		AnimatorSet animationSet = new AnimatorSet();
//		animationSet.play(anim2).after(anim1);
//		animationSet.start();

		 ViewTreeObserver vto = mtestTextView.getViewTreeObserver();
		
		 vto.addOnGlobalLayoutListener(new
		
		 OnGlobalLayoutListener() {
		
		 @Override
		 public void onGlobalLayout() {
		
		 mtestTextView.getViewTreeObserver()
		 .removeGlobalOnLayoutListener(this);
		 ViewWrapper viewWrapper = new ViewWrapper(mtestTextView);
			// viewWrapper.setWidth(0);

			int width = mtestTextView.getMeasuredWidth();
			int height = mtestTextView.getMeasuredHeight();
			
			mtestTextView.setPivotX(0);
			mtestTextView.setPivotY(0);
			
			ObjectAnimator anim1 = ObjectAnimator.ofInt(viewWrapper, "width",
					width, 0).setDuration(0);

			viewWrapper.setWidth(0);
			ObjectAnimator anim2 = ObjectAnimator.ofInt(viewWrapper, "width", 1,
					width).setDuration(700);
			anim2.setInterpolator(new AccelerateInterpolator());
			anim2.start();
		 }
		 });
		// // int width = mtestTextView.getWidth();
		//
		// // mtestTextView.getg
		// // ObjectAnimator.ofInt(mtestTextView, "width", 0,
		// // width).setDuration(500)
		// // .start();
	}

	public class ViewWrapper {
		private View view;

		public ViewWrapper(View view) {
			super();
			this.view = view;
		}

		public void setWidth(int width) {
			view.getLayoutParams().width = width;
			view.requestLayout();
		}

		public int getWidth() {
			return view.getLayoutParams().width;
		}
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.test_btn) {
			test();
		}
	}
}
