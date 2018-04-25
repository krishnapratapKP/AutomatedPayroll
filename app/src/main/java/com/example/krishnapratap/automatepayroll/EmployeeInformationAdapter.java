package com.example.krishnapratap.automatepayroll;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

public class EmployeeInformationAdapter extends ArrayAdapter<EmployeeInformation> {

    ImageView employeeImage;
    ArrayList<EmployeeInformation> imageGet;


    public EmployeeInformationAdapter(Context context, ArrayList<EmployeeInformation> employeeInformations) {
        super(context, 0, employeeInformations);
        imageGet = employeeInformations;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        View employeeInfo = convertView;
        if (employeeInfo == null) {
            employeeInfo = LayoutInflater.from(getContext()).inflate(R.layout.employee_info, parent, false);

        }

        EmployeeInformation currentEmployeeInfo = getItem(position);


        employeeImage = employeeInfo.findViewById(R.id.infoImage);
        DownloadImage downloadImage = new DownloadImage(employeeImage);
        downloadImage.execute(imageGet.get(position).getmEmployeeImage());


        TextView employeeId = employeeInfo.findViewById(R.id.infoId);
        employeeId.setText("" + currentEmployeeInfo.getmEmployeeId());

        TextView employeeName = employeeInfo.findViewById(R.id.infoName);
        employeeName.setText(currentEmployeeInfo.getmEmployeeName());

        TextView employeeEmail = employeeInfo.findViewById(R.id.infoEmail);
        employeeEmail.setText(currentEmployeeInfo.getmEmployeeEmail());

        TextView employeeMobile = employeeInfo.findViewById(R.id.infoMobile);
        employeeMobile.setText("" + currentEmployeeInfo.getmEmployeeMobile());


        return employeeInfo;
    }


    private class DownloadImage extends AsyncTask<String, Void, Bitmap> {

        ImageView mImageView;

        public DownloadImage(ImageView imageView) {
            mImageView = imageView;
        }

        @Override
        protected Bitmap doInBackground(String... strings) {

            Bitmap bitmap = null;

            String imageUrl = ConstantValue.HOST_IP + strings[0];

            try {
                BitmapFactory.Options bitmapFactory = new BitmapFactory.Options();
                bitmapFactory.inSampleSize = 8;
                InputStream inputStream = new URL(imageUrl).openStream();
                bitmap = BitmapFactory.decodeStream(inputStream, null, bitmapFactory);

            } catch (IOException e) {
                e.printStackTrace();
            }
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {


            mImageView.setImageBitmap(bitmap);

        }


    }


}
