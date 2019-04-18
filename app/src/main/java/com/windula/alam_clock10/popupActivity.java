package com.windula.alam_clock10;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TimePicker;
import android.widget.Toast;

import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class popupActivity extends Activity {

    private HashMap<String,Integer> alarmMap;
    private MediaPlayer mediaPlayer;
    private TimePicker timePicker;
    private EditText alarmTitle;
    private Button setButton;
    private Button cancelButton;
    private Spinner alarmSpinner;

    private AlarmDbHelper dbHelper;

    private AlarmReceiver ar;

    private final int ADD_ALARM=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popup);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width=dm.widthPixels;
        int height=dm.heightPixels;

        getWindow().setLayout((int)(width*.7),(int)(height*.8));

        //timepicker
        timePicker=(TimePicker) findViewById(R.id.timePicker);
        timePicker.setIs24HourView(true);

        //button setup
        setButton=findViewById(R.id.btnSet);
        cancelButton=findViewById(R.id.btnCancel);

        //alarm title
        alarmTitle=findViewById(R.id.setTitle);

        //alarm spinner
        alarmSpinner=findViewById(R.id.alarmSpinnerList);

        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.gravity = Gravity.CENTER;
        params.x=0;
        params.y=-30;
        getWindow().setAttributes(params);

        getAudioFilesToSpinner();

        dbHelper=new AlarmDbHelper(getApplicationContext());

        setButtonClick();
        cancelButttonClick();


        /*Intent intent = new Intent(this, AlarmService.class);
        bindService(intent, connection, Context.BIND_AUTO_CREATE);*/

        //register reciever
        ar=new AlarmReceiver();
        IntentFilter filter = new IntentFilter("com.windula.alarm_clock10.ALARM_RECEIVER");
        //filter.addAction(Intent.);
        this.registerReceiver(ar, filter);
    }

    private void getAudioFilesToSpinner(){
        Spinner spinner = (Spinner) findViewById(R.id.alarmSpinnerList);
        List<String> spinnerArray=new ArrayList<String>();;
        alarmMap =new HashMap<String,Integer>();

        Field[] fields=R.raw.class.getFields();

        for(int count=0; count < fields.length; count++){
            int value=getResources().getIdentifier(fields[count].getName(),"raw",getPackageName());
            String key=fields[count].getName();
            spinnerArray.add(key);
            alarmMap.put(key,value);
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item,spinnerArray);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);



        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String key=parent.getItemAtPosition(position).toString();

                if( mediaPlayer==null ){
                    mediaPlayer =  MediaPlayer.create(view.getContext(),alarmMap.get(key));
                    mediaPlayer.start();
                }
                else{
                  mediaPlayer.reset();
                    try {
                        mediaPlayer.setDataSource(view.getContext(), Uri.parse("android.resource://"
                                + getPackageName()
                                + "/"+alarmMap.get(key)));
                        mediaPlayer.prepare();
                        mediaPlayer.start();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                if(mediaPlayer.isPlaying() && mediaPlayer!=null){
                    mediaPlayer.stop();
                }

            }
        });

       /* spinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String key=parent.getItemAtPosition(position).toString();

                if(mediaPlayer==null){
                    mediaPlayer =  MediaPlayer.create(view.getContext(),alarmMap.get(key));
                    mediaPlayer.start();
                }
                else {
                    mediaPlayer.reset();
                    try {
                        mediaPlayer.setDataSource(view.getContext(), Uri.parse("android.resource://com.windula.alarm_clock10/" +alarmMap.get(key)));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            }
        });*/

    }

   /* @Override
    protected void onPause() {
        super.onPause();
        mediaPlayer.release();
        mediaPlayer = null;
    }*/

    @Override
    protected void onStop() {
        super.onStop();
        mediaPlayer.release();
        mediaPlayer = null;
        //unregisterReceiver(ar);
       // unbindService(connection);
        //mBound = false;
    }

    private void setButtonClick(){
        setButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Alarm setAlarm=new Alarm();

                setAlarm.setAlarmTitle(alarmTitle.getText().toString());
                setAlarm.setAlarmTone(alarmSpinner.getSelectedItem().toString());
                //setAlarm.setHour(timePicker.getCurrentHour());
                //setAlarm.setMinute(timePicker.getCurrentMinute());

                setAlarm.setTimeString(":",timePicker.getCurrentHour(),timePicker.getCurrentMinute());

                if(setAlarm.setAlarm(dbHelper)){
                    Toast.makeText(getApplicationContext(),"Alarm Was Set",Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent();
                    intent.putExtra("time",setAlarm.getTime());

                    //set intent to broadcaster
                    Intent broadcast = new Intent();
                    broadcast.setAction("com.windula.alarm_clock10.ALARM_RECEIVER");
                    broadcast.putExtra("data",ADD_ALARM);
                    sendBroadcast(broadcast);

                    setResult(RESULT_OK,intent);

                    finish();
                }
                else {
                    Toast.makeText(getApplicationContext(),"Something went wrong",Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    private void cancelButttonClick(){
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();

        if(!ar.isInitialStickyBroadcast()){
            unregisterReceiver(ar);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dbHelper.close();
        //unregisterReceiver(ar);
    }


    /*private ServiceConnection connection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            AlarmService.LocalBinder binder = (AlarmService.LocalBinder) service;
            mService = binder.getService();
            mBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            mBound = false;
        }
    };*/
}
