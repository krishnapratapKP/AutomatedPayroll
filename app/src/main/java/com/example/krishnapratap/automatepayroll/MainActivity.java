package com.example.krishnapratap.automatepayroll;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class MainActivity extends AppCompatActivity {

    EditText userText, passText;
    Button mLogin;
    SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        requestPermission();
        userText = (EditText) findViewById(R.id.username);
        passText = (EditText) findViewById(R.id.password);
        mLogin = (Button) findViewById(R.id.bLogin);
        mLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String user = userText.getText().toString();
                String pass = passText.getText().toString();
                String type = "login";
                Background background = new Background();
                background.execute(type, user, pass);
            }
        });
    }


     public class Background extends AsyncTask<String, Void, String[]> {

        private String mUsername;
        private String mId;
        ArrayList<String> stringJoiner = new ArrayList<>();

        @Override
        protected String[] doInBackground(String... strings) {

            String type = strings[0];
            mUsername = strings[1];
            String login_url = "http://192.168.43.242/login.php";

            if (type.equals("login")) {

                try {

                    String username = strings[1];
                    String password = strings[2];

                    URL url = new URL(login_url);
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setDoOutput(true);
                    httpURLConnection.setDoInput(true);
                    OutputStream outputStream = httpURLConnection.getOutputStream();
                    BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                    String post_data = URLEncoder.encode("username", "UTF-8") + "=" + URLEncoder.encode(username, "UTF-8") + "&" + URLEncoder.encode("password", "UTF-8") + "=" + URLEncoder.encode(password, "UTF-8") + "&" + type;
                    bufferedWriter.write(post_data);
                    bufferedWriter.flush();
                    bufferedWriter.close();
                    outputStream.close();
                    InputStream inputStream = httpURLConnection.getInputStream();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
                    String line;

                    while ((line = bufferedReader.readLine()) != null) {
                        stringJoiner.add(line);
                    }
                    String[] sSplit = stringJoiner.toString().split("<br>");
                    sSplit[0] = sSplit[0].replace("[", "");
                    sSplit[sSplit.length - 1] = sSplit[sSplit.length - 1].replace("]", "");
                    bufferedReader.close();
                    inputStream.close();
                    httpURLConnection.disconnect();

                    return sSplit;
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
        protected void onPostExecute(String[] s) {

            mId = s[0];


            if (s[s.length - 1].equals("login_success")) {
                SharedPreferences sp = getSharedPreferences("User_info", MODE_PRIVATE);
                SharedPreferences.Editor editor = sp.edit();
                editor.putString("username", mUsername);
                editor.putString("Id", mId);
                editor.apply();


                Intent intent = new Intent(MainActivity.this, LoginPage.class);

                startActivity(intent);
            }
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

}
