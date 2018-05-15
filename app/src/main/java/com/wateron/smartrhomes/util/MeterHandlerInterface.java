package com.wateron.smartrhomes.util;

/**
 * Created by Paranjay on 14-12-2017.
 */

public interface MeterHandlerInterface {

    void nameChanged();
    void errorChangingName(String response, int httpResult, String url1, String s, String url, String xmsin, String token);
}
