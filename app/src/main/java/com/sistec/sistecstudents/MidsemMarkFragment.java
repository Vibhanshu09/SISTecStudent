package com.sistec.sistecstudents;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.sistec.helperClasses.MidSemMarks;
import com.sistec.helperClasses.MyHelperClass;
import com.sistec.helperClasses.RemoteServiceUrl;
import com.sistec.helperClasses.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class MidsemMarkFragment extends Fragment {

    private static String MIDSEM_MARKS_URL = RemoteServiceUrl.SERVER_URL + RemoteServiceUrl.METHOD_NAME.MIDSEM_MARKS;
    Spinner midSemSemesterSpinner, midSemExamTypeSpinner;
    Button findExamResultButton;
    LinearLayout midSemRVHolder;
    RecyclerView midSemRecyclerView;
    int selectedSemPosition = -1, selectedExamTypePosition = -1;
    String selectedSemesterString, selectedExamTypeString;
    //Adapter
    MidSemAdapter midSemAdapter;
    List<MidSemMarks> midSemMarksList;
    private Map<String, String> userStatus;
    private String e_no, is_login;
    private HomeActivity homeActivity;

    public MidsemMarkFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_midsem_mark, container, false);
        getActivity().setTitle(R.string.title_midsem_marks);
        homeActivity = (HomeActivity) getActivity();
        userStatus = homeActivity.getUserStatus();
        e_no = userStatus.get("e_no");
        is_login = userStatus.get("is_login");
        findAllIds(rootView);
        return rootView;
    }

    private void findAllIds(View view) {
        midSemSemesterSpinner = view.findViewById(R.id.midsem_semester_spinner);
        midSemExamTypeSpinner = view.findViewById(R.id.midsem_exam_type_spinner);
        findExamResultButton = view.findViewById(R.id.find_exam_result);
        midSemRVHolder = view.findViewById(R.id.midsem_RV_holder);
        midSemRecyclerView = view.findViewById(R.id.midsem_recyclerView);
        MyHelperClass.setSpinnerHeight(midSemSemesterSpinner);
        MyHelperClass.setSpinnerHeight(midSemExamTypeSpinner);

        midSemSemesterSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != selectedSemPosition) {
                    selectedSemPosition = position;
                    selectedSemesterString = parent.getItemAtPosition(position).toString();
                    findExamResultButton.setVisibility(View.VISIBLE);
                    midSemRVHolder.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        midSemExamTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != selectedExamTypePosition) {
                    selectedExamTypePosition = position;
                    selectedExamTypeString = parent.getItemAtPosition(position).toString();
                    findExamResultButton.setVisibility(View.VISIBLE);
                    midSemRVHolder.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        midSemRecyclerView.setLayoutManager(new LinearLayoutManager(homeActivity));

        findExamResultButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findExamResultButton.setVisibility(View.GONE);
                getMidSemMarks();
            }
        });

        midSemMarksList = new ArrayList<>();
        midSemAdapter = new MidSemAdapter(homeActivity, midSemMarksList);
    }

    private void getMidSemMarks() {
        MyHelperClass.showProgress(getActivity(), "Please Wait", "We are loading Exam Results");
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                MIDSEM_MARKS_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject root = new JSONObject(response);
                            String success = root.getString("success");
                            if (success.equals("1")) {
                                JSONArray marksRecListArray = root.getJSONArray("marks_rec");
                                JSONObject subjectCount = root.getJSONObject("marks");
                                midSemRVHolder.setVisibility(View.VISIBLE);
                                setMarksInRecyclerView(marksRecListArray, subjectCount.getInt("subject_count"));
                                MyHelperClass.hideProgress();
                            } else {
                                findExamResultButton.setVisibility(View.VISIBLE);
                                midSemRVHolder.setVisibility(View.GONE);
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
                        MyHelperClass.showAlerter(homeActivity, "Error", error.getMessage(), R.drawable.ic_error_red_24dp);

                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("e_no", e_no);
                params.put("exam_name", selectedExamTypeString);
                params.put("semester", selectedSemesterString);
                params.put("is_login", is_login);
                return params;
            }
        };
        VolleySingleton.getInstance(getContext()).addToRequestQueue(stringRequest);
    }

    private void setMarksInRecyclerView(JSONArray marksJSONArray, int subjectCount) {
        try {
            midSemMarksList.clear();
            for (int i = 0; i < subjectCount; i++) {
                JSONObject tempJSONObject = marksJSONArray.getJSONObject(i);
                MidSemMarks midSemMarks = new MidSemMarks();
                midSemMarks.setSub_code(tempJSONObject.getString("sub_code"));
                midSemMarks.setSub_name(tempJSONObject.getString("sub_name"));
                midSemMarks.setMarks(tempJSONObject.getInt("marks"));
                midSemMarks.setExam_date(tempJSONObject.getString("exam_date"));
                midSemMarks.setAttended(tempJSONObject.getString("attended"));
                if (tempJSONObject.getString("attended").equals("PRESENT")) {
                    midSemMarks.setMarks_obtained(tempJSONObject.getDouble("marks_obtained"));
                }
                midSemMarksList.add(midSemMarks);
                midSemAdapter.notifyDataSetChanged();
            }
            midSemRecyclerView.setAdapter(midSemAdapter);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
