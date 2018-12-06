package com.at.test.app.usage;

import android.app.Activity;
import android.app.usage.ExternalStorageStats;
import android.app.usage.NetworkStats;
import android.app.usage.NetworkStatsManager;
import android.app.usage.StorageStatsManager;
import android.app.usage.UsageEvents;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Process;
import android.os.RemoteException;
import android.os.storage.StorageManager;
import android.support.annotation.Nullable;

import java.io.IOException;
import java.util.Map;

public class UsagesActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT > 22) {
            NetworkStatsManager networkStatsManager = (NetworkStatsManager) getSystemService(Context.NETWORK_STATS_SERVICE);
            try {
                NetworkStats networkStats = networkStatsManager.queryDetails(ConnectivityManager.TYPE_WIFI, "",
                        System.currentTimeMillis() - 1000000, System.currentTimeMillis());
                while (networkStats.hasNextBucket()) {
                    NetworkStats.Bucket bucket = new NetworkStats.Bucket();
                    networkStats.getNextBucket(bucket);
                }
            } catch (RemoteException e) {
                e.printStackTrace();
            }

            if (Build.VERSION.SDK_INT > 25) {
                StorageStatsManager storageStatsManager = (StorageStatsManager) getSystemService(Context.STORAGE_STATS_SERVICE);
                try {
                    ExternalStorageStats externalStorageStats = storageStatsManager.queryExternalStatsForUser(StorageManager.UUID_DEFAULT, Process.myUserHandle());
                } catch (IOException e) {

                }
            }

            UsageStatsManager usageStatsManager = (UsageStatsManager) getSystemService(Context.USAGE_STATS_SERVICE);
            Map<String, UsageStats> usageStatsMap = usageStatsManager.queryAndAggregateUsageStats(System.currentTimeMillis() - 1000000, System.currentTimeMillis());

            UsageEvents usageEvents = usageStatsManager.queryEvents(System.currentTimeMillis() - 100000, System.currentTimeMillis());

//            usageStatsManager.


        }

    }
}
