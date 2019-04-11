package com.at.test.activity.life;

import com.at.test.TestApplication;

/**
 * Created by dqq on 2019/4/11.
 */
public class UserLifecycle implements Life {

    @Override
    public void onCreate() {
        net();
    }

    @Override
    public void onResume() {

    }

    @Override
    public void onPause() {

    }

    @Override
    public void onDestroy() {

    }


    public void net() {
        netInternal(new IResponse<User>() {
            @Override
            public void onSuccess(User data) {
                UserViewModel userModel = new UserViewModel(TestApplication.getGlobalApplication());
                userModel.userLiveData.setUser(data);
            }

            @Override
            public void onFail() {

            }
        });
    }


    public <T> void netInternal(IResponse<T> iResponse) {

    }
}
