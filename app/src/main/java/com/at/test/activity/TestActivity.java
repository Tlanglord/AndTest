package com.at.test.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.at.test.R;
import com.at.test.utils.ClassUtils;


public class TestActivity extends Activity implements OnItemClickListener {

    private static final String TAG = TestActivity.class.getSimpleName();

    private ClassUtils[] mClassList;
    private ListView mListView;
    private TestAdapter mTestAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        initView();
        initEvent();
        initData();
    }

    private void initView() {
        mListView = findViewById(R.id.test_listview);
    }

    private void initEvent() {
        mListView.setOnItemClickListener(this);
    }

    private void initData() {
        mClassList = ClassUtils.values();
        mTestAdapter = new TestAdapter(mClassList);
        mListView.setAdapter(mTestAdapter);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(this, mClassList[position].getMC());
        startActivity(intent);
    }

    public class TestAdapter extends BaseAdapter {

        private ClassUtils[] mClassList;

        TestAdapter(ClassUtils[] classList) {
            this.mClassList = classList;
        }

        @Override
        public int getCount() {
            return mClassList.length;
        }

        @Override
        public Object getItem(int position) {
            return mClassList[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null) {
                convertView = LayoutInflater.from(TestActivity.this).inflate(R.layout.item_test_classname, null);
                holder = new ViewHolder();
                holder.className = convertView.findViewById(R.id.test_classname);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.className.setText(mClassList[position].getMC()
                    .getSimpleName());
            return convertView;
        }
    }

    static class ViewHolder {
        public TextView className;
    }

}
