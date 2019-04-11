package com.at.test.activity.life;

/**
 * Created by dqq on 2019/4/11.
 */
public interface IResponse<T> {

    void onSuccess(T data);

    void onFail();

    public static class BaseResp<T> {
        T data;
        int code =-1;
        int netCode;
        String msg;

        public T getData() {
            return data;
        }

        public void setData(T data) {
            this.data = data;
        }

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public int getNetCode() {
            return netCode;
        }

        public void setNetCode(int netCode) {
            this.netCode = netCode;
        }
    }


}
