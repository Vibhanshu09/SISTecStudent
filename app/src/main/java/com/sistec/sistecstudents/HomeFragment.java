package com.sistec.sistecstudents;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.sistec.helperClasses.AppConnectivityStatus;
import com.sistec.helperClasses.RemoteServiceUrl;
import com.sistec.helperClasses.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class HomeFragment extends Fragment {

    private static String BASIC_DTL_URL = RemoteServiceUrl.SERVER_URL + RemoteServiceUrl.METHOD_NAME.BASIC_DTL;
    ImageView studImageView;
    TextView studNameTextView, eNoTextView, branchTextView, semesterTextView, collageTextView, motherNameTextView, fatherNameTextView, dobTextView;
    TextView categoryTextView, genderTextView, studMobileTextView, parentMobileTextView, studEmailTextView;
    TextView pAddLocalityTextView, pAddCityTextView, pAddStateTextView, cAddLocalityTextView, cAddCityTextView, cAddStateTextView;
    private Map<String, String> userStatus;
    private String e_no, is_login;
    private HomeActivity homeActivity;
    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        getActivity().setTitle(R.string.title_home);
        homeActivity = (HomeActivity) getActivity();
        userStatus = homeActivity.getUserStatus();
        e_no = userStatus.get("e_no");
        is_login = userStatus.get("is_login");
        findAllId(rootView);
        getBasicDetails();
        return rootView;
    }


    private void getBasicDetails() {
        AppConnectivityStatus.showProgress(getActivity(), "Please Wait", "We are loading your profile");
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                BASIC_DTL_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject root = new JSONObject(response);
                            String success = root.getString("success");
                            if (success.equals("1")) {
                                JSONArray detailsArray = root.getJSONArray("details");
                                JSONObject details = detailsArray.getJSONObject(0);
                                setValuesInTextView(details);
                                AppConnectivityStatus.hideProgress();
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
                        AppConnectivityStatus.hideProgress();
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

    private void findAllId(View view) {
        studImageView = view.findViewById(R.id.stud_image);
        studNameTextView = view.findViewById(R.id.stud_name);
        eNoTextView = view.findViewById(R.id.e_no);
        branchTextView = view.findViewById(R.id.branch);
        semesterTextView = view.findViewById(R.id.semester);
        collageTextView = view.findViewById(R.id.college);
        motherNameTextView = view.findViewById(R.id.mother_name);
        fatherNameTextView = view.findViewById(R.id.father_name);
        dobTextView = view.findViewById(R.id.dob);
        categoryTextView = view.findViewById(R.id.category);
        genderTextView = view.findViewById(R.id.gender);
        studMobileTextView = view.findViewById(R.id.stud_mobile);
        parentMobileTextView = view.findViewById(R.id.parent_mobile);
        studEmailTextView = view.findViewById(R.id.stud_email);
        pAddLocalityTextView = view.findViewById(R.id.p_add_locality);
        pAddCityTextView = view.findViewById(R.id.p_add_city);
        pAddStateTextView = view.findViewById(R.id.p_add_state);
        cAddLocalityTextView = view.findViewById(R.id.c_add_locality);
        cAddCityTextView = view.findViewById(R.id.c_add_city);
        cAddStateTextView = view.findViewById(R.id.c_add_state);

    }

    private void setValuesInTextView(JSONObject detailObject) {
        try {
            studNameTextView.setText(detailObject.getString("name"));
            eNoTextView.setText(detailObject.getString("e_no"));
            branchTextView.setText(detailObject.getString("branch"));
            semesterTextView.setText(detailObject.getString("semester"));
            collageTextView.setText(detailObject.getString("college"));
            motherNameTextView.setText(detailObject.getString("mother_name"));
            fatherNameTextView.setText(detailObject.getString("father_name"));
            dobTextView.setText(detailObject.getString("dob"));
            categoryTextView.setText(detailObject.getString("category"));
            genderTextView.setText(detailObject.getString("gender"));
            studMobileTextView.setText(detailObject.getString("stud_mob"));
            parentMobileTextView.setText(detailObject.getString("father_mob"));
            studEmailTextView.setText(detailObject.getString("email"));
            pAddLocalityTextView.setText(detailObject.getString("p_locality"));
            pAddCityTextView.setText(detailObject.getString("p_city"));
            pAddStateTextView.setText(detailObject.getString("p_state"));
            cAddLocalityTextView.setText(detailObject.getString("c_locality"));
            cAddCityTextView.setText(detailObject.getString("c_city"));
            cAddStateTextView.setText(detailObject.getString("c_state"));
            byte[] imgString = Base64.decode(detailObject.getString("image"), Base64.DEFAULT);
            Bitmap imgBitmap = BitmapFactory.decodeByteArray(imgString, 0, imgString.length);
            studImageView.setImageBitmap(imgBitmap);
        } catch (JSONException e) {
            e.printStackTrace();
            Log.i("Error", e.toString());
        }
    }


}
