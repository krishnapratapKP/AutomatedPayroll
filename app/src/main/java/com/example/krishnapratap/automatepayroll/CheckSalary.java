package com.example.krishnapratap.automatepayroll;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Objects;


public class CheckSalary extends Fragment implements View.OnClickListener {

    public String day;
    private TextView fromDate, toDate, dayValue, empIdValue, salaryValue;
    private Button selectFromDate, selectToDate, searchButton, clearButton;
    private EditText empIdEditText, rupeesPerday;
    private Calendar calendar;
    private int mYear, mMonth, mDay;
    private DatePickerDialog datePickerDialog;
    private View view;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;


    @Nullable
    @Override
    public Context getContext() {
        return super.getContext();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        editor=sharedPreferences.edit();
        editor.putString("fDate",fromDate.getText().toString());
        editor.putString("tDate",toDate.getText().toString());
        editor.putString("dayValue",dayValue.getText().toString());
        editor.putString("empIdValue",empIdValue.getText().toString());
        editor.putString("salaryValue",salaryValue.getText().toString());
        editor.apply();


    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        fromDate.setText(sharedPreferences.getString("fDate","From Date"));
        toDate.setText(sharedPreferences.getString("tDate","To Date"));
        dayValue.setText(sharedPreferences.getString("dayValue","Number of Days"));
        empIdValue.setText(sharedPreferences.getString("empIdValue","Employee Id"));
        salaryValue.setText(sharedPreferences.getString("salaryValue","Salary Value"));

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        getActivity().setTitle(R.string.checksalary);
        sharedPreferences=getContext().getSharedPreferences("info",Context.MODE_PRIVATE);

        view = inflater.inflate(R.layout.fragment_check_salary, container, false);
        fromDate = view.findViewById(R.id.fromdate);
        toDate = view.findViewById(R.id.todate);

        dayValue = view.findViewById(R.id.daysValue);

        empIdValue = view.findViewById(R.id.empIdValue);

        salaryValue = view.findViewById(R.id.salaryValue);
        selectFromDate = view.findViewById(R.id.fromDateButton);
        selectToDate = view.findViewById(R.id.toDateButton);
        searchButton = view.findViewById(R.id.searchSalary);
        clearButton = view.findViewById(R.id.clearSalary);
        empIdEditText = view.findViewById(R.id.empid_salary);
        rupeesPerday = view.findViewById(R.id.rupeesperday);

        Toast.makeText(getContext(), "Check", Toast.LENGTH_LONG).show();
        selectFromDate.setOnClickListener(this);
        selectToDate.setOnClickListener(this);
        searchButton.setOnClickListener(this);
        clearButton.setOnClickListener(this);


        return view;
    }


    @Override
    public void onClick(View v) {


        if (v == selectFromDate) {
            calendar = Calendar.getInstance();
            mYear = calendar.get(Calendar.YEAR);
            mMonth = calendar.get(Calendar.MONTH);
            mDay = calendar.get(Calendar.DAY_OF_MONTH);

            datePickerDialog = new DatePickerDialog(Objects.requireNonNull(getContext()), new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    String date = String.format(getString(R.string.dateFormat), year, month + 1, dayOfMonth);
                    fromDate.setText(date);
                }
            }, mYear, mMonth, mDay);

            datePickerDialog.show();
        }


        if (v == selectToDate) {
            calendar = Calendar.getInstance();
            mYear = calendar.get(Calendar.YEAR);
            mMonth = calendar.get(Calendar.MONTH);
            mDay = calendar.get(Calendar.DAY_OF_MONTH);

            datePickerDialog = new DatePickerDialog(Objects.requireNonNull(getContext()), new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    String date = String.format(getString(R.string.dateFormat), year, month + 1, dayOfMonth);

                    toDate.setText(date);
                }
            }, mYear, mMonth, mDay);

            datePickerDialog.show();
        }
        if (v == searchButton) {

            String empId = empIdEditText.getText().toString();
            String rupeesPerDay = rupeesPerday.getText().toString();

            if (!(empId.equals("") && rupeesPerDay.equals("") && fromDate.equals("") && toDate.equals(""))) {
                Background background = new Background(view);
                background.execute("SearchSalary", empId, fromDate.getText().toString(), toDate.getText().toString());

            } else {
                Toast.makeText(Objects.requireNonNull(getContext()), "Empty Value not Allowed", Toast.LENGTH_LONG).show();
            }


        }
        if (v == clearButton) {
            empIdEditText.getText().clear();
            rupeesPerday.getText().clear();
            fromDate.setText(R.string.FromDate);
            toDate.setText(R.string.ToDate);
            dayValue.setText(R.string.Day);
            salaryValue.setText(R.string.Salary);
            empIdValue.setText(R.string.Id);
        }
    }

    @Override
    public void onDestroy() {

        editor=sharedPreferences.edit();
        editor.remove("fDate");
        editor.remove("tDate");
        editor.remove("dayValue");
        editor.remove("empIdValue");
        editor.remove("salaryValue");
        editor.apply();
        super.onDestroy();
    }
}
