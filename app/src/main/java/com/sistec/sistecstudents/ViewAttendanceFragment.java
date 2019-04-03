package com.sistec.sistecstudents;


import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.sistec.helperClasses.MyHelperClass;
import com.sistec.helperClasses.RemoteServiceUrl;
import com.sistec.helperClasses.StudentAttendance;
import com.sistec.helperClasses.VolleySingleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class ViewAttendanceFragment extends Fragment {

    private static String ATTENDANCE_URL = RemoteServiceUrl.SERVER_URL + RemoteServiceUrl.METHOD_NAME.ATTENDANCE;
    RecyclerView recyclerViewForAttendance;
    TextView totalClassesTV, totalPresetTV, totalPercentTV;
    //Adapter
    AttendanceAdapter attendanceAdapter;
    List<StudentAttendance> studentAttendanceList;
    private Map<String, String> userStatus;
    private String e_no, is_login;
    private HomeActivity homeActivity;

    public ViewAttendanceFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_view_attendance, container, false);
        getActivity().setTitle(R.string.title_view_attendance);
        homeActivity = (HomeActivity) getActivity();
        userStatus = homeActivity.getUserStatus();
        e_no = userStatus.get("e_no");
        is_login = userStatus.get("is_login");
        findAllIds(rootView);
        getStudentAttendance();
        return rootView;
    }

    private void findAllIds(View view) {
        recyclerViewForAttendance = view.findViewById(R.id.recyclerViewForAttendance);
        recyclerViewForAttendance.setLayoutManager(new LinearLayoutManager(homeActivity));
        studentAttendanceList = new ArrayList<>();
        attendanceAdapter = new AttendanceAdapter(homeActivity, studentAttendanceList);
        totalClassesTV = view.findViewById(R.id.total_classes_TV);
        totalPresetTV = view.findViewById(R.id.total_present_TV);
        totalPercentTV = view.findViewById(R.id.total_percent_TV);

    }

    private void getStudentAttendance() {
        MyHelperClass.showProgress(getActivity(), "Please Wait", "We are loading your attendance");
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                ATTENDANCE_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject root = new JSONObject(response);
                            String success = root.getString("success");
                            if (success.equals("1")) {
                                JSONObject attendanceJsonObject = root.getJSONObject("attendance");
                                setAttendance(attendanceJsonObject);
                                MyHelperClass.hideProgress();
                            } else {
                                MyHelperClass.hideProgress();
                                MyHelperClass.showAlerter(homeActivity, "Error", root.getString("message"), R.drawable.ic_error_red_24dp);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i("Error Responce", error.toString());
                        MyHelperClass.hideProgress();
                        MyHelperClass.showAlerter(homeActivity, "Error", "Response Error", R.drawable.ic_error_red_24dp);
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("e_no", e_no);
                params.put("is_login", is_login);
                return params;
            }
        };
        VolleySingleton.getInstance(getContext()).addToRequestQueue(stringRequest);
    }

    private void setAttendance(JSONObject attendance) {
        try {
            int subjectCount = attendance.getInt("subject_count");
            studentAttendanceList.clear();
            for (int i = 0; i < subjectCount; i++) {
                JSONObject subjectAttendanceJSONObject = attendance.getJSONObject("" + i);
                StudentAttendance studentAttendance = new StudentAttendance();
                studentAttendance.setSubjectCode(subjectAttendanceJSONObject.getString("sub_code"));
                studentAttendance.setSubjectName(subjectAttendanceJSONObject.getString("sub_name"));
                studentAttendance.setTotalClasses(Integer.parseInt(subjectAttendanceJSONObject.getString("total_lecture_taken")));
                studentAttendance.setAttendedClasses(Integer.parseInt(subjectAttendanceJSONObject.getString("total_lecture_attended")));
                studentAttendanceList.add(studentAttendance);
                attendanceAdapter.notifyDataSetChanged();
            }
            recyclerViewForAttendance.setAdapter(attendanceAdapter);
            int totalLecture = attendance.getInt("total_lecture");
            int totalAttendance = attendance.getInt("total_attendance");
            float totalPercent;
            if (totalLecture > 0)
                totalPercent = (totalAttendance / (float) totalLecture) * 100;
            else
                totalPercent = 0.0f;
            totalClassesTV.setText("" + totalLecture);
            totalPresetTV.setText("" + totalAttendance);
            totalPercentTV.setText(String.format("%.2f", totalPercent) + "%");
            if (totalPercent < 50.0f)
                totalPercentTV.setTextColor(Color.RED);
            else if (totalPercent > 80.0f)
                totalPercentTV.setTextColor(Color.GREEN);


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
