package com.wateron.smartrhomes.util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.wateron.smartrhomes.models.Alert;
import com.wateron.smartrhomes.models.Apartment;
import com.wateron.smartrhomes.models.DailyData;
import com.wateron.smartrhomes.models.DashDataDates;
import com.wateron.smartrhomes.models.DashboardData;
import com.wateron.smartrhomes.models.HAlert;
import com.wateron.smartrhomes.models.Meter;
import com.wateron.smartrhomes.models.Slabs;
import com.wateron.smartrhomes.models.TwoHourForMeter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Created by Paranjay on 03-12-2017.
 */

public class DataHelper extends SQLiteOpenHelper{

    private static final int DATABASE_VERSION=1;

    private static final String  DATABASE_NAME="waterondb";
    static int id= 0;

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_APARTMENT_DETAILS_TABLE);
        db.execSQL(CREATE_METER_DETAILS_TABLE);
        db.execSQL(CREATE_ALARM_DETAILS_TABLE);
        db.execSQL(CREATE_TWO_HOURLY_CONSUMPTION_DATA_TABLE);
        db.execSQL(CREATE_DAILY_DATA_CONSUMPTION_DATA_TABLE);
        db.execSQL(CREATE_SLAB_DETAILS_TABLE);
        db.execSQL(H_CREATE_DAILY_DATA_CONSUMPTION_DATA_TABLE);
        db.execSQL(H_CREATE_ALERT_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + APARTMENT_DETAILS);
        db.execSQL("DROP TABLE IF EXISTS " + METER_DETAILS);
        db.execSQL("DROP TABLE IF EXISTS " + ALARMS_DETAILS);
        db.execSQL("DROP TABLE IF EXISTS " + TWO_HOURLY_CONSUMPTION_DATA_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + DAILY_DATA_CONSUMPTION_DATA_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + SLAB_DETAILS_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + H_DAILY_DATA_CONSUMPTION_DATA_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + H_ALERT_TABLE);
        onCreate(db);
    }



    public DataHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    private static final String APARTMENT_DETAILS="apartment_details";
    private static final String APARTMENT_ID="aptid";
    private static final String APARTMENT_NAME="name";
    private static final String APARTMENT_SOCIETY="society";
    private static final String APARTMENT_FIXED_CHARGE = "fixed_charge";
    private static final String APARTMENT_BILL_AMOUNT="bill_amount";
    private static final String APARTMENT_BILL_DATE="bill_date";
    private static final String APARTMENT_BILL_STATUS="bill_status";
    private static final String APARTMENT_UNIT_TEXT="unitText";
    private static final String APARTMENT_UNIT_ABB="unitAbbrev";
    private static final String APARTMENT_CURR_TEXT="currencyText";
    private static final String APARTMENT_CURR_ABB="currencyAbbrev";
    private static final String APARTMENT_UNIT_SYM="unitSymbol";
    private static final String APARTMENT_CURR_SYM="currencySymbol";

    private static final String CREATE_APARTMENT_DETAILS_TABLE="CREATE TABLE "+APARTMENT_DETAILS+" (" +
            APARTMENT_ID + " INTEGER PRIMARY KEY, "+
            APARTMENT_NAME + " TEXT, " +
            APARTMENT_SOCIETY + " TEXT, " +
            APARTMENT_FIXED_CHARGE + " REAL, " +
            APARTMENT_BILL_AMOUNT + " REAL, " +
            APARTMENT_BILL_DATE + " TEXT, " +
            APARTMENT_UNIT_TEXT + " TEXT, " +
            APARTMENT_UNIT_ABB + " TEXT, " +
            APARTMENT_CURR_TEXT + " TEXT, " +
            APARTMENT_CURR_ABB + " TEXT, " +
            APARTMENT_UNIT_SYM + " TEXT, " +
            APARTMENT_CURR_SYM + " TEXT, " +
            APARTMENT_BILL_STATUS + " TEXT );";


    private static final String METER_DETAILS="meter_details";
    private static final String METER_ID="meter_id";
    private static final String METER_APARTMENT_INDEX="apartment_index";
    private static final String METER_LOCATION="location";
    private static final String METER_LOCATION_DEFAULT="def_location";
    private static final String METER_HAS_VALVE="has_valve";
    private static final String METER_VALVE_CURRENT_STATUS="current_valve_status";
    private static final String METER_VALVE_LAST_OPERATED="last_operated";
    private static final String METER_APTID="aptid_meter";

    private static final String CREATE_METER_DETAILS_TABLE="CREATE TABLE "+METER_DETAILS+" (" +
            METER_ID + " INTEGER PRIMARY KEY, "+
            METER_LOCATION + " TEXT, " +
            METER_LOCATION_DEFAULT + " TEXT, " +
            METER_APARTMENT_INDEX + " TEXT, " +
            METER_HAS_VALVE + " INTEGER, " +
            METER_VALVE_CURRENT_STATUS + " TEXT, " +
            METER_VALVE_LAST_OPERATED + " TEXT, " +
            METER_APTID + " INTEGER );";

    private static final String ALARMS_DETAILS="alarms_details";
    private static final String ALARMS_QUANTITY="quantity";
    private static final String ALARMS_TIME="time";
    private static final String ALARMS_METERID="alarm_meterid";
    private static final String ALARM_APTID="alarm_apt";
    private static final String ALARMID="al_id";
    private static final String CREATE_ALARM_DETAILS_TABLE="CREATE TABLE "+ALARMS_DETAILS+" (" +
            ALARMID + " TEXT PRIMARY KEY, "+
            ALARMS_METERID + " INTEGER, " +
            ALARMS_TIME + " TEXT, " +
            ALARM_APTID + " INTEGER, " +
            ALARMS_QUANTITY + " TEXT );";

    private static final String TWO_HOURLY_CONSUMPTION_DATA_TABLE="two_hourly_data";
    private static final String TWO_HOURLY_CONSUMPTION_DATA_SLOT="slot";
    private static final String TWO_HOURLY_CONSUMPTION_DATA_ID="id";
    private static final String TWO_HOURLY_CONSUMPTION_DATA_DATE="date";
    private static final String TWO_HOURLY_CONSUMPTION_DATA_VALUE="value";
    private static final String TWO_HOURLY_CONSUMPTION_DATA_METER_ID="meter_id";
    private static final String TWO_HOURLY_CONSUMPTION_DATA_APT_ID="apt_id";

    private static final String CREATE_TWO_HOURLY_CONSUMPTION_DATA_TABLE="CREATE TABLE "+TWO_HOURLY_CONSUMPTION_DATA_TABLE+" (" +
            TWO_HOURLY_CONSUMPTION_DATA_ID + " TEXT PRIMARY KEY, "+
            TWO_HOURLY_CONSUMPTION_DATA_SLOT + " INTEGER , " +
            TWO_HOURLY_CONSUMPTION_DATA_DATE + " TEXT, " +
            TWO_HOURLY_CONSUMPTION_DATA_APT_ID + " INTEGER, " +
            TWO_HOURLY_CONSUMPTION_DATA_METER_ID +" INTEGER, " +
            TWO_HOURLY_CONSUMPTION_DATA_VALUE + " REAL );";

    private static final String DAILY_DATA_CONSUMPTION_DATA_TABLE="daily_data_table";
    private static final String DAILY_DATA_CONSUMPTION_DATA_ID="id";
    private static final String DAILY_DATA_CONSUMPTION_DATA_METER_ID="meter_id";
    private static final String DAILY_DATA_CONSUMPTION_DATA_APT_ID="apt_id";
    private static final String DAILY_DATA_CONSUMPTION_DATA_DATE="date";
    private static final String DAILY_DATA_CONSUMPTION_DATA_VALUE="value";

    private static final String CREATE_DAILY_DATA_CONSUMPTION_DATA_TABLE="CREATE TABLE "+DAILY_DATA_CONSUMPTION_DATA_TABLE+" (" +
            DAILY_DATA_CONSUMPTION_DATA_ID + " TEXT PRIMARY KEY, "+
            DAILY_DATA_CONSUMPTION_DATA_METER_ID + " INTEGER, " +
            DAILY_DATA_CONSUMPTION_DATA_APT_ID + " INTEGER, " +
            DAILY_DATA_CONSUMPTION_DATA_DATE + " TEXT, " +
            DAILY_DATA_CONSUMPTION_DATA_VALUE + " REAL );";

    private static final String H_DAILY_DATA_CONSUMPTION_DATA_TABLE="h_daily_data_table";
    private static final String H_DAILY_DATA_CONSUMPTION_DATA_ID="id";
    private static final String H_DAILY_DATA_CONSUMPTION_DATA_APT_ID="apt_id";
    private static final String H_DAILY_DATA_CONSUMPTION_DATA_DATE="date";
    private static final String H_DAILY_DATA_CONSUMPTION_DATA_VALUE="value";
    private static final String H_CREATE_DAILY_DATA_CONSUMPTION_DATA_TABLE="CREATE TABLE "+H_DAILY_DATA_CONSUMPTION_DATA_TABLE+" (" +
            H_DAILY_DATA_CONSUMPTION_DATA_ID + " TEXT PRIMARY KEY, "+
            H_DAILY_DATA_CONSUMPTION_DATA_APT_ID + " INTEGER, " +
            H_DAILY_DATA_CONSUMPTION_DATA_DATE + " TEXT, " +
            H_DAILY_DATA_CONSUMPTION_DATA_VALUE + " REAL );";

    private static final String H_ALERT_TABLE = "h_alert_table";
    private static final String H_ALERT_DATE = "h_alert_date";
    private static final String H_ALERT_VALUE = "h_alert_value";
    private static final String H_ALERT_APTID = "h_apt_id";
    private static final String H_CREATE_ALERT_TABLE = "CREATE TABLE "+H_ALERT_TABLE+" (" +
            H_ALERT_DATE + " TEXT PRIMARY KEY, "+
            H_ALERT_APTID + " INTEGER, "+
            H_ALERT_VALUE + " INTEGER );";

    private static final String SLAB_DETAILS_TABLE="slab_details";
    private static final String SLAB_DETAILS_ID="id";
    private static final String SLAB_DETAILS_LEVEL="lvl";
    private static final String SLAB_DETAILS_APTID="aptid";
    private static final String SLAB_DETAILS_SLAB="slab";
    private static final String SLAB_DETAILS_SLABRATE="rate";

    private static final String CREATE_SLAB_DETAILS_TABLE="CREATE TABLE "+SLAB_DETAILS_TABLE+" (" +
            SLAB_DETAILS_ID + " TEXT PRIMARY KEY, "+
            SLAB_DETAILS_APTID + " INTEGER, " +
            SLAB_DETAILS_LEVEL + " INTEGER, " +
            SLAB_DETAILS_SLAB + " REAL, " +
            SLAB_DETAILS_SLABRATE + " REAL );";

    void storeDashboard(List<Apartment> apartmentList, List<DailyData> dailyDataList, List<Slabs> slabsList,
                        List<TwoHourForMeter> twoHourForMeterList, List<Meter> meterList, List<Alert> alertList) {
        if(checkIfOldApartmentsGotChangedOrReplaced(apartmentList)){
            deleteAllData();
        }

        SQLiteDatabase db = getWritableDatabase();
        for(Apartment a:apartmentList){
//            Log.d("Apartment",a.toString());
            ContentValues contentValues = new ContentValues();
            contentValues.put(APARTMENT_ID,a.getId());
            contentValues.put(APARTMENT_BILL_AMOUNT,a.getBillAmount());
            contentValues.put(APARTMENT_BILL_DATE,a.getBillDate());
            contentValues.put(APARTMENT_BILL_STATUS,a.getBillPaid());
            contentValues.put(APARTMENT_CURR_ABB,a.getCurrencyAbbrev());
            contentValues.put(APARTMENT_CURR_SYM,a.getCurrencySymbol());
            contentValues.put(APARTMENT_CURR_TEXT,a.getCurrencyText());
            contentValues.put(APARTMENT_FIXED_CHARGE,a.getFixedcharge());
            contentValues.put(APARTMENT_UNIT_ABB,a.getUnitAbbrev());
            contentValues.put(APARTMENT_UNIT_SYM,a.getUnitSymbol());
            contentValues.put(APARTMENT_UNIT_TEXT,a.getUnitText());
            contentValues.put(APARTMENT_NAME,a.getName());
            contentValues.put(APARTMENT_SOCIETY,a.getSociety());
            db.insertWithOnConflict(APARTMENT_DETAILS,null,contentValues,SQLiteDatabase.CONFLICT_REPLACE);
        }
        for(DailyData d:dailyDataList){
//            Log.d("DailyData",d.toString());
            ContentValues contentValues = new ContentValues();
            contentValues.put(DAILY_DATA_CONSUMPTION_DATA_ID,d.getMeterId()+"-"+d.getDate());
            contentValues.put(DAILY_DATA_CONSUMPTION_DATA_VALUE,d.getValue());
            contentValues.put(DAILY_DATA_CONSUMPTION_DATA_APT_ID,d.getAptId());
            contentValues.put(DAILY_DATA_CONSUMPTION_DATA_METER_ID,d.getMeterId());
            contentValues.put(DAILY_DATA_CONSUMPTION_DATA_DATE,d.getDate());

//            Log.d("DataId&Value",d.getMeterId()+"-"+d.getDate());
            db.insertWithOnConflict(DAILY_DATA_CONSUMPTION_DATA_TABLE,null,contentValues,SQLiteDatabase.CONFLICT_REPLACE);
        }
        for(Slabs s:slabsList){
//            Log.d("Slabs",s.toString());
            ContentValues contentValues = new ContentValues();
            contentValues.put(SLAB_DETAILS_APTID,s.getApTid());
            contentValues.put(SLAB_DETAILS_LEVEL,s.getSlabLevel());
            contentValues.put(SLAB_DETAILS_ID,s.getApTid()+"-"+s.getSlabLevel());
            contentValues.put(SLAB_DETAILS_SLABRATE,s.getSlabPrice());
            contentValues.put(SLAB_DETAILS_SLAB,s.getSlabKl());
            db.insertWithOnConflict(SLAB_DETAILS_TABLE,null,contentValues,SQLiteDatabase.CONFLICT_REPLACE);
        }
        for(TwoHourForMeter tm:twoHourForMeterList){
//            Log.d("TwoHourForMeter",tm.toString());
            ContentValues contentValues = new ContentValues();
            contentValues.put(TWO_HOURLY_CONSUMPTION_DATA_APT_ID,tm.getAptId());
            contentValues.put(TWO_HOURLY_CONSUMPTION_DATA_DATE,tm.getDate());
            contentValues.put(TWO_HOURLY_CONSUMPTION_DATA_METER_ID,tm.getMeterId());
            contentValues.put(TWO_HOURLY_CONSUMPTION_DATA_VALUE,tm.getValue());
            contentValues.put(TWO_HOURLY_CONSUMPTION_DATA_SLOT,tm.getSlot());
            contentValues.put(TWO_HOURLY_CONSUMPTION_DATA_ID,tm.getMeterId()+"-"+tm.getDate()+"-"+tm.getSlot());

            db.insertWithOnConflict(TWO_HOURLY_CONSUMPTION_DATA_TABLE,null,contentValues,SQLiteDatabase.CONFLICT_REPLACE);
        }
        for(Meter m:meterList){
//            Log.d("Meter",m.toString());
            ContentValues contentValues = new ContentValues();
            contentValues.put(METER_APTID,m.getAptId());
            contentValues.put(METER_HAS_VALVE,m.getHasValve());
            contentValues.put(METER_APARTMENT_INDEX,m.getApartment_index());
            contentValues.put(METER_ID,m.getId());
            contentValues.put(METER_LOCATION,m.getLocationUser());
            contentValues.put(METER_LOCATION_DEFAULT,m.getLocationDefault());
            contentValues.put(METER_VALVE_CURRENT_STATUS,m.getValveCurrentStatus());
            contentValues.put(METER_VALVE_LAST_OPERATED,m.getValveLastOperated());
            db.insertWithOnConflict(METER_DETAILS,null,contentValues,SQLiteDatabase.CONFLICT_REPLACE);
        }

        db.execSQL("delete from "+ ALARMS_DETAILS);
        for(Alert a:alertList){
            Log.d("AlertTime",a.getTime());
            ++id;
            ContentValues contentValues = new ContentValues();
            contentValues.put(ALARM_APTID,a.getAptId());
            contentValues.put(ALARMID,id+"-"+a.getTime());
            contentValues.put(ALARMS_METERID,a.getMeterId());
            contentValues.put(ALARMS_QUANTITY,a.getQty());
            contentValues.put(ALARMS_TIME,a.getTime());
            if(a.getQty()!=0){
                db.insertWithOnConflict(ALARMS_DETAILS,null,contentValues,SQLiteDatabase.CONFLICT_REPLACE);
            }
        }
        db.close();
    }


    public Apartment getApartment(int id){
        SQLiteDatabase db = getReadableDatabase();
        String query = "SELECT * FROM " + APARTMENT_DETAILS + " WHERE "+APARTMENT_ID+" = "+ id + ";" ;
        Cursor cursor = db.rawQuery(query, null);
        Apartment apartment=new Apartment();
        if(cursor.moveToFirst()){
            apartment.setId(cursor.getInt(cursor.getColumnIndex(APARTMENT_ID)));
            apartment.setUnitSymbol(cursor.getString(cursor.getColumnIndex(APARTMENT_UNIT_SYM)));
            apartment.setUnitText(cursor.getString(cursor.getColumnIndex(APARTMENT_UNIT_TEXT)));
            apartment.setUnitAbbrev(cursor.getString(cursor.getColumnIndex(APARTMENT_UNIT_ABB)));
            apartment.setCurrencySymbol(cursor.getString(cursor.getColumnIndex(APARTMENT_CURR_SYM)));
            apartment.setCurrencyText(cursor.getString(cursor.getColumnIndex(APARTMENT_CURR_TEXT)));
            apartment.setCurrencyAbbrev(cursor.getString(cursor.getColumnIndex(APARTMENT_CURR_ABB)));
            apartment.setBillDate(cursor.getString(cursor.getColumnIndex(APARTMENT_BILL_DATE)));
            apartment.setBillAmount(cursor.getDouble(cursor.getColumnIndex(APARTMENT_BILL_AMOUNT)));
            apartment.setBillPaid(cursor.getString(cursor.getColumnIndex(APARTMENT_BILL_STATUS)));
            apartment.setFixedcharge(cursor.getDouble(cursor.getColumnIndex(APARTMENT_FIXED_CHARGE)));
            apartment.setSociety(cursor.getString(cursor.getColumnIndex(APARTMENT_SOCIETY)));
            apartment.setName(cursor.getString(cursor.getColumnIndex(APARTMENT_NAME)));
        }
        cursor.close();
        db.close();
        return apartment;
    }

    public List<Apartment> loadApartments(){
        SQLiteDatabase db = getReadableDatabase();
        String query = "SELECT * FROM " + APARTMENT_DETAILS +  ";";
        Cursor cursor = db.rawQuery(query, null);
        List<Apartment> apartmentList = new ArrayList<>();
        if(cursor.moveToFirst()){
            do{
                Apartment apartment=new Apartment();
                apartment.setId(cursor.getInt(cursor.getColumnIndex(APARTMENT_ID)));
                apartment.setUnitSymbol(cursor.getString(cursor.getColumnIndex(APARTMENT_UNIT_SYM)));
                apartment.setUnitText(cursor.getString(cursor.getColumnIndex(APARTMENT_UNIT_TEXT)));
                apartment.setUnitAbbrev(cursor.getString(cursor.getColumnIndex(APARTMENT_UNIT_ABB)));
                apartment.setCurrencySymbol(cursor.getString(cursor.getColumnIndex(APARTMENT_CURR_SYM)));
                apartment.setCurrencyText(cursor.getString(cursor.getColumnIndex(APARTMENT_CURR_TEXT)));
                apartment.setCurrencyAbbrev(cursor.getString(cursor.getColumnIndex(APARTMENT_CURR_ABB)));
                apartment.setBillDate(cursor.getString(cursor.getColumnIndex(APARTMENT_BILL_DATE)));
                apartment.setBillAmount(cursor.getDouble(cursor.getColumnIndex(APARTMENT_BILL_AMOUNT)));
                apartment.setBillPaid(cursor.getString(cursor.getColumnIndex(APARTMENT_BILL_STATUS)));
                apartment.setFixedcharge(cursor.getDouble(cursor.getColumnIndex(APARTMENT_FIXED_CHARGE)));
                apartment.setSociety(cursor.getString(cursor.getColumnIndex(APARTMENT_SOCIETY)));
                apartment.setName(cursor.getString(cursor.getColumnIndex(APARTMENT_NAME)));
                apartmentList.add(apartment);
            }while (cursor.moveToNext());
        }
        Log.d("data",String.valueOf(apartmentList.size()));
        cursor.close();
        db.close();
        return apartmentList;
    }

    private void deleteAllData() {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS " + APARTMENT_DETAILS);
        db.execSQL("DROP TABLE IF EXISTS " + METER_DETAILS);
        db.execSQL("DROP TABLE IF EXISTS " + ALARMS_DETAILS);
        db.execSQL("DROP TABLE IF EXISTS " + TWO_HOURLY_CONSUMPTION_DATA_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + DAILY_DATA_CONSUMPTION_DATA_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + SLAB_DETAILS_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + H_DAILY_DATA_CONSUMPTION_DATA_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + H_ALERT_TABLE);
        onCreate(db);
        // add reset for all data status sharedpreference

    }

    private static boolean checkIfOldApartmentsGotChangedOrReplaced(List<Apartment> apartmentList) {
        return false;
    }

    public List<Meter> getMeterForApartment(int aptid) {

        SQLiteDatabase db = getReadableDatabase();
        String query = "SELECT * FROM " + METER_DETAILS + " WHERE " + METER_APTID + " =" + aptid + ";";
        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();
        boolean stat=false;
        List<Meter> meterList=new ArrayList<>();
        if (cursor.moveToFirst()) {
            do{
                Meter meter = new Meter();
                stat=true;
                meter.setId(cursor.getInt(cursor.getColumnIndex(METER_ID)));
                meter.setLocationUser(cursor.getString(cursor.getColumnIndex(METER_LOCATION)));
                meter.setLocationDefault(cursor.getString(cursor.getColumnIndex(METER_LOCATION_DEFAULT)));
                meter.setHasValve(cursor.getInt(cursor.getColumnIndex(METER_HAS_VALVE)));
                meter.setValveCurrentStatus(cursor.getString(cursor.getColumnIndex(METER_VALVE_CURRENT_STATUS)));
                meter.setValveLastOperated(cursor.getString(cursor.getColumnIndex(METER_VALVE_LAST_OPERATED)));
                meter.setAptId(cursor.getInt(cursor.getColumnIndex(METER_APTID)));
                meterList.add(meter);
            }while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return stat?meterList:null;
    }

    public List<String> getActiveAlarmIdsforMeter(int meterid) {
        SQLiteDatabase db = getReadableDatabase();
        int alarmid=0;
        String query = "SELECT * FROM " + ALARMS_DETAILS + " WHERE " +  ALARMS_METERID + " ="+meterid+";";

        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();
        boolean stat=false;
        List<String> alarmList=new ArrayList<>();
        try{
            if (cursor.moveToFirst()) {
                do{
                    String alarm ;
                    stat=true;
                    alarm=(cursor.getString(cursor.getColumnIndex(ALARMID)));
                    SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm");

                    long time= sdf.parse((cursor.getString(cursor.getColumnIndex(ALARMS_TIME)))).getTime();
                    if(time+900000>new Date().getTime()){
                        alarmList.add(alarm);
                    }
                }while (cursor.moveToNext());
            }
        }catch(Exception e){
            e.printStackTrace();
            stat = false;
        }finally {
            cursor.close();
            db.close();
        }
        return stat?alarmList:null;
    }

    public DashboardData getDashboardData(DashDataDates dashDataDates) {

        Log.d("DashboardDates", String.valueOf(dashDataDates.hourlydatayesterdaydates));
        DashboardData dashboardData=new DashboardData();
        SQLiteDatabase database=getReadableDatabase();
        int count=0;
        for(String s:dashDataDates.hourlydatadates){
            String query = "SELECT * FROM " + TWO_HOURLY_CONSUMPTION_DATA_TABLE +
                    " WHERE " + TWO_HOURLY_CONSUMPTION_DATA_ID + " =\"" + s +
                    "\";";
            Cursor cursor = database.rawQuery(query, null);
            if (cursor.moveToFirst()) {
                dashboardData.hourlydatalitres[count]=cursor.getDouble(cursor.getColumnIndex(TWO_HOURLY_CONSUMPTION_DATA_VALUE));
                dashboardData.hourlytotal+=dashboardData.hourlydatalitres[count];
                Log.d(String.valueOf(dashboardData.hourlydatalitres[count]),query);
                Log.d("valuett",cursor.getString(cursor.getColumnIndex(TWO_HOURLY_CONSUMPTION_DATA_DATE)));
                Log.d("Hourly Total in sql", String.valueOf(dashboardData.hourlytotal));
                Log.d("Hourly lITRES in sql", String.valueOf(dashboardData.hourlydatalitres[count]));
                count++;
            }
            cursor.close();
        }
        count=0;
        for(String s:dashDataDates.hourlydatayesterdaydates){
            String query = "SELECT * FROM " + TWO_HOURLY_CONSUMPTION_DATA_TABLE +
                    " WHERE " + TWO_HOURLY_CONSUMPTION_DATA_ID + " =\"" + s +
                    "\";";
            Cursor cursor = database.rawQuery(query, null);
            if (cursor.moveToFirst()) {
                dashboardData.hourlydatalitresyesterday[count]=cursor.getDouble(cursor.getColumnIndex(TWO_HOURLY_CONSUMPTION_DATA_VALUE));
                dashboardData.hourlytotalyesterday+=dashboardData.hourlydatalitresyesterday[count];
                Log.d("HourlyLitresyesterday", Arrays.toString(dashboardData.hourlydatalitresyesterday));
                count++;
            }
            cursor.close();

        }
        count=0;
        for(String s:dashDataDates.weekldatalitresdates){

            String query = "SELECT * FROM " + DAILY_DATA_CONSUMPTION_DATA_TABLE + " WHERE " + DAILY_DATA_CONSUMPTION_DATA_ID + " =\"" + s + "\";";

            Cursor cursor = database.rawQuery(query, null);

            if (cursor.moveToFirst()) {
                dashboardData.weekldatalitres[count]=cursor.getDouble(cursor.getColumnIndex(DAILY_DATA_CONSUMPTION_DATA_VALUE));
                dashboardData.weeklytotal+=dashboardData.weekldatalitres[count];
                count++;
            }
            cursor.close();


        }
        count=0;
        for(String s:dashDataDates.weekldatalitrespastdates){

            String query = "SELECT * FROM " + DAILY_DATA_CONSUMPTION_DATA_TABLE + " WHERE " + DAILY_DATA_CONSUMPTION_DATA_ID + " =\"" + s + "\";";

            Cursor cursor = database.rawQuery(query, null);

            if (cursor.moveToFirst()) {
                dashboardData.weekldatalitrespast[count]=cursor.getDouble(cursor.getColumnIndex(DAILY_DATA_CONSUMPTION_DATA_VALUE));
                dashboardData.weeklypasttotal+=dashboardData.weekldatalitrespast[count];
                count++;
            }
            cursor.close();


        }
        for(String s:dashDataDates.monthdatalitresdates1){
            String query = "SELECT * FROM " + DAILY_DATA_CONSUMPTION_DATA_TABLE + " WHERE " + DAILY_DATA_CONSUMPTION_DATA_ID + " =\"" + s + "\";";

            Cursor cursor = database.rawQuery(query, null);
            double val=0;
            if (cursor.moveToFirst()) {
                try{
                    val= dashboardData.monthdatalitres[0];
                }catch (Exception e){
                    e.printStackTrace();
                }

                dashboardData.monthdatalitres[0]+=cursor.getDouble(cursor.getColumnIndex(DAILY_DATA_CONSUMPTION_DATA_VALUE));
                Log.d(s,String.valueOf(dashboardData.monthdatalitres[0]-val));
            }
            cursor.close();
        }

        for(String s:dashDataDates.monthdatalitresdates2){
            String query = "SELECT * FROM " + DAILY_DATA_CONSUMPTION_DATA_TABLE + " WHERE " + DAILY_DATA_CONSUMPTION_DATA_ID + " =\"" + s + "\";";

            Cursor cursor = database.rawQuery(query, null);
            double val=0;
            if (cursor.moveToFirst()) {
                try{
                    val= dashboardData.monthdatalitres[1];
                }catch (Exception e){
                    e.printStackTrace();
                }
                dashboardData.monthdatalitres[1]+=cursor.getDouble(cursor.getColumnIndex(DAILY_DATA_CONSUMPTION_DATA_VALUE));
                Log.d(s,String.valueOf(dashboardData.monthdatalitres[1]-val));
            }
            cursor.close();
        }

        for(String s:dashDataDates.monthdatalitresdates3){
            String query = "SELECT * FROM " + DAILY_DATA_CONSUMPTION_DATA_TABLE + " WHERE " + DAILY_DATA_CONSUMPTION_DATA_ID + " =\"" + s + "\";";

            Cursor cursor = database.rawQuery(query, null);
            double val=0;
            if (cursor.moveToFirst()) {
                try{
                    val= dashboardData.monthdatalitres[2];
                }catch (Exception e){
                    e.printStackTrace();
                }
                dashboardData.monthdatalitres[2]+=cursor.getDouble(cursor.getColumnIndex(DAILY_DATA_CONSUMPTION_DATA_VALUE));
                Log.d(s,String.valueOf(dashboardData.monthdatalitres[2]-val));
            }
            cursor.close();
        }
        dashboardData.monthlytotal=dashboardData.monthdatalitres[0]+dashboardData.monthdatalitres[1]+dashboardData.monthdatalitres[2];
        database.close();
        return dashboardData;
    }

    public List<Slabs> getSlabs(int selectedAptId) {
        List<Slabs> slabsList = new ArrayList<>();
        SQLiteDatabase database = getReadableDatabase();
        String query = "SELECT * FROM " + SLAB_DETAILS_TABLE + " WHERE " + SLAB_DETAILS_APTID + " =" + selectedAptId + ";";
        Cursor cursor = database.rawQuery(query, null);
        if(cursor.moveToFirst()){
            do{
                Slabs slabs=new Slabs();
                slabs.setSlabLevel(cursor.getInt(cursor.getColumnIndex(SLAB_DETAILS_LEVEL)));
                slabs.setApTid(cursor.getInt(cursor.getColumnIndex(SLAB_DETAILS_APTID)));
                slabs.setSlabPrice(cursor.getDouble(cursor.getColumnIndex(SLAB_DETAILS_SLABRATE)));
                slabs.setSlabKl(cursor.getDouble(cursor.getColumnIndex(SLAB_DETAILS_SLAB)));
                Log.d("opopop",String.valueOf(slabs.getSlabKl()));
                Log.d("opopop",String.valueOf(slabs.getSlabPrice()));

                slabsList.add(slabs);
            }while (cursor.moveToNext());
        }
        cursor.close();
        database.close();
        return slabsList;
    }
    public void storeHistory(List<DailyData> dailyDataList, List<HAlert> alertList) {
        SQLiteDatabase db = getWritableDatabase();
        for(DailyData d:dailyDataList){
//            Log.d("DailyData",d.toString());
            ContentValues contentValues = new ContentValues();
            contentValues.put(H_DAILY_DATA_CONSUMPTION_DATA_ID,d.getAptId()+"-"+d.getDate());
            contentValues.put(H_DAILY_DATA_CONSUMPTION_DATA_VALUE,d.getValue());
            contentValues.put(H_DAILY_DATA_CONSUMPTION_DATA_APT_ID,d.getAptId());
            contentValues.put(H_DAILY_DATA_CONSUMPTION_DATA_DATE,d.getDate());
            db.insertWithOnConflict(H_DAILY_DATA_CONSUMPTION_DATA_TABLE,null,contentValues,SQLiteDatabase.CONFLICT_REPLACE);
        }

        for(HAlert a:alertList){
//            Log.d("DailyData",d.toString());
            ContentValues contentValues = new ContentValues();
            contentValues.put(H_ALERT_VALUE,a.getValue());
            contentValues.put(H_ALERT_APTID,a.getAptId());
            contentValues.put(H_ALERT_DATE,a.getDate());
            db.insertWithOnConflict(H_ALERT_TABLE,null,contentValues,SQLiteDatabase.CONFLICT_REPLACE);
        }

        db.close();

    }

    public HAlert getHAlert(int selectedAptId, String date) {
        HAlert alert=new HAlert();
        SQLiteDatabase database = getReadableDatabase();
        String query = "SELECT * FROM " + H_ALERT_TABLE + " WHERE "
                + H_ALERT_DATE + " =\""+ date + "\" AND "
                + H_ALERT_APTID + " =" + selectedAptId + ";";
        Cursor cursor = database.rawQuery(query, null);
        alert.setDate(date);
        alert.setAptId(selectedAptId);
        if(cursor.moveToFirst()){
            alert.setValue(cursor.getInt(cursor.getColumnIndex(H_ALERT_VALUE)));
        }else{
            alert.setValue(0);
        }
        cursor.close();
        database.close();
        return alert;
    }

    public double getHDailyData(String date, int selectedAptId) {
        SQLiteDatabase database = getReadableDatabase();
        String query = "SELECT * FROM " + H_DAILY_DATA_CONSUMPTION_DATA_TABLE + " WHERE "
                + H_DAILY_DATA_CONSUMPTION_DATA_DATE + " =\""+ date + "\" AND "
                + H_DAILY_DATA_CONSUMPTION_DATA_APT_ID + " =" + selectedAptId + ";";
        Cursor cursor = database.rawQuery(query, null);
        double ans =0;
        if(cursor.moveToFirst()){
            ans= cursor.getDouble(cursor.getColumnIndex(H_DAILY_DATA_CONSUMPTION_DATA_VALUE));
        }
        cursor.close();
        database.close();
        return ans;
    }

    public void DeleteAlert(String alarmtoload) {
        SQLiteDatabase db = getWritableDatabase();
        int alarmid=0;
        String query = "DELETE FROM " + ALARMS_DETAILS + " WHERE " +  ALARMID + " ="+alarmtoload+";";
        db.execSQL(query);
    }

    public void storeAlerts(List<Alert> alertList) {
        SQLiteDatabase db = getWritableDatabase();
        Log.d("AlertList", String.valueOf(alertList));
        id = 0;
        for(Alert a:alertList){
            ++id;
            //            Log.d("Alert",a.toString());
            ContentValues contentValues = new ContentValues();
            contentValues.put(ALARM_APTID,a.getAptId());
            contentValues.put(ALARMID,a.getAptId()+"-"+a.getMeterId()+"-"+a.getTime());
            contentValues.put(ALARMS_METERID,a.getMeterId());
            contentValues.put(ALARMS_QUANTITY,a.getQty());
            Log.d("InsertingTime",a.getTime());
            contentValues.put(ALARMS_TIME,a.getTime());
            if(a.getQty()!=0){
                db.execSQL("delete from "+ALARMS_DETAILS+" where "+ALARMS_METERID+ " = "+a.getMeterId() );
                db.insertWithOnConflict(ALARMS_DETAILS,null,contentValues,SQLiteDatabase.CONFLICT_REPLACE);
            }

        }
        db.close();
    }

    public List<Alert> getActiveAlarms(int id) {
        SQLiteDatabase db = getReadableDatabase();
        int alarmid=0;
        String query = "SELECT * FROM " + ALARMS_DETAILS + " WHERE " +  ALARM_APTID + " ="+id+";";
        Cursor cursor = db.rawQuery(query, null);
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm");

        boolean stat=false;
        List<Alert> alertList =  new ArrayList<>();
        try{
            if (cursor.moveToFirst()) {
                do{
                    Alert alert = new Alert();
                    alert.setTime(cursor.getString(cursor.getColumnIndex(ALARMS_TIME)));
                    alert.setQty(cursor.getDouble(cursor.getColumnIndex(ALARMS_QUANTITY)));
                    alert.setMeterId(cursor.getInt(cursor.getColumnIndex(ALARMS_METERID)));
                    alert.setAptId(cursor.getInt(cursor.getColumnIndex(ALARM_APTID)));
                    long time= sdf.parse((cursor.getString(cursor.getColumnIndex(ALARMS_TIME)))).getTime();
                    Log.d("ActiveTime", String.valueOf(time));
                    if(time+900000>new Date().getTime()){
                        alertList.add(alert);
                    }else {
                        alertList.add(alert);
                    }
                    stat=true;
                }while (cursor.moveToNext());
            }/////////////////
        }catch(Exception e){
            e.printStackTrace();
            stat = false;
        }finally {
            cursor.close();
            db.close();
        }
        return stat?alertList:null;
    }

    public void updateMeter(Meter meter) {
        ContentValues values = new ContentValues();
        values.put(METER_ID,meter.getId());
        values.put(METER_VALVE_LAST_OPERATED,meter.getValveLastOperated());
        values.put(METER_VALVE_CURRENT_STATUS,meter.getValveCurrentStatus());
        values.put(METER_LOCATION_DEFAULT,meter.getLocationDefault());
        values.put(METER_LOCATION,meter.getLocationUser());
        values.put(METER_HAS_VALVE,meter.getHasValve());
        SQLiteDatabase db = getWritableDatabase();
        db.update(METER_DETAILS, values, METER_ID + " ='" + meter.getId() + "' ",null);
        db.close();
    }
     public void updateDashboard(List<TwoHourForMeter> twoHourForMeterList ){
         SQLiteDatabase db = getWritableDatabase();
         for(TwoHourForMeter tm:twoHourForMeterList){
             db.execSQL("delete from "+ TWO_HOURLY_CONSUMPTION_DATA_TABLE+ " where "+TWO_HOURLY_CONSUMPTION_DATA_ID+ " = "+tm.getMeterId()+"-"+tm.getDate()+"-"+tm.getSlot());
             ContentValues contentValues = new ContentValues();
             contentValues.put(TWO_HOURLY_CONSUMPTION_DATA_APT_ID,tm.getAptId());
             contentValues.put(TWO_HOURLY_CONSUMPTION_DATA_DATE,tm.getDate());
             contentValues.put(TWO_HOURLY_CONSUMPTION_DATA_METER_ID,tm.getMeterId());
             contentValues.put(TWO_HOURLY_CONSUMPTION_DATA_VALUE,tm.getValue());
             contentValues.put(TWO_HOURLY_CONSUMPTION_DATA_ID,tm.getMeterId()+"-"+tm.getDate()+"-"+tm.getSlot());
             contentValues.put(TWO_HOURLY_CONSUMPTION_DATA_SLOT,tm.getSlot());
             Log.d("Two hourly value", String.valueOf(tm.getValue()));
             db.insertWithOnConflict(TWO_HOURLY_CONSUMPTION_DATA_TABLE,null,contentValues,SQLiteDatabase.CONFLICT_REPLACE);

         }

     }

    private void updataDailyConsumptionTable() {
    }

    private List<TwoHourForMeter>[] partition(List<TwoHourForMeter> twoHourForMeterList, int size) {
        // get size of the list
        int size_list = twoHourForMeterList.size();

        // calculate number of partitions --> m sublists each of size n
        int m = size_list / size;
        if (size_list % size != 0)
            m++;

        // create m empty lists
        List<TwoHourForMeter>[] partition = new ArrayList[m];
        for (int i = 0; i < m; i++)
        {
            int fromIndex = i*size;
            int toIndex = (i*size + size < size) ? (i*size + size) : size;

            partition[i] = new ArrayList<>(twoHourForMeterList.subList(fromIndex, toIndex));
        }

        // return the lists
        return partition;
     }

    public void deletedata() {
        SQLiteDatabase db=getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS " + APARTMENT_DETAILS);
        db.execSQL("DROP TABLE IF EXISTS " + METER_DETAILS);
        db.execSQL("DROP TABLE IF EXISTS " + ALARMS_DETAILS);
        db.execSQL("DROP TABLE IF EXISTS " + TWO_HOURLY_CONSUMPTION_DATA_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + DAILY_DATA_CONSUMPTION_DATA_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + SLAB_DETAILS_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + H_DAILY_DATA_CONSUMPTION_DATA_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + H_ALERT_TABLE);
        onCreate(db);
    }


    public Meter getMeter(String meterid) {
        SQLiteDatabase db = getReadableDatabase();
        String query = "SELECT * FROM " + METER_DETAILS + " WHERE " + METER_ID + " =\"" + meterid + "\";";
        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();
        boolean stat=false;
        Meter meter = new Meter();
        if (cursor.moveToFirst()) {
            stat=true;
            meter.setId(cursor.getInt(cursor.getColumnIndex(METER_ID)));
            meter.setLocationUser(cursor.getString(cursor.getColumnIndex(METER_LOCATION)));
            meter.setApartment_index(cursor.getInt(cursor.getColumnIndex(METER_APARTMENT_INDEX)));
            meter.setLocationDefault(cursor.getString(cursor.getColumnIndex(METER_LOCATION_DEFAULT)));
            meter.setHasValve(cursor.getInt(cursor.getColumnIndex(METER_HAS_VALVE)));
            meter.setValveCurrentStatus(cursor.getString(cursor.getColumnIndex(METER_VALVE_CURRENT_STATUS)));
            meter.setValveLastOperated(cursor.getString(cursor.getColumnIndex(METER_VALVE_LAST_OPERATED)));
            meter.setAptId(cursor.getInt(cursor.getColumnIndex(METER_APTID)));
        }
        cursor.close();
        db.close();
        return stat?meter:null;
    }

    public void updateAlert(Alert a) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(ALARM_APTID,a.getAptId());
//        Log.d("AlarmID",a.getAptId()+"-"+a.getMeterId()+"-"+a.getTime());
        contentValues.put(ALARMID,a.getAptId()+"-"+a.getMeterId()+"-"+a.getTime());
        contentValues.put(ALARMS_METERID,a.getMeterId());
        contentValues.put(ALARMS_QUANTITY,a.getQty());
        contentValues.put(ALARMS_TIME,a.getTime());
        if(a.getQty()!=0){
//            db.delete(ALARMS_DETAILS,ALARMS_METERID+"="+a.getMeterId(),null);
//                db.insert(ALARMS_DETAILS,null,contentValues);
            db.execSQL("delete from "+ ALARMS_DETAILS+ " where "+ALARMS_METERID+" = "+a.getMeterId());
            //db.execSQL("delete from "+ ALARMS_DETAILS+" where alarm_meterid"+ " ="+a.getMeterId());
            db.insertWithOnConflict(ALARMS_DETAILS,null,contentValues,SQLiteDatabase.CONFLICT_REPLACE);
//            db.update(ALARMS_DETAILS, contentValues, ALARMS_METERID + " ="+a.getMeterId(),null);
//            db.close();
        }
    }

    public void RefreshMeterData() {
    }


    public void deleteAlert(Alert alarm) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("delete from "+ALARMS_DETAILS+" where "+ALARMS_METERID +" ="+alarm.getMeterId());
    }
    public List<TwoHourForMeter> getTwoHourlyData(String selectedAptId){
        SQLiteDatabase db = getReadableDatabase();
        String query = "SELECT * FROM " + TWO_HOURLY_CONSUMPTION_DATA_TABLE + " WHERE " + TWO_HOURLY_CONSUMPTION_DATA_APT_ID + " =\"" + selectedAptId + "\";";
        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();
        boolean stat=false;
        List<TwoHourForMeter> meterList = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {

                TwoHourForMeter forMeter = new TwoHourForMeter();
                forMeter.setMeterId(cursor.getInt(cursor.getColumnIndex(TWO_HOURLY_CONSUMPTION_DATA_METER_ID)));
                forMeter.setAptId(cursor.getInt(cursor.getColumnIndex(TWO_HOURLY_CONSUMPTION_DATA_APT_ID)));
                forMeter.setDate(cursor.getString(cursor.getColumnIndex(TWO_HOURLY_CONSUMPTION_DATA_DATE)));
                forMeter.setValue(cursor.getDouble(cursor.getColumnIndex(TWO_HOURLY_CONSUMPTION_DATA_VALUE)));
                forMeter.setSlot(cursor.getInt(cursor.getColumnIndex(TWO_HOURLY_CONSUMPTION_DATA_SLOT)));
                meterList.add(forMeter);
                stat = true;
            }while(cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return stat?meterList:null;
    }

}
