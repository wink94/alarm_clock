package com.windula.alam_clock10;

import android.app.IntentService;
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
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class AlarmService extends IntentService {

    private ArrayList<String> alarms;
    // Binder given to clients
    private final IBinder binder = new LocalBinder();
/*    private Timer timer;
    private TimerTask timerTask;
    private int counter=0;
    long oldTime=0;*/

    private static final String TAG = "IntentService";

    public AlarmService() {
        super("AlarmService");

        alarms=new ArrayList<>();

    }

    @Override
    public int onStartCommand( Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        readDb();
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        //throw new UnsupportedOperationException("Not yet implemented");
        return binder;
    }

   /* public void startTimer() {
        //set a new Timer
        timer = new Timer();

        //initialize the TimerTask's job
        initializeTimerTask();

        //schedule the timer, to wake up every 1 second
        timer.schedule(new TimerTask() {
            @Override
            public void run() {

                readDb();
                String timeStamp = new SimpleDateFormat("HH:mm").format(Calendar.getInstance().getTime());
                Log.i("in timer", "in timer ++++  "+ (counter++));
                *//*for(String time:alarms){
                    if(time.equalsIgnoreCase(timeStamp)){
                        Toast.makeText(getApplicationContext(),time ,Toast.LENGTH_LONG).show();

                        Intent popup = new Intent(getApplicationContext(), AlarmQuiz.class);
                        startActivity(popup);
                    }
                }*//*

                if(alarms.contains(timeStamp)){
                    Log.i("in timer", "alarm!!! ");
                }

            }
        },1000,1000);//
    }

    public void initializeTimerTask() {

        timerTask = new TimerTask() {
            public void run() {
                Log.i("in timer", "in timer ++++  "+ (counter++));
            }
        };

    }*/


    @Override
    protected void onHandleIntent(Intent intent) {



        //Log.i(TAG,intent.getDataString());
    }

    private void readDb(){

        SQLiteDatabase db = new AlarmDbHelper(getApplicationContext()).getReadableDatabase();
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

            if(cursor.getColumnCount()>0){
                if (cursor.moveToFirst()) {
                    while (!cursor.isAfterLast()) {
                        String time = cursor.getString(cursor.getColumnIndex("time"));
                        //String title  =  cursor.getString(cursor.getColumnIndex("title"));

                        alarms.add(time);
                        cursor.moveToNext();
                    }

                }
            }

        }

    }

/*
    @Override
    public void onDestroy() {
        super.onDestroy();
        stoptimertask();
    }*/

   /* public void stoptimertask() {
        //stop the timer, if it's not already null
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }*/

    public class LocalBinder extends Binder {
        AlarmService getService() {
            // Return this instance of LocalService so clients can call public methods
            return AlarmService.this;
        }
    }
}
