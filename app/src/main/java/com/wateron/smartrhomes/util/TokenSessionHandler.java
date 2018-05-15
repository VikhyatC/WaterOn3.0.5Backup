package com.wateron.smartrhomes.util;

import org.json.JSONArray;

/**
 * Created by vikhyat on 11/4/18.
 */

public interface TokenSessionHandler {

    public void autoRefresher(JSONArray consumptiontoday);
    public void storeToken(String auth);
}
