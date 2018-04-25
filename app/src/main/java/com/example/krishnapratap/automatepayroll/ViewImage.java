package com.example.krishnapratap.automatepayroll;


import android.app.DatePickerDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Calendar;


public class ViewImage extends Fragment implements View.OnClickListener {

    private static ImageView mImageView;
    public String imageUrl;
    Bitmap bitmap = null;
    private TextView ImageDate;
    private EditText empIdView;
    private Button selectDate, searchImage;
    private Calendar calendar;
    private int mYear, mMonth, mDay;
    private DatePickerDialog datePickerDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_view_image, container, false);

        mImageView = view.findViewById(R.id.viewImage);
        ImageDate = view.findViewById(R.id.viewImage_date);
        empIdView = view.findViewById(R.id.empIdView);
        selectDate = view.findViewById(R.id.selectDateView);
        searchImage = view.findViewById(R.id.searchImage);

        selectDate.setOnClickListener(this);
        searchImage.setOnClickListener(this);



        /*AlertDialog.Builder alert=new AlertDialog.Builder(getContext());
        alert.setTitle("Check");
        alert.setMessage("HI KP");
        alert.show();*/


        return view;
    }

    @Override
    public void onClick(View v) {

        if (v == selectDate) {
            calendar = Calendar.getInstance();
            mYear = calendar.get(Calendar.YEAR);
            mMonth = calendar.get(Calendar.MONTH);
            mDay = calendar.get(Calendar.DAY_OF_MONTH);

            datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                    ImageDate.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
                }
            }, mYear, mMonth, mDay);

            datePickerDialog.show();

        }
        if (v == searchImage) {

            String empId = empIdView.getText().toString();
            String empImageDate = ImageDate.getText().toString();


            Background background = new Background();
            background.execute("viewImage", empId, empImageDate);
            // DownloadImage downloadImage=new DownloadImage();
            // downloadImage.execute();
        }

    }

    public static class DownloadImage extends AsyncTask<String, Void, Bitmap> {

        ViewImage viewImage = new ViewImage();

        @Override
        protected Bitmap doInBackground(String... strings) {

            Bitmap bitmap = null;
            String k = strings[0];
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

        private void rotateImage(Bitmap bitmap) {
            ExifInterface exifInterface = null;

            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);
            Matrix matrix = new Matrix();
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    matrix.setRotate(90);
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    matrix.setRotate(180);
                    break;
                default:

            }

            Bitmap rotateBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
            mImageView.setImageBitmap(rotateBitmap);
        }
    }
}