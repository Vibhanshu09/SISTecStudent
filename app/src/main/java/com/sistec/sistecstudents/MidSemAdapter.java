package com.sistec.sistecstudents;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sistec.helperClasses.MidSemMarks;

import java.util.List;

public class MidSemAdapter extends RecyclerView.Adapter<MidSemAdapter.MidSemHolder> {

    private Context mCtx;
    private List<MidSemMarks> midSemMarks;

    public MidSemAdapter(Context mCtx, List<MidSemMarks> midSemMarks) {
        this.mCtx = mCtx;
        this.midSemMarks = midSemMarks;
    }

    @NonNull
    @Override
    public MidSemHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mCtx).inflate(R.layout.mid_sem_card_view, viewGroup, false);
        MidSemHolder midSemHolder = new MidSemHolder(view);
        return midSemHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MidSemHolder midSemHolder, int i) {
        final MidSemMarks midSemMarks = this.midSemMarks.get(i);
        midSemHolder.midSemSubjectCodeTV.setText(midSemMarks.getSub_code());
        midSemHolder.midSemSubjectNameTV.setText(midSemMarks.getSub_name());
        midSemHolder.midSemExamDateTV.setText(midSemMarks.getExam_date());
        midSemHolder.midSemAttendedTV.setText(midSemMarks.getAttended());
        midSemHolder.midSemMaxMarksTv.setText("" + midSemMarks.getMarks());
        if (midSemMarks.getAttended().equals("PRESENT")) {
            midSemHolder.midSemMarksObtainedTV.setText(String.format("%.2f", midSemMarks.getMarks_obtained()));
        } else {
            midSemHolder.midSemAttendedTV.setTextColor(Color.RED);
            midSemHolder.midSemMarksObtainedTV.setTextColor(Color.RED);
            midSemHolder.midSemMarksObtainedTV.setText("0");
        }
    }

    @Override
    public int getItemCount() {
        return midSemMarks.size();
    }

    class MidSemHolder extends RecyclerView.ViewHolder {
        TextView midSemSubjectCodeTV, midSemSubjectNameTV, midSemExamDateTV, midSemAttendedTV, midSemMaxMarksTv, midSemMarksObtainedTV;

        public MidSemHolder(@NonNull View itemView) {
            super(itemView);
            midSemSubjectCodeTV = itemView.findViewById(R.id.mid_sem_sub_code_TV);
            midSemSubjectNameTV = itemView.findViewById(R.id.mid_sem_sub_name_TV);
            midSemExamDateTV = itemView.findViewById(R.id.mis_sem_exam_date_TV);
            midSemAttendedTV = itemView.findViewById(R.id.mis_sem_attended_TV);
            midSemMaxMarksTv = itemView.findViewById(R.id.mid_sem_max_marks_TV);
            midSemMarksObtainedTV = itemView.findViewById(R.id.mid_sem_marks_obtained_TV);
        }
    }
}
