package com.wateron.smartrhomes.util;

/**
 * Created by Paranjay on 14-12-2017.
 */

public interface ValveHandlerInterface {

    void actionSuccess(int state,String response);
    void actionFailed(int state, String response, int code, String url, String xmsin, String token, String meter_id, String action);

}
