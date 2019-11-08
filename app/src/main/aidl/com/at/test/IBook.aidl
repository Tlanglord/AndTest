// IBook.aidl
package com.at.test;

import com.at.test.OnBookListener;

// Declare any non-default types here with import statements

interface IBook {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
    void basicTypes(int anInt,long aLong, boolean aBoolean, float aFloat,
            double aDouble, String aString);

    void register(OnBookListener listener);

    void unregister(OnBookListener listener);

    int testResult(int p);
}
