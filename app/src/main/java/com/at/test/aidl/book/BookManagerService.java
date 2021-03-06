package com.at.test.aidl.book;

import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.util.Log;

import com.at.test.IBook;
import com.at.test.OnBookListener;

import java.util.Timer;
import java.util.TimerTask;

public class BookManagerService extends IBook.Stub {

    private static final String TAG = "BookManagerService";
    private Timer timer;

    private RemoteCallbackList<OnBookListener> callbackList;

    public BookManagerService() {
        super();
        Log.v(TAG, "BookManagerService");
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                try {
                    if (callbackList != null && callbackList.beginBroadcast() > 0) {
                        OnBookListener onBookListener = callbackList.getBroadcastItem(0);
                        if (onBookListener != null) {
                            onBookListener.leave();

                        }
                        callbackList.finishBroadcast();
                    }
                    Log.v(TAG, "onBookListener");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, 5000, 1000);
    }

    @Override
    public void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat, double aDouble, String aString) throws RemoteException {

    }

    @Override
    public void register(OnBookListener listener) throws RemoteException {
        if (callbackList == null) {
            callbackList = new RemoteCallbackList<>();
        }
        callbackList.register(listener);
    }

    @Override
    public void unregister(OnBookListener listener) throws RemoteException {
        if (callbackList != null) {
            callbackList.unregister(listener);
        }
    }

    @Override
    public int testResult(int p) throws RemoteException {
        return 0;
    }

    //    public void register(OnBookListener onBookListener) {
//        if (callbackList == null) {
//            callbackList = new RemoteCallbackList<>();
//        }
//        callbackList.register(onBookListener);
//    }

}
