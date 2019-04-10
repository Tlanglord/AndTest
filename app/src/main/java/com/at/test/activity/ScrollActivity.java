package com.at.test.activity;


import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ScrollView;

import com.at.test.R;

public class ScrollActivity extends Activity {

    private ScrollView sv;
    private View up;
    private View down;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scroll);
        sv = findViewById(R.id.test_scroll);
        down = findViewById(R.id.test_scroll_down);
        up = findViewById(R.id.test_scroll_up);
        up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sv.scrollTo(100, 100);
            }
        });

        down.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sv.scrollTo(-100, -100);
            }
        });
    }
}
