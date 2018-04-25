package com.example.krishnapratap.automatepayroll;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.media.ExifInterface;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class LoginPage extends AppCompatActivity {

    private static final int REQUEST_IMAGE_CAPTURE = 1;
    SharedPreferences sp;
    private TextView mLongitude;
    private TextView mLatitude;

    private LocationRequest mLocationRequest;
    private long mInterval;
    private String msLongitude;
    private String msLatitude;
    private String mCurrentPhotoPath;
    private ImageView mImageView;
    private TextView mUsername;
    private String sUsername;
    private String mId;
    private Toolbar mToolbar;

    private FusedLocationProviderClient mFusedLocationProviderClient;
    private static final int MY_PERMISSION_REQUEST_FINE_LOCATION = 101;
    private LocationCallback mLocationCallback;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);

        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        mImageView = findViewById(R.id.uImageView);
        mUsername = findViewById(R.id.tUsername);

        dispatchTakePictureIntent();

        mInterval = 1000 * 60;

        getLocationRequest();

        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            mFusedLocationProviderClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if (location != null) {
                        getLocation(location);
                    } else {
                        Toast.makeText(LoginPage.this, "Location is not available", Toast.LENGTH_LONG).show();
                    }
                }
            });
        } else {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSION_REQUEST_FINE_LOCATION);
        }

        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                for (Location location : locationResult.getLocations()) {
                    if (location != null) {
                        getLocation(location);
                        String mLocation = "location";
                        String date = new SimpleDateFormat(getString(R.string.dateL), Locale.US).format(new Date());


                        Background background = new Background();
                        background.execute(mLocation, mId, msLongitude, msLatitude, date);

                    }
                }
            }
        };
        mFusedLocationProviderClient.requestLocationUpdates(mLocationRequest, mLocationCallback, null);

        sp = getSharedPreferences("User_info", MODE_PRIVATE);
        sUsername = sp.getString("username", "N/A");
        mId = sp.getString("Id", "N/A");

        mUsername.setText(sUsername);
        mLatitude = findViewById(R.id.latitude);
        mLongitude = findViewById(R.id.longitude);
    }

    private void getLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(180000);
        mLocationRequest.setFastestInterval(3000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    public void getLocation(Location location) {
        mLatitude.setText(String.valueOf(location.getLatitude()));
        mLongitude.setText(String.valueOf(location.getLongitude()));
        msLatitude = String.valueOf(location.getLatitude());
        msLongitude = String.valueOf(location.getLongitude());
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);


        switch (requestCode) {
            case MY_PERMISSION_REQUEST_FINE_LOCATION:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //
                } else {
                    Toast.makeText(getApplicationContext(), "This app requires location permission to be gran", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private void fileSize() {
        File file = new File(mCurrentPhotoPath);
        if (file.length() == 0) {

            Intent intent = new Intent(LoginPage.this, MainActivity.class);
            startActivity(intent);

        }
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (photoFile != null) {
                Uri photoUri = FileProvider.getUriForFile(this, "com.example.krishnapratap.automatepayroll", photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    private File createImageFile() throws IOException {
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(new Date());
        String imageFileName = "JPEG_" + timestamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName, ".jpg", storageDir);

        mCurrentPhotoPath = image.getAbsolutePath();

        return image;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {


        if (requestCode == REQUEST_IMAGE_CAPTURE) {
            if (resultCode == RESULT_OK) {
                Bitmap bitmap;
                BitmapFactory.Options bitmapFactory = new BitmapFactory.Options();
                bitmapFactory.inSampleSize = 4;
                bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath);

                rotateImage(bitmap);

                String date = new SimpleDateFormat(getString(R.string.dateL), Locale.US).format(new Date());
                String time = new SimpleDateFormat(getString(R.string.timeL), Locale.US).format(new Date());
                Background background = new Background();
                background.execute("Image", mId, mCurrentPhotoPath, date, time, msLatitude, msLongitude);

            } else {
                fileSize();
            }
        }


    }




    @Override
    public void onBackPressed() {

        if (sUsername != null) {
            Intent startMain = new Intent(Intent.ACTION_MAIN);
            startMain.addCategory(Intent.CATEGORY_HOME);
            startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(startMain);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.log_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.nav_logout) {
            mFusedLocationProviderClient.removeLocationUpdates(mLocationCallback);
            Intent intent = new Intent(LoginPage.this, MainActivity.class);
            startActivity(intent);
            return true;

        }
        return super.onOptionsItemSelected(item);
    }

    private void rotateImage(Bitmap bitmap) {

        ExifInterface exifInterface = null;
        try {
            exifInterface = new ExifInterface(mCurrentPhotoPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        assert exifInterface != null;
        int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);
        Matrix matrix = new Matrix();
        switch (orientation) {
            case ExifInterface.ORIENTATION_ROTATE_90:
                matrix.setRotate(90);
                break;
            case ExifInterface.ORIENTATION_ROTATE_180:
                matrix.setRotate(180);
                break;
            case ExifInterface.ORIENTATION_ROTATE_270:
                matrix.setRotate(270);
                break;

            default:

        }

        Bitmap rotateBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        mImageView.setImageBitmap(rotateBitmap);
    }


}