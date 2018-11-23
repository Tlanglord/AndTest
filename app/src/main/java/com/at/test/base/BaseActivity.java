

package com.at.test.base;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.widget.Toast;

import com.at.test.sliding.ActivityBackHelper;
import com.at.test.sliding.IBackListener;

public abstract class BaseActivity extends AppCompatActivity implements IBackListener {

    private static final String TAG = "BaseActivity";

    protected abstract void onCreateImpl(Bundle savedInstanceState);

    protected abstract void onDestroyImpl();

    private Toast mToast;

    private ActivityBackHelper mBackHelper;
    private ViewPager mPager;

    @Override
    final protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        onCreateImpl(savedInstanceState);

        if (onSlidingBack() || onFlingBack()) {
            mBackHelper = new ActivityBackHelper();
            mBackHelper.init(this);
            if (mPager != null) {
                switchSlidingWithViewPager(mPager.getCurrentItem());
            }
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    final protected void onDestroy() {
        super.onDestroy();

        onDestroyImpl();

    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();

        backOfFinish();
    }

    /**
     * add方式添加fragment
     *
     * @param f
     * @param containerId
     */
    public void addFragment(Fragment f, int containerId) {

        addFragment(f, containerId, null);

    }

    /**
     * add方式添加fragment，入栈
     *
     * @param f
     * @param containerId
     */
    public void addFragment(Fragment f, int containerId, String stackName) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.add(containerId, f);
        if (TextUtils.isEmpty(stackName)) {
            ft.addToBackStack(stackName);
        }
        ft.commitAllowingStateLoss();
    }

    /**
     * replace方式添加fragment
     *
     * @param f
     * @param containerId
     */
    public void replaceFragment(Fragment f, int containerId) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(containerId, f);
        ft.commitAllowingStateLoss();
    }

    /**
     * replace方式添加fragment，入栈
     *
     * @param f
     * @param containerId
     */
    public void replaceFragment(Fragment f, int containerId, String stackName) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(containerId, f, stackName);
        ft.addToBackStack(stackName);
        ft.commitAllowingStateLoss();
    }

    /**
     * 退栈，逐个退栈
     *
     * @return
     */
    public boolean backForFragment() {
        FragmentManager fm = getSupportFragmentManager();
        int stackCount = fm.getBackStackEntryCount();
        if (stackCount > 1) {
            fm.popBackStack();
            return true;
        }
        return false;
    }

    /**
     * Inclusive类型退栈
     *
     * @param stackName
     * @return
     */
    public boolean backOfInclusive(String stackName) {
        FragmentManager fm = getSupportFragmentManager();
        if (fm != null) {
            int stackCount = fm.getBackStackEntryCount();
            if (stackCount > 1) {
                fm.popBackStack(stackName,
                        FragmentManager.POP_BACK_STACK_INCLUSIVE);
                return true;
            }
        }
        return false;
    }

    /**
     * 退栈，没有则finish activity
     */
    public void backOfFinish() {
        FragmentManager fm = getSupportFragmentManager();
        int stackCount = fm.getBackStackEntryCount();
        if (stackCount > 1) {
            fm.popBackStack();
        } else {
            doFinish();
//            overridePendingTransition(0, android.R.anim.slide_out_right);
        }
    }

    public void doFinish() {
        finish();
    }

    protected void showToast(int resid) {
        showToast(getString(resid));
    }

    protected void showToast(String toast) {
        if (mToast == null) {
            mToast = Toast.makeText(this, toast, Toast.LENGTH_LONG);
        } else {
            mToast.cancel();
        }
        mToast.setText(toast);
        mToast.setDuration(Toast.LENGTH_LONG);
        mToast.show();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        //LogM.v(TAG, "case     dispatchTouchEvent");
        boolean boolBack = false;
        if (mBackHelper != null && onFlingBack()) {
            boolBack = mBackHelper.processDispatchEvent(ev);
        }

        if (boolBack) {
            return boolBack;
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onSlidingBack() {
        return true;
    }

    @Override
    public boolean onFlingBack() {
        return false;
    }

    private void switchSlidingWithViewPager(int position) {
        if (mBackHelper != null) {
            mBackHelper.setEnableSliding(position == 0);
        }
    }

    /**
     * 侧滑退出兼容ViewPager
     *
     * @param pager
     */
    protected void setViewPagerWithSliding(ViewPager pager) {
        mPager = pager;
        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switchSlidingWithViewPager(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

}
