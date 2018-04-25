package com.example.krishnapratap.automatepayroll;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class CheckEmployee extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_check_employee, container, false);


        Background background = new Background(view, getContext());
        background.execute("checkEmployee");

        return view;
    }


}
