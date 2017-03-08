package com.kgt.kyle.perfectbicepcurl;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Kyle on 19/12/2016.
 *
 * The database helper methods
 */

public class ExerciseDataDbHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "exercisetracker.db";

    public ExerciseDataDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(SQL_CREATE_EXERCISE_ENTRIES);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_EXERCISE_ENTRIES);

        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    //create table
    private static final String SQL_CREATE_EXERCISE_ENTRIES = "CREATE TABLE " + ExerciseContract.ExersiseEntries.TABLE_NAME +
            " ( " + ExerciseContract.ExersiseEntries._ID  + " INTEGER PRIMARY KEY, " + ExerciseContract.ExersiseEntries.REPS_DONE +
            " INTEGER, " + ExerciseContract.ExersiseEntries.WEIGHT_LIFTED + " INTEGER, " + ExerciseContract.ExersiseEntries.CURL_FORM + " REAL," +
            ExerciseContract.ExersiseEntries.PACE_FORM + " REAL);";
    //delete table
    private static final String SQL_DELETE_EXERCISE_ENTRIES = "DROP TABLE IF EXISTS " + ExerciseContract.ExersiseEntries.TABLE_NAME + ";";

    //Method to insert entries to table
    public long insertExerciseEntery(SQLiteDatabase db, int reps, int weight, double curlForm, double paceForm){

        ContentValues val = new ContentValues();
        val.put(ExerciseContract.ExersiseEntries.REPS_DONE,reps);
        val.put(ExerciseContract.ExersiseEntries.WEIGHT_LIFTED,weight);
        val.put(ExerciseContract.ExersiseEntries.CURL_FORM,curlForm);
        val.put(ExerciseContract.ExersiseEntries.PACE_FORM,paceForm);

        return db.insert(ExerciseContract.ExersiseEntries.TABLE_NAME,null,val);
    }
}
