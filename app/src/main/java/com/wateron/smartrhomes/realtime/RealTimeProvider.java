package com.wateron.smartrhomes.realtime;

import java.util.List;
import java.util.Map;

/**
 * Created by Paranjay on 27-12-2017.
 */

public interface RealTimeProvider {
    void setUserValue(Map<String, Object> data);

    void apartmentUpdateEvent(Map<String, Object> map);

    void apartmentsDetail(Map<String, Object> map);

    void metersLoaded(List<Map<String,Object>> meters);

    void dailyDataForMeterLoaded(int id, Map<String, Object> map);

    void twoHourDataTodayUpdate(int id, Map<String, Object> map);

    void twoHourDataYesterdayUpdate(int id, Map<String, Object> map);
}
