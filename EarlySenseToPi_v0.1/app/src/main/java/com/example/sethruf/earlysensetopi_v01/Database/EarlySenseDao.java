package com.example.sethruf.earlysensetopi_v01.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.sethruf.earlysensetopi_v01.EarlySenseReading;

/**
 * Created by Seth.Ruf on 24/04/2015.
 */
public class EarlySenseDao {

    private SQLiteDatabase database;
    private EarlySenseDbHelper helper;

    public EarlySenseDao(Context context){
        helper = new EarlySenseDbHelper(context);
        database = helper.getWritableDatabase();
    }

    public void insert(EarlySenseReading reading){
        if (database != null && database.isOpen()) {
            ContentValues values = new ContentValues();
            values.put(helper.getColumnTimestamp(), reading.getTimestamp());
            values.put(helper.getColumnHeartRate(), reading.getHeartRate());
            values.put(helper.getColumnMovementLevel(), reading.getMovementLevel());
            values.put(helper.getColumnRespiratoryRate(), reading.getRespiratoryRate());
            values.put(helper.getColumnInBed(), reading.isInBed());
            long insertRow = database.insert(helper.getTableName(), null, values);
        }
    }

    public EarlySenseReading get(long index){
        EarlySenseReading reading = null;
        if (database != null && database.isOpen()){
            Cursor resultsCursor = database.query(helper.getTableName(),
                    new String[]{helper.getColumnTimestamp(),
                            helper.getColumnHeartRate(),
                            helper.getColumnRespiratoryRate(),
                            helper.getColumnMovementLevel(),
                            helper.getColumnInBed()},
                    helper.getColumnId() + " = " + index,
                    null,
                    null,
                    null,
                    null
            );
            reading = new EarlySenseReading(resultsCursor.getInt(0),
                    resultsCursor.getInt(1),
                    resultsCursor.getInt(2),
                    resultsCursor.getInt(3),
                    resultsCursor.getInt(4) != 0);
        }
        return reading;
    }

    public EarlySenseReading[] getAll(){
        EarlySenseReading[] readings = null;
        if (database != null && database.isOpen()){
            Cursor resultsCursor = database.query(helper.getTableName(),
                    new String[]{helper.getColumnTimestamp(),
                            helper.getColumnHeartRate(),
                            helper.getColumnRespiratoryRate(),
                            helper.getColumnMovementLevel(),
                            helper.getColumnInBed()},
                    null,
                    null,
                    null,
                    null,
                    null
            );
            readings = new EarlySenseReading[resultsCursor.getCount()];
            resultsCursor.moveToFirst();
            for (int i = 0; i < readings.length; i++){
                EarlySenseReading reading = new EarlySenseReading(resultsCursor.getInt(0),
                        resultsCursor.getInt(1),
                        resultsCursor.getInt(2),
                        resultsCursor.getInt(3),
                        resultsCursor.getInt(4) != 0);
                readings[i] = reading;
                resultsCursor.moveToNext();
            }
        }
        return readings;
    }

}
