package com.windula.alam_clock10;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

import java.util.Date;

public class Alarm {
    private int hour;
    private int minute;
    private String time;
    private String alarmTone;
    private int alarmID;
    private String alarmTitle;

    //private AlarmDbHelper dbHelper;


    public String getAlarmTitle() {
        return alarmTitle;
    }

    public void setAlarmTitle(String alarmTitle) {
        this.alarmTitle = alarmTitle;
    }

    public String getTime() {
        return time;
    }

    public void setTimeString(String flag,int hr,int min) { //tyoe of the flag used eg:- : . / -
        this.time = Integer.toString(hr)+flag+Integer.toString(min);
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    public String getAlarmTone() {
        return alarmTone;
    }

    public void setAlarmTone(String alarmTone) {
        this.alarmTone = alarmTone;
    }

    public int getAlarmID() {
        return alarmID;
    }

    public void setAlarmID(int alarmID) {
        this.alarmID = alarmID;
    }

    /*public void setDbHelper(){
        dbHelper= new AlarmDbHelper(getContext());
    }*/

    public boolean setAlarm(AlarmDbHelper dbHelper){
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(AlarmContract.AlarmEntry.COLUMN_TIME, time);
        values.put(AlarmContract.AlarmEntry.COLUMN_NAME_ALARM, alarmTone);
        values.put(AlarmContract.AlarmEntry.COLUMN_TITLE, alarmTitle);

        long newRowId = db.insert(AlarmContract.AlarmEntry.TABLE_NAME, null, values);

        return (newRowId==-1)? false:true;
    }

    public Cursor getAllAlarms(AlarmDbHelper dbHelper){
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        if(db!=null) {
            String[] projection = {
                    BaseColumns._ID,
                    AlarmContract.AlarmEntry.COLUMN_TITLE,
                    AlarmContract.AlarmEntry.COLUMN_TIME
            };

            Cursor cursor = db.query(
                    AlarmContract.AlarmEntry.TABLE_NAME,
                    projection,
                    null,
                    null,
                    null,
                    null,
                    null
            );
            return cursor;
        }
        else
            return null;

    }

    public static boolean removeAlarm(AlarmDbHelper dbHelper,String time){
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        String selection = AlarmContract.AlarmEntry.COLUMN_TIME + " LIKE ?";

        String[] selectionArgs = { time };

        int deletedRows = db.delete( AlarmContract.AlarmEntry.TABLE_NAME, selection, selectionArgs);

        return (deletedRows==0)? false:true;
    }

}
