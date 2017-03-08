package com.kgt.kyle.perfectbicepcurl;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

/**
 * Created by Kyle on 12/12/2016.
 */

public class IndexActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.index_activity);

        //set buttons programmatically
        Button checkActivity = (Button)findViewById(R.id.checkActivity);

        Button startExercise = (Button)findViewById(R.id.startExercise);

        checkActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent checkActInt = new Intent(IndexActivity.this,CheckActivity.class);
                startActivityForResult(checkActInt,0);
            }
        });

        startExercise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent startExInt = new Intent(IndexActivity.this,Exercise.class);
                startActivityForResult(startExInt,0);
            }
        });
    }

    //NOT USED- would be for setting he height of button evenly
    public int getDim() {

        DisplayMetrics displayMetrics = new DisplayMetrics();
        WindowManager windowMgr = (WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        windowMgr.getDefaultDisplay().getMetrics(displayMetrics);
        int deviceHeight = displayMetrics.heightPixels;

        return deviceHeight;
    }
}
