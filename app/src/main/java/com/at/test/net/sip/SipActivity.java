package com.at.test.net.sip;

import android.app.Activity;
import android.net.sip.SipManager;
import android.net.sip.SipProfile;
import android.net.sip.SipSession;
import android.os.Bundle;
import android.support.annotation.Nullable;

public class SipActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SipManager sipManager = SipManager.newInstance(this);
        try {
            SipProfile sipProfile = new SipProfile.Builder("").build();
            SipSession sipSession = sipManager.createSipSession(sipProfile, new SipSession.Listener() {

                @Override
                public void onCalling(SipSession session) {
                    super.onCalling(session);
                }

                @Override
                public void onRinging(SipSession session, SipProfile caller, String sessionDescription) {
                    super.onRinging(session, caller, sessionDescription);
                }

                @Override
                public void onRingingBack(SipSession session) {
                    super.onRingingBack(session);
                }

                @Override
                public void onCallEstablished(SipSession session, String sessionDescription) {
                    super.onCallEstablished(session, sessionDescription);
                }

                @Override
                public void onCallEnded(SipSession session) {
                    super.onCallEnded(session);
                }

                @Override
                public void onCallBusy(SipSession session) {
                    super.onCallBusy(session);
                }

                @Override
                public void onError(SipSession session, int errorCode, String errorMessage) {
                    super.onError(session, errorCode, errorMessage);
                }

                @Override
                public void onCallChangeFailed(SipSession session, int errorCode, String errorMessage) {
                    super.onCallChangeFailed(session, errorCode, errorMessage);
                }

                @Override
                public void onRegistering(SipSession session) {
                    super.onRegistering(session);
                }

                @Override
                public void onRegistrationDone(SipSession session, int duration) {
                    super.onRegistrationDone(session, duration);
                }

                @Override
                public void onRegistrationFailed(SipSession session, int errorCode, String errorMessage) {
                    super.onRegistrationFailed(session, errorCode, errorMessage);
                }

                @Override
                public void onRegistrationTimeout(SipSession session) {
                    super.onRegistrationTimeout(session);
                }
            });
        } catch (Exception e) {

        }

    }
}
