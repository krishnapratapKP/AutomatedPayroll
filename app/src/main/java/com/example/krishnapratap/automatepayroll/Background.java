package com.example.krishnapratap.automatepayroll;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.MessageFormat;
import java.util.ArrayList;


/**
 * Created by Krishna Pratap on 13-03-2018.
 */
public class Background extends AsyncTask<String, Void, String> {

    private String encodedImageString;
    private Context mContext;
    private String type;
    private View mView;

    public Background(Context context) {
        mContext = context;
    }

    public Background(View view, Context context) {
        mView = view;
        mContext = context;
    }

    public Background(View view) {
        mView = view;

    }

    public Background() {

    }

    @Override
    protected String doInBackground(String... strings) {


        type = strings[0];


        if (type.equals("location")) {
            String mId = strings[1];
            String mLongitude = strings[2];
            String mLatitude = strings[3];
            String mDate = strings[4];
            String result = getLocation(type, ConstantValue.EMP_LOCATION_URL, mId, mLongitude, mLatitude, mDate);
            if (result != null) return result;
        }

        if (type.equals("Image")) {
            String mId = strings[1];
            String Image = strings[2];
            String mDate = strings[3];
            String mTime = strings[4];
            String mLongitude = strings[5];
            String mLatitude = strings[6];
            encodeImage(Image);
            String result = getEmpLog(type, ConstantValue.EMP_LOG_URL, mId, mDate, mTime, mLongitude, mLatitude);
            if (result != null) return result;
        }

        if (type.equals("SearchSalary")) {
            String empId = strings[1];

            String fromDate = strings[2];
            String toDate = strings[3];
            String result = getSearchSalary(empId, fromDate, toDate, ConstantValue.SEARCH_SALARY_URL);
            if (result != null) return result;
        }

        if (type.equals("checkPassword")) {
            String empId = strings[1];
            String oldPass = strings[2];
            String result = getPasswordValidity(empId, oldPass, ConstantValue.CHANGE_PASSWORD_URL);
            if (result != null) return result;
        }

        if (type.equals("changePassword")) {
            String empId = strings[1];
            String newPass = strings[2];
            String result = getChangePassword(empId, newPass, ConstantValue.CHANGE_PASSWORD_URL);
            if (result != null) return result;
        }


        if (type.equals("viewImage")) {

            String mId = strings[1];
            String mDate = strings[2];

            String result = getViewImage(mId, mDate, ConstantValue.VIEW_IMAGE_URL);
            if (result != null) return result;

        }

        if (type.equals("checkEmployee")) {
            String result = getCheckEmployee(ConstantValue.CHECK_EMPLOYEE_URL);
            if (result != null) return result;
        }

        return null;
    }


