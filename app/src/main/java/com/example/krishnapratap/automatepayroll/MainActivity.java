package com.example.krishnapratap.automatepayroll;

import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class MainActivity extends AppCompatActivity implements OnFailureListener {

    private EditText userText, passText;
    private Button mLogin;
    private Switch aSwitch;
    private String registration = "emp_registration";
    private Boolean switchState;
    private SharedPreferences sp;
    private SharedPreferences.Editor editor;
    private LocationSettingsRequest settingsRequest;
    private SettingsClient client;
    private Task<LocationSettingsResponse> task;
    private LocationRequest mLocationRequest;
    private static final int REQUEST_CHECK_SETTING = 101;
    private FusedLocationProviderClient mFusedLocationProviderClient;






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        requestPermission();


        mLocationRequest=new LocationRequest();
        settingsRequest=new LocationSettingsRequest.Builder().addLocationRequest(mLocationRequest).build();

        client= LocationServices.getSettingsClient(this);

        task=client.checkLocationSettings(settingsRequest);

        task.addOnFailureListener(this);
        sp = getSharedPreferences("User_info", MODE_PRIVATE);

        aSwitch = findViewById(R.id.switch1);

        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                editor = sp.edit();
                editor.putBoolean("switch", isChecked);
                if (isChecked) {
                    editor.putString("switchName", "Admin");
                } else {
                    editor.putString("switchName", "Employee");
                }
                aSwitch.setText(sp.getString("switchName", "N/A"));
                switchState = sp.getBoolean("switch", false);
                editor.apply();
            }
        });




        userText = findViewById(R.id.username);
        passText = findViewById(R.id.password);
        mLogin = findViewById(R.id.bLogin);
        mLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String user = userText.getText().toString();
                String pass = passText.getText().toString();
                String type = "login";
                getRegistration();
                Background background = new Background();
                background.execute(type, user, pass);
            }
        });


    }

    @Override
    protected void onStart() {
        aSwitch.setText(sp.getString("switchName", "N/A"));
        switchState = sp.getBoolean("switch", false);
        aSwitch.setChecked(switchState);

        super.onStart();
    }



    private void getRegistration() {

        if (switchState) {
            registration = "registration";

        } else {
            registration = "emp_registration";
        }
    }

    @Override
    public void onBackPressed() {
        Intent startMain = new Intent(Intent.ACTION_MAIN);
        startMain.addCategory(Intent.CATEGORY_HOME);
        startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(startMain);
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[]{ACCESS_FINE_LOCATION}, 1);
        ActivityCompat.requestPermissions(this, new String[]{ACCESS_COARSE_LOCATION}, 1);
    }



    @Override
    public void onFailure(@NonNull Exception e) {
        int statusCode=((ApiException)e).getStatusCode();
        if(statusCode==LocationSettingsStatusCodes.RESOLUTION_REQUIRED)
        {
            // Locationg setting are not satisfied

            try
            {
                // show the dialog by calling
                ResolvableApiException resolvableApiException=(ResolvableApiException)e;
                resolvableApiException.startResolutionForResult(MainActivity.this,REQUEST_CHECK_SETTING);

            }
            catch (IntentSender.SendIntentException sendEx)
            {
                // Ignore the error
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(requestCode==REQUEST_CHECK_SETTING)
        {
            if(resultCode==RESULT_CANCELED){
                //stopTrackingLocatoin();
            }
            else  if(resultCode==RESULT_OK)
            {
                //startTrackingLocation();
                mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }







    private class Background extends AsyncTask<String, Void, String> {

        StringBuilder stringBuilder = new StringBuilder();
        private String mUsername;
        private String mId;

        @Override
        protected String doInBackground(String... strings) {

            String type = strings[0];
            mUsername = strings[1];

            if (type.equals("login")) {

                try {

                    String username = strings[1];
                    String password = strings[2];

                    URL url = new URL(ConstantValue.LOGIN_URL);
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setDoOutput(true);
                    httpURLConnection.setDoInput(true);
                    OutputStream outputStream = httpURLConnection.getOutputStream();
                    BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                    String post_data = URLEncoder.encode("username", "UTF-8") + "=" + URLEncoder.encode(username, "UTF-8") + "&" + URLEncoder.encode("password", "UTF-8") + "=" + URLEncoder.encode(password, "UTF-8") + "&" + URLEncoder.encode("registration", "UTF-8") + "=" + URLEncoder.encode(registration, "UTF-8") + "&" + type;
                    bufferedWriter.write(post_data);
                    bufferedWriter.flush();
                    bufferedWriter.close();
                    outputStream.close();
                    InputStream inputStream = httpURLConnection.getInputStream();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
                    String line;

                    while ((line = bufferedReader.readLine()) != null) {
                        stringBuilder.append(line.trim());
                    }

                    bufferedReader.close();
                    inputStream.close();
                    httpURLConnection.disconnect();

                    return stringBuilder.toString();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {


            if (!(s.equals("notsuccess"))) {

                try {
                    JSONArray jsonArray = new JSONArray(s);
                    JSONObject jsonObject = jsonArray.getJSONObject(0);
                    SharedPreferences sp = getSharedPreferences("User_info", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sp.edit();
                    String username = jsonObject.getString("Username");
                    String Id = jsonObject.getString("Id");

                    if (switchState) {

                        String Email = jsonObject.getString("Email");
                        String Image = jsonObject.getString("Image");
                        editor.putString("username", username);
                        editor.putString("Id", Id);
                        editor.putString("Email", Email);
                        editor.putString("Image", Image);
                        editor.apply();
                        Intent intent = new Intent(MainActivity.this, AdminHr.class);
                        startActivity(intent);
                    } else {
                        editor.putString("username", username);
                        editor.putString("Id", Id);

                        editor.apply();
                        Intent intent = new Intent(MainActivity.this, LoginPage.class);
                        startActivity(intent);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                android.widget.Toast.makeText(MainActivity.this, "Check Your Username and Password", Toast.LENGTH_LONG).show();
            }
        }
    }
}