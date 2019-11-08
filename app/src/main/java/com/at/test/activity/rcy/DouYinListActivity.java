package com.at.test.activity.rcy;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.VideoView;

import com.at.test.R;

/**
 * Created by dqq on 2019/11/7.
 */
public class DouYinListActivity extends Activity {

    RecyclerView rcy_test_douyin;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dy_list);
        rcy_test_douyin = findViewById(R.id.rcy_test_douyin);

        rcy_test_douyin.setLayoutManager(new LinearLayoutManager(DouYinListActivity.this));
        rcy_test_douyin.setAdapter(new DouYinAdapter());
        PagerSnapHelper pagerSnapHelper = new PagerSnapHelper();
        pagerSnapHelper.attachToRecyclerView(rcy_test_douyin);
    }


    class DouYinAdapter extends RecyclerView.Adapter<DouYinVH> {

        @NonNull
        @Override
        public DouYinVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            DouYinVH douYinVH = new DouYinVH(LayoutInflater.from(parent.getContext()).inflate(R.layout.view_douyin_list_item, parent, false));

            return douYinVH;
        }

        @Override
        public void onBindViewHolder(@NonNull DouYinVH holder, int position) {
            String ss = String.valueOf(position);
            holder.tv.setText(ss);
            holder.vv.setVideoPath("");
        }

        @Override
        public int getItemCount() {
            return 10;
        }
    }

    class DouYinVH extends RecyclerView.ViewHolder {

        public TextView tv;
        public VideoView vv;

        public DouYinVH(View itemView) {
            super(itemView);
            tv = itemView.findViewById(R.id.test_rcy_tv);
            vv = itemView.findViewById(R.id.test_rcy_vv);
        }
    }

}
