package com.kgt.kyle.perfectbicepcurl;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import static android.graphics.Color.rgb;

/**
 * Created by Kyle on 12/12/2016.
 */

public class CheckActivity extends AppCompatActivity {

    private String currentSort = ExerciseContract.ExersiseEntries._ID;
    private int selected = -1;
    private boolean selectedFlag = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.check_activity);

        sortEntries(ExerciseContract.ExersiseEntries._ID);
    }

    public void sortEntries(String count){
        ExerciseDataDbHelper dbHelper = new ExerciseDataDbHelper(getApplicationContext());
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        displayExerciseData(getAllEntries(db,count + " ASC"));
        currentSort = count;
    }

    public Cursor getAllEntries(SQLiteDatabase db, String order){

        String[] proj = {
                ExerciseContract.ExersiseEntries._ID,
                ExerciseContract.ExersiseEntries.REPS_DONE,
                ExerciseContract.ExersiseEntries.WEIGHT_LIFTED,
                ExerciseContract.ExersiseEntries.CURL_FORM,
                ExerciseContract.ExersiseEntries.PACE_FORM
        };

        return db.query(
                ExerciseContract.ExersiseEntries.TABLE_NAME,
                proj,
                null,
                null,
                null,
                null,
                order
        );
    }

    public void displayTableHeader(TableLayout tl){

        TableRow headerRow = new TableRow(this);

        TextView repsHeader = new TextView(this);
        repsHeader.setText("Reps");
        repsHeader.setTypeface(repsHeader.getTypeface(), Typeface.BOLD);
        repsHeader.setTextSize(20);
        repsHeader.setPadding(5,5,5,5);
        repsHeader.setGravity(Gravity.CENTER);
        repsHeader.setTextColor(Color.WHITE);
        repsHeader.setBackgroundColor(rgb(23, 55, 107));

        TextView weightHeader = new TextView(this);
        weightHeader.setText("Weight");
        weightHeader.setTypeface(weightHeader.getTypeface(), Typeface.BOLD);
        weightHeader.setTextSize(20);
        weightHeader.setPadding(5,5,5,5);
        weightHeader.setGravity(Gravity.CENTER);
        weightHeader.setTextColor(Color.WHITE);
        weightHeader.setBackgroundColor(rgb(23, 55, 107));

        TextView curlHeader = new TextView(this);
        curlHeader.setText("Good Form");
        curlHeader.setTypeface(curlHeader.getTypeface(), Typeface.BOLD);
        curlHeader.setTextSize(20);
        curlHeader.setPadding(5,5,5,5);
        curlHeader.setGravity(Gravity.CENTER);
        curlHeader.setTextColor(Color.WHITE);
        curlHeader.setBackgroundColor(rgb(23, 55, 107));

        TextView paceHeader = new TextView(this);
        paceHeader.setText("Good Pace");
        paceHeader.setTypeface(paceHeader.getTypeface(), Typeface.BOLD);
        paceHeader.setTextSize(20);
        paceHeader.setPadding(5,5,5,5);
        paceHeader.setGravity(Gravity.CENTER);
        paceHeader.setTextColor(Color.WHITE);
        paceHeader.setBackgroundColor(rgb(23, 55, 107));

       // headerRow.addView(dateHeader);
        headerRow.addView(repsHeader);
        headerRow.addView(weightHeader);
        headerRow.addView(curlHeader);
        headerRow.addView(paceHeader);

        tl.addView(headerRow);
    }

    public void displayExerciseData(final Cursor cursor){
        final TableLayout tableLayout = (TableLayout) findViewById(R.id.exercisedata);
        tableLayout.removeAllViews();

        displayTableHeader(tableLayout);
        for(boolean isData = cursor.moveToFirst();isData; isData = cursor.moveToNext()) {

            final TableRow tr = new TableRow(this);

            final int itemID = cursor.getInt(cursor.getColumnIndexOrThrow(ExerciseContract.ExersiseEntries._ID));

            int reps = cursor.getInt(cursor.getColumnIndexOrThrow(ExerciseContract.ExersiseEntries.REPS_DONE));
            TextView repView = new TextView(this);
            repView.setGravity(Gravity.CENTER);
            repView.setTextSize(18);
            repView.setText(String.valueOf(reps));
            tr.addView(repView);

            int weight = cursor.getInt(cursor.getColumnIndexOrThrow(ExerciseContract.ExersiseEntries.WEIGHT_LIFTED));
            TextView weightView = new TextView(this);
            weightView.setGravity(Gravity.CENTER);
            weightView.setTextSize(18);
            weightView.setText(String.valueOf(weight) + "Kg");
            tr.addView(weightView);

            double curl = cursor.getDouble(cursor.getColumnIndexOrThrow(ExerciseContract.ExersiseEntries.CURL_FORM));
            TextView curlView = new TextView(this);
            curlView.setGravity(Gravity.CENTER);
            curlView.setTextSize(18);
            curlView.setText(String.valueOf(curl)+ "%");
            tr.addView(curlView);

            double pace = cursor.getDouble(cursor.getColumnIndexOrThrow(ExerciseContract.ExersiseEntries.PACE_FORM));
            TextView paceView = new TextView(this);
            paceView.setGravity(Gravity.CENTER);
            paceView.setTextSize(18);
            paceView.setText(String.valueOf(pace) + "%");
            tr.addView(paceView);

            tr.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(selected != itemID){
                        tr.setBackgroundColor(rgb(213,224,232));
                        selected = itemID;
                    }else{
                        tr.setBackgroundColor(rgb(238,238,238));
                    }
                }
            });

            tableLayout.addView(tr);
        }
    }
}
