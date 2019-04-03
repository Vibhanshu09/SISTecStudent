package com.sistec.helperClasses;

public class AssignmentMarks {

    private int assign_no;
    private String topic;
    private int max_marks;
    private String given_date;
    private String s_status;
    private String submitted_on;
    private double marks_obtained;

    public AssignmentMarks() {
    }

    public AssignmentMarks(int assign_no, String topic, int max_marks, String given_date, String s_status, String submitted_on, double marks_obtained) {
        this.assign_no = assign_no;
        this.topic = topic;
        this.max_marks = max_marks;
        this.given_date = given_date;
        this.s_status = s_status;
        this.submitted_on = submitted_on;
        this.marks_obtained = marks_obtained;
    }

    public int getAssign_no() {
        return assign_no;
    }

    public void setAssign_no(int assign_no) {
        this.assign_no = assign_no;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public int getMax_marks() {
        return max_marks;
    }

    public void setMax_marks(int max_marks) {
        this.max_marks = max_marks;
    }

    public String getGiven_date() {
        return given_date;
    }

    public void setGiven_date(String given_date) {
        this.given_date = given_date;
    }

    public String getS_status() {
        return s_status;
    }

    public void setS_status(String s_status) {
        this.s_status = s_status;
    }

    public String getSubmitted_on() {
        return submitted_on;
    }

    public void setSubmitted_on(String submitted_on) {
        this.submitted_on = submitted_on;
    }

    public double getMarks_obtained() {
        return marks_obtained;
    }

    public void setMarks_obtained(double marks_obtained) {
        this.marks_obtained = marks_obtained;
    }
}
