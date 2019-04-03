package com.sistec.helperClasses;

public class MidSemMarks {
    private String sub_code;
    private String sub_name;
    private String exam_date;
    private int marks;
    private String attended;
    private double marks_obtained;

    public MidSemMarks() {
    }

    public MidSemMarks(String sub_code, String sub_name, String exam_date, int marks, String attended, double marks_obtained) {
        this.sub_code = sub_code;
        this.sub_name = sub_name;
        this.exam_date = exam_date;
        this.marks = marks;
        this.attended = attended;
        this.marks_obtained = marks_obtained;
    }

    public String getSub_code() {
        return sub_code;
    }

    public void setSub_code(String sub_code) {
        this.sub_code = sub_code;
    }

    public String getSub_name() {
        return sub_name;
    }

    public void setSub_name(String sub_name) {
        this.sub_name = sub_name;
    }

    public String getExam_date() {
        return exam_date;
    }

    public void setExam_date(String exam_date) {
        this.exam_date = exam_date;
    }

    public int getMarks() {
        return marks;
    }

    public void setMarks(int marks) {
        this.marks = marks;
    }

    public String getAttended() {
        return attended;
    }

    public void setAttended(String attended) {
        this.attended = attended;
    }

    public double getMarks_obtained() {
        return marks_obtained;
    }

    public void setMarks_obtained(double marks_obtained) {
        this.marks_obtained = marks_obtained;
    }
}