    @Nullable
    private String getEmpLog(String mImage, String empLog_url, String mId, String mDate, String mTime, String mLongitude, String mLatitude) {
        try {
            URL url = new URL(empLog_url);
            HttpURLConnection httpURLConnection = getHttpURLConnection(url);
            OutputStream outputStream = httpURLConnection.getOutputStream();
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
            String post_data = URLEncoder.encode("emp_id", "UTF-8") + "=" + URLEncoder.encode(mId, "UTF-8")
                    + "&" + URLEncoder.encode("empLongitude", "UTF-8") + "=" + URLEncoder.encode(mLongitude, "UTF-8")
                    + "&" + URLEncoder.encode("empLatitude", "UTF-8") + "=" + URLEncoder.encode(mLatitude, "UTF-8")
                    + "&" + URLEncoder.encode("empImage", "UTF-8") + "=" + URLEncoder.encode(encodedImageString, "UTF-8")
                    + "&" + URLEncoder.encode("empDate", "UTF-8") + "=" + URLEncoder.encode(mDate, "UTF-8")
                    + "&" + URLEncoder.encode("empTime", "UTF-8") + "=" + URLEncoder.encode(mTime, "UTF-8")
                    + "&" + mImage;
            bufferedWriter.write(post_data);
            bufferedWriter.flush();
            bufferedWriter.close();
            outputStream.close();
            return getInputStreamResult(httpURLConnection);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    @Nullable
    private String getLocation(String mlocation, String empLocation_url, String mId, String mLongitude, String mLatitude, String mDate) {
        try {
            URL url = new URL(empLocation_url);
            HttpURLConnection httpURLConnection = getHttpURLConnection(url);
            OutputStream outputStream = httpURLConnection.getOutputStream();
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
            String post_data = URLEncoder.encode("emp_id", "UTF-8") + "=" + URLEncoder.encode(mId, "UTF-8")
                    + "&" + URLEncoder.encode("mLongitude", "UTF-8") + "=" + URLEncoder.encode(mLongitude, "UTF-8")
                    + "&" + URLEncoder.encode("mLatitude", "UTF-8") + "=" + URLEncoder.encode(mLatitude, "UTF-8")
                    + "&" + URLEncoder.encode("empDate", "UTF-8") + "=" + URLEncoder.encode(mDate, "UTF-8")
                    + "&" + mlocation;
            bufferedWriter.write(post_data);
            bufferedWriter.flush();
            bufferedWriter.close();
            outputStream.close();
            return getInputStreamResult(httpURLConnection);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String getSearchSalary(String empId, String fromDate, String toDate, String searchSalary) {

        try {
            URL url = new URL(searchSalary);
            HttpURLConnection httpURLConnection = getHttpURLConnection(url);
            OutputStream outputStream = httpURLConnection.getOutputStream();
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
            String post_data = URLEncoder.encode("emp_id", "UTF-8") + "=" + URLEncoder.encode(empId, "UTF-8")
                    + "&" + URLEncoder.encode("fromDate", "UTF-8") + "=" + URLEncoder.encode(fromDate, "UTF-8")
                    + "&" + URLEncoder.encode("toDate", "UTF-8") + "=" + URLEncoder.encode(toDate, "UTF-8");
            bufferedWriter.write(post_data);
            bufferedWriter.flush();
            bufferedWriter.close();
            outputStream.close();
            return getInputStreamResult(httpURLConnection);

        } catch (IOException e) {
            e.printStackTrace();
        }


        return null;
    }


    private String getPasswordValidity(String empId, String oldPass, String password_url) {

        try {
            URL url = new URL(password_url);
            HttpURLConnection httpURLConnection = getHttpURLConnection(url);
            OutputStream outputStream = httpURLConnection.getOutputStream();
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
            String postData = URLEncoder.encode("empId", "UTF-8") + "=" + URLEncoder.encode(empId, "UTF-8")
                    + "&" + URLEncoder.encode("oldpass", "UTF-8") + "=" + URLEncoder.encode(oldPass, "UTF-8")
                    + "&check";
            bufferedWriter.write(postData);
            bufferedWriter.flush();
            bufferedWriter.close();
            outputStream.close();

            return getInputStreamResult(httpURLConnection);

        } catch (IOException e) {
            e.printStackTrace();
        }


        return null;
    }

    private String getChangePassword(String empId, String newPass, String password_url) {

        try {
            URL url = new URL(password_url);
            HttpURLConnection httpURLConnection = getHttpURLConnection(url);
            OutputStream outputStream = httpURLConnection.getOutputStream();
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
            String postData = URLEncoder.encode("empId", "UTF-8") + "=" + URLEncoder.encode(empId, "UTF-8")
                    + "&" + URLEncoder.encode("newpass", "UTF-8") + "=" + URLEncoder.encode(newPass, "UTF-8")
                    + "&change";
            bufferedWriter.write(postData);
            bufferedWriter.flush();
            bufferedWriter.close();
            outputStream.close();

            return getInputStreamResult(httpURLConnection);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;

    }

    private String getViewImage(String mId, String mDate, String viewImage_url) {

        try {
            URL url = new URL(viewImage_url);
            HttpURLConnection httpURLConnection = getHttpURLConnection(url);
            OutputStream outputStream = httpURLConnection.getOutputStream();
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
            String postData = URLEncoder.encode("empId", "UTF-8") + "=" + URLEncoder.encode(mId, "UTF-8")
                    + "&" + URLEncoder.encode("date", "UTF-8") + "=" + URLEncoder.encode(mDate, "UTF-8");
            bufferedWriter.write(postData);
            bufferedWriter.flush();
            bufferedWriter.close();
            outputStream.close();

            return getInputStreamResult(httpURLConnection);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    private String getCheckEmployee(String checkEmployee_url) {


        try {
            URL url = new URL(checkEmployee_url);
            HttpURLConnection httpURLConnection = getHttpURLConnection(url);
            OutputStream outputStream = httpURLConnection.getOutputStream();
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
            String postData = URLEncoder.encode("empId", "UTF-8") + "=" + URLEncoder.encode("", "UTF-8")
                    + "&" + URLEncoder.encode("date", "UTF-8") + "=" + URLEncoder.encode("", "UTF-8");
            bufferedWriter.write(postData);
            bufferedWriter.flush();
            bufferedWriter.close();
            outputStream.close();

            return getInputStreamResult(httpURLConnection);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    @NonNull
    private HttpURLConnection getHttpURLConnection(URL url) throws IOException {
        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
        httpURLConnection.setRequestMethod("POST");
        httpURLConnection.setDoInput(true);
        httpURLConnection.setDoOutput(true);
        return httpURLConnection;
    }


    @NonNull
    private String getInputStreamResult(HttpURLConnection httpURLConnection) throws IOException {
        InputStream inputStream = httpURLConnection.getInputStream();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
        StringBuilder result = new StringBuilder();
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            result.append(line.trim());
        }
        bufferedReader.close();
        inputStream.close();
        httpURLConnection.disconnect();
        if (result.toString().equals(""))
            return "No connection ";
        else
            return result.toString();
    }

    private void encodeImage(String image) {
        Bitmap mBitmap = BitmapFactory.decodeFile(image);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);

        byte[] arrayImage = byteArrayOutputStream.toByteArray();
        encodedImageString = Base64.encodeToString(arrayImage, 0);

    }

    @Override
    protected void onPreExecute() {
    }

    @Override
    protected void onPostExecute(String s) {


        if (type.equals("checkPassword")) {
            if (s.equals("success")) {
                EditText oldpass = mView.findViewById(R.id.oldpass);
                Button checkPassword = mView.findViewById(R.id.buttonPassword);
                EditText newpass = mView.findViewById(R.id.newpass);
                Button changePassword = mView.findViewById(R.id.buttonChangePassword);

                oldpass.setVisibility(View.INVISIBLE);
                checkPassword.setVisibility(View.INVISIBLE);
                newpass.setVisibility(View.VISIBLE);
                changePassword.setVisibility(View.VISIBLE);

            } else {
                Toast.makeText(mContext, "Re-Check Password", Toast.LENGTH_LONG).show();
            }
        }

        if (type.equals("changePassword")) {
            if (s.equals("success")) {
                EditText oldpass = mView.findViewById(R.id.oldpass);
                Button checkPassword = mView.findViewById(R.id.buttonPassword);
                EditText newpass = mView.findViewById(R.id.newpass);
                Button changePassword = mView.findViewById(R.id.buttonChangePassword);

                oldpass.setVisibility(View.VISIBLE);
                checkPassword.setVisibility(View.VISIBLE);
                newpass.setVisibility(View.INVISIBLE);
                changePassword.setVisibility(View.INVISIBLE);
                Toast.makeText(mContext, "Successfully Changed Password", Toast.LENGTH_LONG).show();

            } else {
                Toast.makeText(mContext, "Re-Check Password", Toast.LENGTH_LONG).show();
            }
        }

        if (type.equals("viewImage")) {

            ViewImage.DownloadImage downloadImage = new ViewImage.DownloadImage();
            downloadImage.execute(s);

        }

        if (type.equals("checkEmployee")) {

            try {
                JSONArray jsonArray = new JSONArray(s);
                JSONObject jsonObject;

                ArrayList<EmployeeInformation> employeeInformations = new ArrayList<>();

                for (int i = 0; i < jsonArray.length(); i++) {
                    jsonObject = jsonArray.getJSONObject(i);
                    employeeInformations.add(new EmployeeInformation(jsonObject.getInt("Id"), jsonObject.getString("Username"), jsonObject.getString("empEmail"), jsonObject.getString("empImage"), jsonObject.getLong("empMobileNo")));

                }

                EmployeeInformationAdapter employeeInformationAdapter = new EmployeeInformationAdapter(mContext, employeeInformations);

                ListView listView = mView.findViewById(R.id.list_view);
                listView.setAdapter(employeeInformationAdapter);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        if (type.equals("SearchSalary")) {

            TextView day = mView.findViewById(R.id.daysValue);
            TextView id = mView.findViewById(R.id.empIdValue);
            TextView salary = mView.findViewById(R.id.salaryValue);
            EditText empId = mView.findViewById(R.id.empid_salary);
            EditText rupeesPerday = mView.findViewById(R.id.rupeesperday);


            id.setText(empId.getText().toString());
            day.setText(s);
            int tSalary = Integer.parseInt(rupeesPerday.getText().toString()) * Integer.parseInt(s);
            salary.setText(MessageFormat.format("{0}", tSalary));

        }

    }


}
