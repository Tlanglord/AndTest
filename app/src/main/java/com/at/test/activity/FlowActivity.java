package com.at.test.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;

import com.at.test.R;
import com.at.test.views.FlowLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class FlowActivity extends Activity implements OnItemClickListener {

    private static final String TAG = FlowActivity.class.getSimpleName();

    private FlowLayout mFlowLayout;
    private List<String> list = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flow);
        initView();
        initEvent();
        initData();
    }

    private void initView() {
        mFlowLayout = (FlowLayout) findViewById(R.id.flow_layout);
    }

    private void initEvent() {
    }

    private void initData() {
        for (int i = 0; i < 20; i++) {
            StringBuffer buffer = new StringBuffer();
            for (int j = 0; j < new Random().nextInt(20); j++) {
                buffer.append("jj");
            }
            list.add(buffer.toString());
        }

        for (int i = 0; i < list.size(); i++) {
            TextView view = new TextView(this);

            MarginLayoutParams layoutParams = new MarginLayoutParams(0, 0);

            view.setText(list.get(i));
            mFlowLayout.addView(view);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {


    }
}
