package com.example.krishnapratap.automatepayroll;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
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

public class MainActivity extends AppCompatActivity {

    EditText userText,passText;
    Button mLogin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        userText=(EditText)findViewById(R.id.username);
        passText=(EditText)findViewById(R.id.password);
        mLogin=(Button)findViewById(R.id.bLogin);
        mLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String user=userText.getText().toString();
                String pass=passText.getText().toString();
                String type="login";
                Background background=new Background();
                background.execute(type,user,pass);
            }
        });
    }

    public class Background extends AsyncTask<String,Void,String> {

        private String mUsername;

        @Override
        protected String doInBackground(String... strings) {

            String type=strings[0];
             mUsername = strings[1];
            String login_url="http://192.168.43.242/login.php";

            if(type.equals("login")){

                try{

                    String username=strings[1];
                    String password=strings[2];

                    URL url = new URL(login_url);
                    HttpURLConnection httpURLConnection =(HttpURLConnection)url.openConnection();
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setDoOutput(true);
                    httpURLConnection.setDoInput(true);
                    OutputStream outputStream=httpURLConnection.getOutputStream();
                    BufferedWriter bufferedWriter=new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));
                    String post_data=URLEncoder.encode("username","UTF-8")+"="+URLEncoder.encode(username,"UTF-8")+"&"+URLEncoder.encode("password","UTF-8")+"="+ URLEncoder.encode(password,"UTF-8")+"&"+type;
                    bufferedWriter.write(post_data);
                    bufferedWriter.flush();
                    bufferedWriter.close();
                    outputStream.close();
                    InputStream inputStream=httpURLConnection.getInputStream();
                    BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(inputStream,"iso-8859-1"));
                    String result="";
                    String line="";
                    while ((line=bufferedReader.readLine())!=null){
                        result+=line;
                    }
                    bufferedReader.close();
                    inputStream.close();
                    httpURLConnection.disconnect();
                    if(result.equals(""))
                        return "No connection ";
                    else
                        return result;

                } catch (MalformedURLException e) {
                    e.printStackTrace();
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
            if(s.equals("login_success")) {
                SharedPreferences sp=getSharedPreferences("User_info",MODE_PRIVATE);
                SharedPreferences.Editor editor=sp.edit();
                editor.putString("username",mUsername);
                editor.apply();
                Intent intent = new Intent(MainActivity.this,LoginPage.class);
                startActivity(intent);
            }
        }
    }


}
