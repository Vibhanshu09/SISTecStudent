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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.sistec.helperClasses.AssignmentMarks;
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
public class AssignmentMarkFragment extends Fragment {

    private static String SUBJECTS_URL = RemoteServiceUrl.SERVER_URL + RemoteServiceUrl.METHOD_NAME.FETCH_SUBJECTS;
    private static String ASSIGNMENTS_URL = RemoteServiceUrl.SERVER_URL + RemoteServiceUrl.METHOD_NAME.ASSIGNMENTS;
    List<String> subjectList, subjectCode;
    int selectedSubjectPosition = -1;
    //Adapter
    AssignmentAdapter assignmentAdapter;
    List<AssignmentMarks> assignmentMarksList;
    private Map<String, String> userStatus;
    private String e_no, is_login;
    private HomeActivity homeActivity;
    private Spinner assignmentSubjectSpinner;
    private Button findAssignmentButton;
    private LinearLayout assignmentRVHolder;
    private RecyclerView assignmentRecyclerView;

    public AssignmentMarkFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_assignment_mark, container, false);
        getActivity().setTitle(R.string.title_assignment_marks);
        homeActivity = (HomeActivity) getActivity();
        userStatus = homeActivity.getUserStatus();
        e_no = userStatus.get("e_no");
        is_login = userStatus.get("is_login");
        findAllIds(rootView);
        getSubjectList();
        return rootView;
    }

    private void findAllIds(View view) {
        assignmentSubjectSpinner = view.findViewById(R.id.assignment_sub_spinner);
        findAssignmentButton = view.findViewById(R.id.find_assignments);
        assignmentRVHolder = view.findViewById(R.id.assignment_RV_holder);
        assignmentRecyclerView = view.findViewById(R.id.assignment_recyclerView);
        assignmentRecyclerView.setLayoutManager(new LinearLayoutManager(homeActivity));
        MyHelperClass.setSpinnerHeight(assignmentSubjectSpinner);
        assignmentSubjectSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //Toast.makeText(homeActivity, subjectCode.get(position), Toast.LENGTH_SHORT).show();
                if (position != selectedSubjectPosition) {
                    selectedSubjectPosition = position;
                    findAssignmentButton.setVisibility(View.VISIBLE);
                    assignmentRVHolder.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

        });

        findAssignmentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findAssignmentButton.setVisibility(View.GONE);
                getAssignmentMarks();
            }
        });

        assignmentMarksList = new ArrayList<>();
        assignmentAdapter = new AssignmentAdapter(homeActivity, assignmentMarksList);
    }

    private void getSubjectList() {
        MyHelperClass.showProgress(getActivity(), "Please Wait", "We are loading Subject List");
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                SUBJECTS_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject root = new JSONObject(response);
                            String success = root.getString("success");
                            if (success.equals("1")) {
                                JSONArray subjectListArray = root.getJSONArray("sub_rec");
                                JSONObject subjectCount = root.getJSONObject("subjects");
                                setSubjectsInSpinner(subjectListArray, subjectCount.getInt("sub_count"));
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
                        MyHelperClass.showAlerter(homeActivity, "Error", error.getMessage(), R.drawable.ic_error_red_24dp);

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

    private void setSubjectsInSpinner(JSONArray subjectsArray, int sub_count) {
        JSONObject tempSubJSONObject;
        subjectList = new ArrayList<String>();
        subjectCode = new ArrayList<String>();
        try {
            for (int i = 0; i < sub_count; i++) {
                tempSubJSONObject = subjectsArray.getJSONObject(i);
                subjectList.add(tempSubJSONObject.getString("sub_name"));
                subjectCode.add(tempSubJSONObject.getString("sub_code"));
            }
            ArrayAdapter<String> subListAdapter = new ArrayAdapter<String>(homeActivity, android.R.layout.simple_spinner_dropdown_item, subjectList);
            assignmentSubjectSpinner.setAdapter(subListAdapter);
        } catch (JSONException e) {
            Log.e("JSONErroe", e.toString());
        }
    }

    private void getAssignmentMarks() {
        MyHelperClass.showProgress(getActivity(), "Please Wait", "We are loading Assignments");
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                ASSIGNMENTS_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject root = new JSONObject(response);
                            String success = root.getString("success");
                            if (success.equals("1")) {
                                JSONArray assignmentRecListArray = root.getJSONArray("assignment_rec");
                                JSONObject assignmentCount = root.getJSONObject("assignments");
                                assignmentRVHolder.setVisibility(View.VISIBLE);
                                setAssignmentsInRecyclerView(assignmentRecListArray, assignmentCount.getInt("assignment_count"));
                                MyHelperClass.hideProgress();
                            } else {
                                findAssignmentButton.setVisibility(View.VISIBLE);
                                assignmentRVHolder.setVisibility(View.GONE);
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
                params.put("sub_code", subjectCode.get(selectedSubjectPosition));
                params.put("is_login", is_login);
                return params;
            }
        };
        VolleySingleton.getInstance(getContext()).addToRequestQueue(stringRequest);
    }

    private void setAssignmentsInRecyclerView(JSONArray assignmentsJSONArray, int assignmentCount) {
        try {
            assignmentMarksList.clear();
            for (int i = 0; i < assignmentCount; i++) {
                JSONObject tempJSONObject = assignmentsJSONArray.getJSONObject(i);
                AssignmentMarks assignmentMarks = new AssignmentMarks();
                assignmentMarks.setAssign_no(tempJSONObject.getInt("assign_no"));
                assignmentMarks.setTopic(tempJSONObject.getString("topic"));
                assignmentMarks.setMax_marks(tempJSONObject.getInt("max_marks"));
                assignmentMarks.setGiven_date(tempJSONObject.getString("given_date"));
                assignmentMarks.setS_status(tempJSONObject.getString("s_status"));
                if (tempJSONObject.getString("s_status").equals("SUBMITTED")) {
                    assignmentMarks.setSubmitted_on(tempJSONObject.getString("submitted_on"));
                    assignmentMarks.setMarks_obtained(tempJSONObject.getDouble("marks_obtained"));
                }
                assignmentMarksList.add(assignmentMarks);
                assignmentAdapter.notifyDataSetChanged();
            }
            assignmentRecyclerView.setAdapter(assignmentAdapter);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
