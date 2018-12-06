package com.at.test.transition;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.view.View;

import com.at.test.R;
import com.at.test.graphics.sample.TextView;

public class TransitionActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transition);

        final View tv = findViewById(R.id.test_transition_go_2);
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TransitionActivity.this, Transition2Activity.class);
                Pair<View, String> stringPair = Pair.create(tv, "su");
                Bundle bundle = ActivityOptionsCompat.makeSceneTransitionAnimation(TransitionActivity.this,stringPair).toBundle();
                startActivity(intent, bundle);
            }
        });

//        Intent intent = new Intent(this, NdkActivity.class);
//        startActivity(intent);

//        ActivityOptionsCompat.makeScaleUpAnimation(, , , , )
//        if (Build.VERSION.SDK_INT > 21) {
////            TransitionManager.go(getContentScene());
//            getWindow().setEnterTransition(new Explode());
//            getWindow().setExitTransition(new Slide());
//        }
    }
}
