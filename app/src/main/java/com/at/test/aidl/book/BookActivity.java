package com.at.test.aidl.book;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;

import com.at.test.IBook;
import com.at.test.OnBookListener;

public class BookActivity extends Activity {

    private static final String TAG = "BookActivity";

    private IBook book;

    private ServiceConnection serviceConnection;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = new Intent(this, BookService.class);
//        startService(intent);
//        startService(intent);
//
        startService(intent);
        bindService(intent, serviceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                book = IBook.Stub.asInterface(service);
                try {
                    book.register(onBookListener);
                    service.linkToDeath(new IBinder.DeathRecipient() {
                        @Override
                        public void binderDied() {
                            Log.v(TAG, "binderDied");
                        }
                    }, 0);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                Log.v(TAG, "onServiceDisconnected");
            }
        }, Context.BIND_AUTO_CREATE);
    }

    OnBookListener onBookListener = new OnBookListener.Stub() {
        @Override
        public void leave() throws RemoteException {
            Log.v(TAG, "onBookListener");
        }
    };


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (book != null) {
            try {
//                Intent intent = new Intent(this, BookService.class);
//                stopService(intent);
                book.unregister(onBookListener);
                unbindService(serviceConnection);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
}
