package com.windula.alam_clock10;

import java.util.ArrayList;

public class AlarmView {
    private String time;
    private String title;

    public AlarmView(String time, String title) {
        this.time = time;
        this.title = title;
    }

    public String getTime() {
        return time;
    }

    public String getTitle() {
        return title;
    }

    public  ArrayList<AlarmView> createContactsList(int numContacts) {
        ArrayList<AlarmView> alarmlist = new ArrayList<AlarmView>();

        for (int i = 1; i <= numContacts; i++) {
            alarmlist.add(new AlarmView(time, title));
        }

        return alarmlist;
    }

}
