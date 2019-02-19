package com.sistec.sistecstudents;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class AcadmicRecordFragment extends Fragment {


    String[] sem = {"1st", "2nd", "3rd", "4th", "5th", "6th", "7th", "8th"};
    String[] year = {"2015", "2016", "2016", "2017", "2017", "2018", "2018", "2019"};
    String[] cgpa = {"9.08", "9.46", "9.18", "9.07", "8.91", "8.67", "7.13", "8.00"};
    String status = "ALL CLEAR";
    public AcadmicRecordFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_acadmic_record, container, false);
        getActivity().setTitle(R.string.title_academic);

        TableLayout tableLayout = rootView.findViewById(R.id.sem_result_table_layout);
        for (int i = 0; i < 8; i++) {
            LayoutInflater inflater1 = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View rowLayout = inflater1.inflate(R.layout.sem_result_list_view, null);
            TextView semTextView = rowLayout.findViewById(R.id.list_view_sem);
            TextView yearTextView = rowLayout.findViewById(R.id.list_view_yr);
            TextView cgpaTextView = rowLayout.findViewById(R.id.list_view_cgpa);
            TextView statusTextView = rowLayout.findViewById(R.id.list_view_status);
            semTextView.setText(sem[i]);
            yearTextView.setText(year[i]);
            cgpaTextView.setText(cgpa[i]);
            statusTextView.setText(status);
            tableLayout.addView(rowLayout, i);
        }


        return rootView;

    }

}
