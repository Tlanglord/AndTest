package com.at.test.activity.life;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.support.annotation.NonNull;

/**
 * Created by dqq on 2019/4/10.
 */
public class UserViewModel extends AndroidViewModel {

    public User.UserLiveData userLiveData = new User.UserLiveData();

    private UserRepository userRepository;

    public UserViewModel(@NonNull Application application) {
        super(application);
    }

    public void login() {
        UserRepository.login(new IResponse<User>() {
            @Override
            public void onSuccess(User data) {
                userLiveData.setUser(data);
            }

            @Override
            public void onFail() {

            }
        });
//        netInternal(new IResponse<User>() {
//            @Override
//            public void onSuccess(User data) {
//                userLiveData.setUser(data);
//            }
//
//            @Override
//            public void onFail() {
//
//            }
//        });
    }

    public <T> void netInternal(IResponse<T> iResponse) {

    }

    public User.BaseLiveData<IResponse.BaseResp<User>> baseLiveData = new User.BaseLiveData();

    public UserRepository getUserRepository() {
        return userRepository;
    }

    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void logout() {

        baseLiveData.setData(NetStatus.getShowLoadingStatus());
        UserRepository.loginOut(new IResponse<IResponse.BaseResp<User>>() {
            @Override
            public void onSuccess(BaseResp<User> data) {
                baseLiveData.setData(NetStatus.getHideLoadingStatus());
                data.code = NetStatus.REQUEST_NET_SUCCESS;
                baseLiveData.setData(data);
            }

            @Override
            public void onFail() {

            }
        });
    }

    public void submit() {
        userRepository.submit(new IResponse<User>() {

            @Override
            public void onSuccess(User data) {
                userLiveData.setUser(data);
            }

            @Override
            public void onFail() {

            }
        });
    }
}


