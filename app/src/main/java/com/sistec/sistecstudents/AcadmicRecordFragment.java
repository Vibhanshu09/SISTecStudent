package com.sistec.sistecstudents;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.sistec.helperClasses.MyHelperClass;
import com.sistec.helperClasses.RemoteServiceUrl;
import com.sistec.helperClasses.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class AcadmicRecordFragment extends Fragment {

    private static String ACADEMIC_DTL_URL = RemoteServiceUrl.SERVER_URL + RemoteServiceUrl.METHOD_NAME.ACADEMICS;
    TextView tenthQualifTV, tenthPyTV, tenthBoardTV, tenthPercTV, tenthMediumTV, tenthStreamTV;
    TextView twelfthQualifTV, twelfthPyTV, twelfthBoardTV, twelfthPercTV, twelfthMediumTV, twelfthStreamTV;
    private Map<String, String> userStatus;
    private String e_no, is_login;
    private HomeActivity homeActivity;
    private View rootView;

    public AcadmicRecordFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_acadmic_record, container, false);
        getActivity().setTitle(R.string.title_academic);
        homeActivity = (HomeActivity) getActivity();
        userStatus = homeActivity.getUserStatus();
        e_no = userStatus.get("e_no");
        is_login = userStatus.get("is_login");
        findAllIds(rootView);
        getAcademicDetails();
        return rootView;
    }

    private void findAllIds(View view) {
        tenthQualifTV = view.findViewById(R.id.qualif_tenth_TV);
        tenthPyTV = view.findViewById(R.id.py_tenth_TV);
        tenthBoardTV = view.findViewById(R.id.board_tenth_TV);
        tenthPercTV = view.findViewById(R.id.perc_tenth_TV);
        tenthMediumTV = view.findViewById(R.id.medium_tenth_TV);
        tenthStreamTV = view.findViewById(R.id.stream_tenth_TV);
        twelfthQualifTV = view.findViewById(R.id.qualif_twelfth_TV);
        twelfthPyTV = view.findViewById(R.id.py_twelfth_TV);
        twelfthBoardTV = view.findViewById(R.id.board_twelfth_TV);
        twelfthPercTV = view.findViewById(R.id.perc_twelfth_TV);
        twelfthMediumTV = view.findViewById(R.id.medium_twelfth_TV);
        twelfthStreamTV = view.findViewById(R.id.stream_twelfth_TV);
    }

    private void getAcademicDetails() {
        MyHelperClass.showProgress(getActivity(), "Please Wait", "We are loading academic details");
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                ACADEMIC_DTL_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject root = new JSONObject(response);
                            String success = root.getString("success");
                            if (success.equals("1")) {
                                JSONObject graduationRecordJsonObject = root.getJSONObject("grad_rec");
                                JSONArray qualificationRecJsonArray = root.getJSONArray("qualif_rec");
                                setValuesInGradRec(graduationRecordJsonObject);
                                setValuesInQualifRec(qualificationRecJsonArray);
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

    private void setValuesInGradRec(JSONObject gradRecJSONObject) {
        try {
            int sem_count = gradRecJSONObject.getInt("sem_count");
            TableLayout tableLayout = rootView.findViewById(R.id.sem_result_table_layout);
            for (int i = 0; i < sem_count; i++) {
                LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View rowLayout = inflater.inflate(R.layout.sem_result_list_view, null);
                TextView semTextView = rowLayout.findViewById(R.id.list_view_sem);
                TextView yearTextView = rowLayout.findViewById(R.id.list_view_yr);
                TextView cgpaTextView = rowLayout.findViewById(R.id.list_view_cgpa);
                TextView statusTextView = rowLayout.findViewById(R.id.list_view_status);
                JSONObject semResultJSONObject = gradRecJSONObject.getJSONObject("" + i);
                semTextView.setText(semResultJSONObject.getString("sem"));
                yearTextView.setText(semResultJSONObject.getString("year"));
                cgpaTextView.setText(semResultJSONObject.getString("cgpa"));
                statusTextView.setText(semResultJSONObject.getString("status"));
                tableLayout.addView(rowLayout, i);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void setValuesInQualifRec(JSONArray qualifRecJSONArray) {
        try {
            JSONObject tenthQualifJSONObject = qualifRecJSONArray.getJSONObject(0);
            JSONObject twelfthQualifJSONObject = qualifRecJSONArray.getJSONObject(1);
            tenthQualifTV.setText(tenthQualifJSONObject.getString("qualification"));
            tenthPyTV.setText(tenthQualifJSONObject.getString("pass_year"));
            tenthBoardTV.setText(tenthQualifJSONObject.getString("board"));
            tenthPercTV.setText(tenthQualifJSONObject.getString("percentage"));
            tenthMediumTV.setText(tenthQualifJSONObject.getString("medium"));
            tenthStreamTV.setText(tenthQualifJSONObject.getString("stream"));
            twelfthQualifTV.setText(twelfthQualifJSONObject.getString("qualification"));
            twelfthPyTV.setText(twelfthQualifJSONObject.getString("pass_year"));
            twelfthBoardTV.setText(twelfthQualifJSONObject.getString("board"));
            twelfthPercTV.setText(twelfthQualifJSONObject.getString("percentage"));
            twelfthMediumTV.setText(twelfthQualifJSONObject.getString("medium"));
            twelfthStreamTV.setText(twelfthQualifJSONObject.getString("stream"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
