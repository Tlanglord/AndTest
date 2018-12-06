package com.at.test.app.keyguard;

import android.app.Activity;
import android.app.KeyguardManager;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;

public class KeyguardActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        KeyguardManager keyguardManager = (KeyguardManager) getSystemService(Context.KEYGUARD_SERVICE);
        if (Build.VERSION.SDK_INT > 22) {
            try {
                keyguardManager.createConfirmDeviceCredentialIntent("", "");
            } catch (Exception e) {
                e.printStackTrace();
            }
            KeyguardManager.KeyguardLock keyguardLock = keyguardManager.newKeyguardLock("user");
            keyguardLock.disableKeyguard();
            keyguardLock.reenableKeyguard();
            keyguardManager.inKeyguardRestrictedInputMode();
            keyguardManager.isDeviceLocked();
            keyguardManager.isDeviceSecure();
            if (Build.VERSION.SDK_INT > 25) {
                keyguardManager.requestDismissKeyguard(this, new KeyguardManager.KeyguardDismissCallback() {
                    @Override
                    public void onDismissError() {
                        super.onDismissError();
                    }

                    @Override
                    public void onDismissSucceeded() {
                        super.onDismissSucceeded();
                    }

                    @Override
                    public void onDismissCancelled() {
                        super.onDismissCancelled();
                    }
                });
            }
        }


    }
}
