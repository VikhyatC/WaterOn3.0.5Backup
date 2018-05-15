package com.wateron.smartrhomes.util;

import android.content.Context;

/**
 * Created by Paranjay on 14-12-2017.
 */

public interface AlertHandlerInterface {

    public Context getInstance();

    void loadData(boolean latest);

    void errorGettingData(String response, int httpResult, String url, String xmsin, String token);
}
