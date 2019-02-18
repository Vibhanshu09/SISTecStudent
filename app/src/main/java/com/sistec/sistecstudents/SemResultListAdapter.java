package com.sistec.sistecstudents;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class SemResultListAdapter extends ArrayAdapter<String> {

    private final Context context;
    private final String[] sem;
    private final String[] year;
    private final String[] cgpa;
    private final String status;


    public SemResultListAdapter(Context context, String[] sem, String[] year, String[] cgpa, String status) {
        super(context, R.layout.sem_result_list_view);

        this.context = context;
        this.sem = sem;
        this.year = year;
        this.cgpa = cgpa;
        this.status = status;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = convertView;
        if (row == null) {
            row = LayoutInflater.from(getContext()).inflate(R.layout.sem_result_list_view, parent, false);
        }

        TextView semTextView = row.findViewById(R.id.list_view_sem);
        TextView yrTextView = row.findViewById(R.id.list_view_yr);
        TextView cgpaTextView = row.findViewById(R.id.list_view_cgpa);
        TextView statusTextView = row.findViewById(R.id.list_view_status);

        semTextView.setText(sem[position]);
        yrTextView.setText(year[position]);
        cgpaTextView.setText(cgpa[position]);
        statusTextView.setText(status);

        return row;
    }
}
