package com.at.test.activity.start;

/**
 * Created by dqq on 2019/4/10.
 */
public interface Start {

    @RouterPath("order/list")
    void orderList(@BundleParam("orderNum") String orderNo);

    @RouterPath("order/detail")
    void orderDetail(@BundleParam("orderNum") String orderNo);

}
