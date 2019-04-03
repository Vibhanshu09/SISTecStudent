package com.sistec.sistecstudents;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sistec.helperClasses.AssignmentMarks;

import java.util.List;

public class AssignmentAdapter extends RecyclerView.Adapter<AssignmentAdapter.AssignmentHolder> {
    private Context mCtx;
    private List<AssignmentMarks> assignmentMarks;

    public AssignmentAdapter(Context mCtx, List<AssignmentMarks> assignmentMarks) {
        this.mCtx = mCtx;
        this.assignmentMarks = assignmentMarks;
    }

    @NonNull
    @Override
    public AssignmentHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mCtx).inflate(R.layout.assignment_card_view, viewGroup, false);
        AssignmentHolder holder = new AssignmentHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull AssignmentHolder assignmentHolder, int i) {
        final AssignmentMarks assignmentMarks = this.assignmentMarks.get(i);
        assignmentHolder.assignmentNumTV.setText("" + assignmentMarks.getAssign_no());
        assignmentHolder.assignmentTopicTV.setText(assignmentMarks.getTopic());
        assignmentHolder.assignmentGivenOnTV.setText(assignmentMarks.getGiven_date());
        assignmentHolder.maxMarksTV.setText("" + assignmentMarks.getMax_marks());
        if (assignmentMarks.getS_status().equals("SUBMITTED")) {
            assignmentHolder.assignmentSubmittedOnTV.setText(assignmentMarks.getSubmitted_on());
            assignmentHolder.marksObtainedTV.setText(String.format("%.2f", assignmentMarks.getMarks_obtained()));
        } else {
            assignmentHolder.assignmentSubmittedOnTV.setTextColor(Color.RED);
            assignmentHolder.marksObtainedTV.setTextColor(Color.RED);
            assignmentHolder.assignmentSubmittedOnTV.setText(assignmentMarks.getS_status());
            assignmentHolder.marksObtainedTV.setText("0");
        }
    }

    @Override
    public int getItemCount() {
        return assignmentMarks.size();
    }

    class AssignmentHolder extends RecyclerView.ViewHolder {
        TextView assignmentNumTV, assignmentTopicTV, assignmentGivenOnTV, assignmentSubmittedOnTV, maxMarksTV, marksObtainedTV;

        public AssignmentHolder(@NonNull View itemView) {
            super(itemView);
            assignmentNumTV = itemView.findViewById(R.id.assignment_num_TV);
            assignmentTopicTV = itemView.findViewById(R.id.assignment_topic_TV);
            assignmentGivenOnTV = itemView.findViewById(R.id.assignment_given_on_TV);
            assignmentSubmittedOnTV = itemView.findViewById(R.id.assignment_submitted_on_TV);
            maxMarksTV = itemView.findViewById(R.id.assignment_max_marks_TV);
            marksObtainedTV = itemView.findViewById(R.id.assignment_marks_obtained_TV);

        }
    }


}
