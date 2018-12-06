package com.at.test.transition;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.transition.ArcMotion;
import android.transition.AutoTransition;
import android.transition.ChangeBounds;
import android.transition.Scene;
import android.transition.Transition;
import android.transition.TransitionManager;
import android.view.View;
import android.view.Window;
import android.widget.FrameLayout;

import com.at.test.R;

public class Transition2Activity extends Activity {

    private View tr2;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        setContentView(R.layout.activity_transition2);

//        tr2 = findViewById(R.id.test_transition2_tr2);
        final FrameLayout fl = findViewById(R.id.test_transition2_fl);

        findViewById(R.id.test_transition2_tr1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT > 22) {
//                    tr2.setVisibility(View.VISIBLE);
                    Scene scene = Scene.getSceneForLayout(fl, R.layout.include_transition2, Transition2Activity.this);
                    TransitionManager.go(scene, new AutoTransition());
                }
            }
        });

//        Intent intent = new Intent(this, NdkActivity.class);
//        startActivity(intent);

//        ActivityOptionsCompat.makeScaleUpAnimation(, , , , )
        if (Build.VERSION.SDK_INT > 22) {
//            TransitionManager.go(getContentScene());
            Transition transition = new ChangeBounds();
//            transition.setDuration(1);
            ArcMotion motion = new ArcMotion();
            motion.setMaximumAngle(90);
            motion.setMinimumHorizontalAngle(45);
            motion.setMinimumVerticalAngle(45);
            transition.setPathMotion(motion);
//            TransitionValues transitionValues = new TransitionValues();
//            transitionValues.view = getWindow().getDecorView();
//            transition.captureStartValues(transitionValues);


            getWindow().setSharedElementEnterTransition(transition);
            getWindow().setEnterTransition(transition);
//            getWindow().setExitTransition(new Slide());
        }
    }
}
