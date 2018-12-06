package com.at.test.hw.finger;

import android.app.Activity;
import android.content.Context;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.support.annotation.Nullable;
import android.util.Log;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

public class FingerprintActivity extends Activity {

    private static final String TAG = "FingerprintActivity";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT > 22) {
            FingerprintManager fingerprintManager = (FingerprintManager) getSystemService(Context.FINGERPRINT_SERVICE);
            FingerprintManager.CryptoObject cryptoObject = null;
            try {
                KeyGenerator keyGenerator = KeyGenerator.getInstance("HmacMD5");
                SecretKey secretKey = keyGenerator.generateKey();
                Cipher cipher = cryptoObject.getCipher();
                cipher.init(Cipher.UNWRAP_MODE, secretKey);
                cryptoObject = new FingerprintManager.CryptoObject(cipher);
            } catch (Exception e) {
                e.printStackTrace();
            }
            CancellationSignal signal = new CancellationSignal();
            FingerprintManager.AuthenticationCallback authenticationCallback = new FingerprintManager.AuthenticationCallback() {
                @Override
                public void onAuthenticationError(int errorCode, CharSequence errString) {
                    super.onAuthenticationError(errorCode, errString);
                    Log.v(TAG, "onAuthenticationError");
                }

                @Override
                public void onAuthenticationHelp(int helpCode, CharSequence helpString) {
                    super.onAuthenticationHelp(helpCode, helpString);
                    Log.v(TAG, "onAuthenticationHelp ：" + "helpCode: " + helpCode + ", helpString: " + helpString);
                }

                @Override
                public void onAuthenticationSucceeded(FingerprintManager.AuthenticationResult result) {
                    super.onAuthenticationSucceeded(result);
                    Log.v(TAG, "onAuthenticationSucceeded: " + result.getCryptoObject());
                }

                @Override
                public void onAuthenticationFailed() {
                    super.onAuthenticationFailed();
                    Log.v(TAG, "onAuthenticationFailed");
                }
            };
            if (fingerprintManager != null) {
                fingerprintManager.authenticate(cryptoObject, signal, FingerprintManager.FINGERPRINT_ACQUIRED_GOOD, authenticationCallback, null);
            }
        }
    }
}
