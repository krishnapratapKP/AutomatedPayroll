package com.example.krishnapratap.automatepayroll;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.util.Base64;

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

import static android.support.v4.content.ContextCompat.startActivity;


/**
 * Created by Krishna Pratap on 13-03-2018.
 */

public class Background extends AsyncTask<String, Void, String> {

    private String encodedImageString;


    @Override
    protected String doInBackground(String... strings) {

        String mlocation = strings[0];
        String mImage = strings[0];
        String empLocation_url = "http://192.168.43.242/emp_location.php";
        String empLog_url = "http://192.168.43.242/emp_log.php";

        if (mlocation.equals("location")) {
            String mId = strings[1];
            String mLongitude = strings[2];
            String mLatitude = strings[3];
            String result = getLocation(mlocation, empLocation_url, mId, mLongitude, mLatitude);
            if (result != null) return result;
        }

        if (mImage.equals("Image")) {
            String mId = strings[1];
            String Image = strings[2];
            String mDate = strings[3];
            String mLongitude = strings[4];
            String mLatitude = strings[5];
            encodeImage(Image);
            String result = getEmpLog(mImage, empLog_url, mId, mDate, mLongitude, mLatitude);
            if (result != null) return result;
        }
        return null;
    }


    @Nullable
    private String getEmpLog(String mImage, String empLog_url, String mId, String mDate, String mLongitude, String mLatitude) {
        try {
            URL url = new URL(empLog_url);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoInput(true);
            httpURLConnection.setDoOutput(true);
            OutputStream outputStream = httpURLConnection.getOutputStream();
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
            String post_data = URLEncoder.encode("emp_id", "UTF-8") + "=" + URLEncoder.encode(mId, "UTF-8")
                    + "&" + URLEncoder.encode("empLongitude", "UTF-8") + "=" + URLEncoder.encode(mLongitude, "UTF-8")
                    + "&" + URLEncoder.encode("empLatitude", "UTF-8") + "=" + URLEncoder.encode(mLatitude, "UTF-8")
                    + "&" + URLEncoder.encode("empImage", "UTF-8") + "=" + URLEncoder.encode(encodedImageString, "UTF-8")
                    + "&" + URLEncoder.encode("empDate", "UTF-8") + "=" + URLEncoder.encode(mDate, "UTF-8")
                    + "&" + mImage;
            bufferedWriter.write(post_data);
            bufferedWriter.flush();
            bufferedWriter.close();
            outputStream.close();
            InputStream inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
            StringBuilder result = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                result.append(line);
            }
            bufferedReader.close();
            inputStream.close();
            httpURLConnection.disconnect();
            if (result.toString().equals(""))
                return "No connection ";
            else
                return result.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Nullable
    private String getLocation(String mlocation, String empLocation_url, String mId, String mLongitude, String mLatitude) {
        try {
            URL url = new URL(empLocation_url);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoInput(true);
            httpURLConnection.setDoOutput(true);
            OutputStream outputStream = httpURLConnection.getOutputStream();
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
            String post_data = URLEncoder.encode("emp_id", "UTF-8") + "=" + URLEncoder.encode(mId, "UTF-8")
                    + "&" + URLEncoder.encode("mLongitude", "UTF-8") + "=" + URLEncoder.encode(mLongitude, "UTF-8")
                    + "&" + URLEncoder.encode("mLatitude", "UTF-8") + "=" + URLEncoder.encode(mLatitude, "UTF-8")
                    + "&" + mlocation;
            bufferedWriter.write(post_data);
            bufferedWriter.flush();
            bufferedWriter.close();
            outputStream.close();
            InputStream inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
            StringBuilder result = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                result.append(line);
            }
            bufferedReader.close();
            inputStream.close();
            httpURLConnection.disconnect();
            if (result.toString().equals(""))
                return "No connection ";
            else
                return result.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
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
}
