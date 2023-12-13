// ITwoAidlInterface.aidl
package com.anxer.part2;

// Declare any non-default types here with import statements

interface ITwoAidlInterface {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
    String sendNameTo3();
    void callBack(int response);
}