package com.example.krishnapratap.automatepayroll;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;


public class ChangePassword extends Fragment implements View.OnClickListener {

    private EditText empIdPass, empOldPass, empNewPass;
    private Button buttonPassword, buttonChangePass;
    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_change_password, container, false);

        empIdPass = view.findViewById(R.id.empIdPassword);
        empOldPass = view.findViewById(R.id.oldpass);
        empNewPass = view.findViewById(R.id.newpass);
        buttonPassword = view.findViewById(R.id.buttonPassword);
        buttonChangePass = view.findViewById(R.id.buttonChangePassword);


        buttonPassword.setOnClickListener(this);
        buttonChangePass.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {


        if (v == buttonPassword) {
            String empId = empIdPass.getText().toString();
            String empOldPassword = empOldPass.getText().toString();


            Background background = new Background(view, getContext());
            background.execute("checkPassword", empId, empOldPassword);


        }

        if (v == buttonChangePass) {
            String empId = empIdPass.getText().toString();
            String empNewPassword = empNewPass.getText().toString();


            Background background = new Background(view, getContext());
            background.execute("changePassword", empId, empNewPassword);
        }

    }

    public void vis() {
        buttonPassword.setVisibility(View.GONE);
        empOldPass.setVisibility(View.GONE);
        empNewPass.setVisibility(View.VISIBLE);
        buttonChangePass.setVisibility(View.VISIBLE);
    }
}
