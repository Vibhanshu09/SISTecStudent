package com.sistec.sistecstudents;


import android.os.Bundle;
import android.support.v4.app.Fragment;
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
import com.sistec.helperClasses.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class FeeStatusFragment extends Fragment {

    private static String FEE_STATUS_URL = RemoteServiceUrl.SERVER_URL + RemoteServiceUrl.METHOD_NAME.FEE_STATUS;
    TextView firstInstPayableTV, firstInstPaidTV, firstInstDuesTV, secondInstPayableTV, secondInstPaidTV, secondInstDuesTV;
    TextView totalDuesTV, scholarshipTV;
    private Map<String, String> userStatus;
    private String e_no, is_login;
    private HomeActivity homeActivity;

    public FeeStatusFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_fee_status, container, false);
        getActivity().setTitle(R.string.title_fee_status);
        homeActivity = (HomeActivity) getActivity();
        userStatus = homeActivity.getUserStatus();
        e_no = userStatus.get("e_no");
        is_login = userStatus.get("is_login");
        findAllIds(rootView);
        getFeeStatus();
        return rootView;
    }

    private void findAllIds(View view) {
        firstInstPayableTV = view.findViewById(R.id.first_inst_payable_TV);
        firstInstPaidTV = view.findViewById(R.id.first_inst_paid_TV);
        firstInstDuesTV = view.findViewById(R.id.first_inst_dues_TV);
        secondInstPayableTV = view.findViewById(R.id.second_inst_payable_TV);
        secondInstPaidTV = view.findViewById(R.id.second_inst_paid_TV);
        secondInstDuesTV = view.findViewById(R.id.second_inst_dues_TV);
        totalDuesTV = view.findViewById(R.id.total_dues_TV);
        scholarshipTV = view.findViewById(R.id.scholarship_TV);
    }

    private void getFeeStatus() {
        MyHelperClass.showProgress(getActivity(), "Please Wait", "We are loading your Fee status");
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                FEE_STATUS_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject root = new JSONObject(response);
                            String success = root.getString("success");
                            if (success.equals("1")) {
                                JSONArray feeStatusArray = root.getJSONArray("fee_status");
                                JSONObject feeDetails = feeStatusArray.getJSONObject(0);
                                setValuesInTextView(feeDetails);
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

    private void setValuesInTextView(JSONObject feeStatusJSONObject) {
        try {
            firstInstPayableTV.setText(String.format("%.2f", feeStatusJSONObject.getDouble("f_payable")));
            firstInstPaidTV.setText(String.format("%.2f", feeStatusJSONObject.getDouble("f_paid")));
            firstInstDuesTV.setText(String.format("%.2f", feeStatusJSONObject.getDouble("f_due")));
            secondInstPayableTV.setText(String.format("%.2f", feeStatusJSONObject.getDouble("s_payable")));
            secondInstPaidTV.setText(String.format("%.2f", feeStatusJSONObject.getDouble("s_paid")));
            secondInstDuesTV.setText(String.format("%.2f", feeStatusJSONObject.getDouble("s_due")));
            totalDuesTV.setText(String.format("%.2f", feeStatusJSONObject.getDouble("total_due")));
            scholarshipTV.setText(feeStatusJSONObject.getString("scholarship"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
