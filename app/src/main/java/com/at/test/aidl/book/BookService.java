package com.at.test.aidl.book;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

public class BookService extends Service {

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new BookManagerService();
    }
}
