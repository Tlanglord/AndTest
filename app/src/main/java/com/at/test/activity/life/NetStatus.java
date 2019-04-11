package com.at.test.activity.life;

/**
 * Created by dqq on 2019/4/11.
 */
public class NetStatus {

    public static final int SHOW_LOADING = -1;
    public static final int HIDE_LOADING = -2;
    public static final int REQUEST_NET_SUCCESS = 0;

    public static IResponse.BaseResp getShowLoadingStatus() {
        IResponse.BaseResp baseResp = new IResponse.BaseResp();
        baseResp.code = SHOW_LOADING;
        return baseResp;
    }

    public static IResponse.BaseResp getHideLoadingStatus() {
        IResponse.BaseResp baseResp = new IResponse.BaseResp();
        baseResp.code = HIDE_LOADING;
        return baseResp;
    }

    public static IResponse.BaseResp getRequestSuccessStatus() {
        IResponse.BaseResp baseResp = new IResponse.BaseResp();
        baseResp.code = REQUEST_NET_SUCCESS;
        return baseResp;
    }
}
