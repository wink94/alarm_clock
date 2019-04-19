package com.windula.alam_clock10;


import android.app.Service;
import android.content.Intent;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Binder;
import android.os.IBinder;
import android.provider.BaseColumns;
import android.util.Log;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


public class AlarmService extends Service {

    private ArrayList<String> alarms;
    // Binder given to clients
    private final IBinder binder = new LocalBinder();
    private Timer timer;
    private TimerTask timerTask;
    private int counter=0;
    public static String alarmID;
    long oldTime=0;

    private static final String TAG = "IntentService";


    @Override
    public int onStartCommand( Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        Log.i(TAG, "Alarm service start");

        startTimer();
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        //throw new UnsupportedOperationException("Not yet implemented");
        return binder;
    }

    public void startTimer() {
        //set a new Timer
        timer = new Timer();

        //initialize the TimerTask's job
        initializeTimerTask();

        //schedule the timer, to wake up every 1 second
        timer.schedule(new TimerTask() {
            @Override
            public void run() {


                String timeStamp = new SimpleDateFormat("HH:mm").format(new Date());

                Log.i("in timer", "in timer ++++  "+ (counter++)+" current time :"+timeStamp);


                if(readDb(timeStamp)){
                    Log.i("in timer", "alarm!!! ");
                    Intent quizView=new Intent(getApplicationContext(),AlarmQuiz.class);
                    quizView.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                    startActivity(quizView);
                }



            }
        },0,1000*57);//
    }

    public void initializeTimerTask() {

        timerTask = new TimerTask() {
            public void run() {
                Log.i("in timer", "in timer ++++  "+ (counter++));
            }
        };

    }


    private boolean readDb(String selectTime){

        SQLiteDatabase db = new AlarmDbHelper(getApplicationContext()).getReadableDatabase();
        if(db!=null) {
            String[] projection = {
                    BaseColumns._ID,

                    AlarmContract.AlarmEntry.COLUMN_TIME,
                    AlarmContract.AlarmEntry.COLUMN_NAME_ALARM
            };

            String selection =  AlarmContract.AlarmEntry.COLUMN_TIME + " = ?";
            String[] selectionArgs = { selectTime };

            Cursor cursor = db.query(
                    AlarmContract.AlarmEntry.TABLE_NAME,
                    projection,
                    selection,
                    selectionArgs,
                    null,
                    null,
                    null
            );

            if(cursor.getCount()>0) {

                if (cursor.moveToFirst()) {
                    while (!cursor.isAfterLast()) {
                        this.alarmID = cursor.getString(cursor.getColumnIndex("alarm"));
                        //String title  =  cursor.getString(cursor.getColumnIndex("title"));

                        cursor.moveToNext();
                    }
                    return true;
                }

            }

        }

        return false;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stoptimertask();
    }

    public void stoptimertask() {
        //stop the timer, if it's not already null
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    public class LocalBinder extends Binder {
        AlarmService getService() {
            // Return this instance of LocalService so clients can call public methods
            return AlarmService.this;
        }
    }
}
