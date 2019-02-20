package com.sistec.sistecstudents;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * A simple {@link Fragment} subclass.
 */
public class MidsemMarkFragment extends Fragment {


    public MidsemMarkFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_midsem_mark, container, false);
        getActivity().setTitle(R.string.title_midsem_marks);
        return rootView;
    }

}
