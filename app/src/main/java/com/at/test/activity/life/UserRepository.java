package com.at.test.activity.life;

/**
 * Created by dqq on 2019/4/11.
 */
public class UserRepository {

    public ILoading iLoading;

    public void setLoading(ILoading loading) {
        this.iLoading = loading;
    }

    public static <T> void login(IResponse<T> iResponse) {

    }

    public static <T> void loginOut(IResponse<T> iResponse) {

    }

    public <T> void submit(final IResponse<T> iResponse) {

        if (iLoading != null) {
            iLoading.showLoading();
        }

        Executor.execute(new HttpExecuteCallback<T>() {
            @Override
            void onResponse(T data) {
                if (iLoading != null) {
                    iLoading.hideLoading();
                }
                if (iResponse != null) {
                    iResponse.onSuccess(data);
                }
            }

            @Override
            void onFailure() {
                if (iLoading != null) {
                    iLoading.hideLoading();
                }
            }
        });
    }

    public static abstract class HttpExecuteCallback<T> {

        abstract void onResponse(T data);

        abstract void onFailure();

    }

    public static class Executor {

        public static void execute(HttpExecuteCallback httpExecuteCallback) {

        }
    }


}
