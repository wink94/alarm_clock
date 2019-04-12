package com.windula.alam_clock10;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    //private ListView alarmList;

    private AlarmDbHelper dbHelper;

    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setActionBar();

        //alarmList=findViewById(R.id.alarm_list_view);
        recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        recyclerView.setHasFixedSize(true);
        // use a linear layout manager
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        dbHelper=new AlarmDbHelper(getApplicationContext());
        // specify an adapter (see also next example)
        mAdapter = new AlarmAdapter(updateAlarmList());
        recyclerView.setAdapter(mAdapter);

        dbHelper=new AlarmDbHelper(getApplicationContext());
    }

    private void setActionBar(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
        //toolbar.setTitle(R.string.app_title);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_items, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_alarm:
                Intent popup = new Intent(getApplicationContext(), popupActivity.class);
                startActivity(popup);
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    private List<AlarmView> updateAlarmList(){

        Alarm getAlarms=new Alarm();

        Cursor alarmCursor=getAlarms.getAllAlarms(dbHelper);
        ArrayList<AlarmView> alarmViewList = new ArrayList<>();

        if(alarmCursor.getColumnCount()>0){
            if (alarmCursor.moveToFirst()) {
                while (!alarmCursor.isAfterLast()) {
                    String time = alarmCursor.getString(alarmCursor.getColumnIndex("time"));
                    String title  =  alarmCursor.getString(alarmCursor.getColumnIndex("title"));

                    alarmViewList.add(new AlarmView(time,title));
                    alarmCursor.moveToNext();
                }

                return alarmViewList;
            }
        }

        return null;
    }



}
