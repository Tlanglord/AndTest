package com.at.test.activity.start;

import android.app.Activity;

/**
 * Created by dqq on 2019/4/10.
 */
public class StartFactory {

    public static Start build() {
        return new Start() {
            @Override
            public void orderList(String orderNo) {

            }

            @Override
            public void orderDetail(String orderNo) {

            }
        };
    }


    public static Start buildOf(Activity activity) {
        return new Start() {
            @Override
            public void orderList(String orderNo) {

            }

            @Override
            public void orderDetail(String orderNo) {

            }
        };
    }

}
