package com.at.test.activity.life;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProvider;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by dqq on 2019/4/9.
 */
public class LifecycleActivity extends AppCompatActivity implements ILoading {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        UserViewModel userModel = ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication()).create(UserViewModel.class);
        UserRepository userRepository = new UserRepository();
        userRepository.setLoading(this);
        userModel.setUserRepository(userRepository);

        userModel.userLiveData.observe(this, new Observer<User>() {
            @Override
            public void onChanged(@Nullable User user) {

            }
        });
        userModel.login();

        userModel.baseLiveData.observe(this, new Observer<IResponse.BaseResp<User>>() {
            @Override
            public void onChanged(@Nullable IResponse.BaseResp<User> userBaseResp) {
                if (userBaseResp == null) {
                    return;
                }

                if (userBaseResp.code == NetStatus.SHOW_LOADING) {

                }
            }
        });
        userModel.logout();

        userModel.submit();
    }


    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }
}
