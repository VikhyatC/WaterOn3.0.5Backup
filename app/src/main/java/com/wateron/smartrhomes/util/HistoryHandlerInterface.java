package com.wateron.smartrhomes.util;

import android.content.Context;

/**
 * Created by Paranjay on 12-12-2017.
 */

public interface HistoryHandlerInterface {

    public Context getInstance();

    void loadData(boolean latest);

    void errorGettingData(String response, int httpResult, String url, String xmsin, String token);

    void RecheckData();
}
