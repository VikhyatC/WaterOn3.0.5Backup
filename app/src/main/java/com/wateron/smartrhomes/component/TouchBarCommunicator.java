package com.wateron.smartrhomes.component;

/**
 * Created by paran on 11/23/2016.
 */

public interface TouchBarCommunicator {

    void barTouched(int style, int slot);
    void barEntered(int style, int slot);
    void barExcitedd(int style, int slot);
}
