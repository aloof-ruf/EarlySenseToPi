package com.example.sethruf.earlysensetopi_v01.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Seth.Ruf on 24/04/2015.
 */
public class EarlySenseDbHelper extends SQLiteOpenHelper{

    private final String TABLE_NAME = "early_sense_to_pi_v0";

    private final String COLUMN_ID = "id";
    private final String COLUMN_TIMESTAMP = "date_timestamp";
    private final String COLUMN_HEART_RATE = "heart_rate";
    private final String COLUMN_RESPIRATORY_RATE = "respiratory_rate";
    private final String COLUMN_MOVEMENT_LEVEL = "movement_level";
    private final String COLUMN_IN_BED = "in_bed";

    private final String CREATE_TABLE = "create table " + TABLE_NAME + " (" +
            COLUMN_ID + " integer primary key autoincrement, " +
            COLUMN_TIMESTAMP + " integer not null, " +
            COLUMN_HEART_RATE + " integer not null, " +
            COLUMN_RESPIRATORY_RATE + " integer not null, " +
            COLUMN_MOVEMENT_LEVEL + " integer not null, " +
            COLUMN_IN_BED + " integer not null, " +
            " UNIQUE (" + COLUMN_TIMESTAMP + ") ON CONFLICT REPLACE );";

    public EarlySenseDbHelper(Context context) {
        super(context, "early_sense_to_pi.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public String getTableName() {
        return TABLE_NAME;
    }

    public String getColumnId() {
        return COLUMN_ID;
    }

    public String getColumnTimestamp() {
        return COLUMN_TIMESTAMP;
    }

    public String getColumnHeartRate() {
        return COLUMN_HEART_RATE;
    }

    public String getColumnRespiratoryRate() {
        return COLUMN_RESPIRATORY_RATE;
    }

    public String getColumnMovementLevel() {
        return COLUMN_MOVEMENT_LEVEL;
    }

    public String getColumnInBed() {
        return COLUMN_IN_BED;
    }
}
