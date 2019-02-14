package com.sistec.sistecstudents;

import android.app.Activity;
import android.widget.ArrayAdapter;

public class SemResultListAdapter extends ArrayAdapter<String> {

    private final String[] sem;
    private final String[] year;
    private final String[] cgpa;
    private final String[] status;


    public SemResultListAdapter(Activity context, String[] sem, String[] year, String[] cgpa, String[] status) {
        super(context, R.layout.sem_result_list_view);

        this.sem = sem;
        this.year = year;
        this.cgpa = cgpa;
        this.status = status;
    }

}
