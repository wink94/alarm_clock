package com.windula.alam_clock10;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class AlarmDbHelper extends SQLiteOpenHelper {

    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "AlarmSetter.db";

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + AlarmContract.AlarmEntry.TABLE_NAME + " (" +
                    AlarmContract.AlarmEntry._ID + " INTEGER PRIMARY KEY," +
                    AlarmContract.AlarmEntry.COLUMN_TIME + " TEXT," +
                    AlarmContract.AlarmEntry.COLUMN_TITLE + " TEXT," +
                    AlarmContract.AlarmEntry.COLUMN_NAME_ALARM + " TEXT)";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + AlarmContract.AlarmEntry.TABLE_NAME;

    private static final String TAG = "DB";

    public AlarmDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);

        Log.i(TAG,"DB created");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
    }
}
