package com.windula.alam_clock10;

import android.provider.BaseColumns;

public final class AlarmContract {

    private AlarmContract() {
    }

    public static class AlarmEntry implements BaseColumns{
        public static final String TABLE_NAME = "alarm_entry";
        public static final String COLUMN_TIME = "time";
        public static final String COLUMN_NAME_ALARM = "alarm";
        public static final String COLUMN_TITLE = "title";
    }



}
