package com.sistec.sistecstudents;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sistec.helperClasses.StudentAttendance;

import java.util.List;

public class AttendanceAdapter extends RecyclerView.Adapter<AttendanceAdapter.AttendanceHolder> {
    private Context mCtx;
    private List<StudentAttendance> studentAttendances;

    public AttendanceAdapter(Context mCtx, List<StudentAttendance> studentAttendances) {
        this.mCtx = mCtx;
        this.studentAttendances = studentAttendances;
    }

    @NonNull
    @Override
    public AttendanceHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(mCtx).inflate(R.layout.attendance_card_view, parent, false);
        AttendanceHolder holder = new AttendanceHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull AttendanceHolder holder, int i) {
        final StudentAttendance studentAttendance = studentAttendances.get(i);
        holder.subjectCodeTV.setText(studentAttendance.getSubjectCode());
        holder.subjectNameTV.setText(studentAttendance.getSubjectName());
        int present, total;
        float percent;
        present = studentAttendance.getAttendedClasses();
        total = studentAttendance.getTotalClasses();
        if (total > 0)
            percent = (present / (float) total) * 100;
        else
            percent = 0.0f;
        holder.presentTV.setText("" + present);
        holder.classesTV.setText("" + total);
        if (percent < 50.0f)
            holder.percentTV.setTextColor(Color.RED);
        else if (percent > 80.0f)
            holder.percentTV.setTextColor(Color.GREEN);
        holder.percentTV.setText(String.format("%.2f", percent) + "%");
    }

    @Override
    public int getItemCount() {
        return studentAttendances.size();
    }

    class AttendanceHolder extends RecyclerView.ViewHolder {
        TextView subjectCodeTV, subjectNameTV, classesTV, presentTV, percentTV;

        public AttendanceHolder(@NonNull View itemView) {
            super(itemView);
            subjectCodeTV = itemView.findViewById(R.id.subject_code_TV);
            subjectNameTV = itemView.findViewById(R.id.subject_name_TV);
            classesTV = itemView.findViewById(R.id.classes_TV);
            presentTV = itemView.findViewById(R.id.present_TV);
            percentTV = itemView.findViewById(R.id.percent_TV);

        }
    }

}
