package com.at.test.activity.life;

import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.support.annotation.NonNull;

/**
 * Created by dqq on 2019/4/10.
 */
public class User {


    public static class UserLiveData extends LiveData<User> {
        @Override
        public void observe(@NonNull LifecycleOwner owner, @NonNull Observer<User> observer) {
            super.observe(owner, observer);
        }

        public void setUser(User user) {
            setValue(user);
        }

        public User getUser() {
            return getValue();
        }
    }

    public static class BaseLiveData<T extends IResponse.BaseResp<?>> extends LiveData<T> {

        public void setData(T data) {
            setValue(data);
        }

    }

}
