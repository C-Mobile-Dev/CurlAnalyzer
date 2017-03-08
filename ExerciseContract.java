package com.kgt.kyle.perfectbicepcurl;

import android.provider.BaseColumns;

/**
 * Created by Kyle on 19/12/2016.
 *
 * Contract for the database tables
 */

public final class ExerciseContract {

    private ExerciseContract(){
        //empty
    }

    public static class ExersiseEntries implements BaseColumns {
        public static final String TABLE_NAME = "exercise_data";
        public static final String REPS_DONE = "reps_done";
        public static final String WEIGHT_LIFTED = "weight_lifted";
        public static final String CURL_FORM = "form_data";
        public static final String PACE_FORM = "pace_form";
    }
}
