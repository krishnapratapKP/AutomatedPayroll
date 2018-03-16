package com.example.krishnapratap.automatepayroll;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static java.lang.System.exit;


import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class LoginPage extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,GoogleApiClient.OnConnectionFailedListener,LocationListener{

    private TextView mLongitude;
    private TextView mLatitude;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private static final long mInterval=1000*60*1;
    private String msLongitude;
    private String msLatitude;
    private String mCurrentPhotoPath;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    private  ImageView mImageView;
    private TextView mUsername;
    private String sUsername;
    private String mId;
    private  String mDateTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);


        mImageView=(ImageView)findViewById(R.id.uImageView);
        mUsername=(TextView)findViewById(R.id.tUsername);

        requestPermission();
        dispatchTakePicturInten();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        SharedPreferences sp=getSharedPreferences("User_info",MODE_PRIVATE);
        sUsername =sp.getString("username","N/A");
        mId =sp.getString("Id","N/A");
        mUsername.setText(sUsername);
        mLatitude=(TextView)findViewById(R.id.latitude);
        mLongitude=(TextView)findViewById(R.id.longitude);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();

    }

    @Override
    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    @Override
    public void onConnected( Bundle bundle) {

        mLocationRequest=LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(mInterval);

        if(ActivityCompat.checkSelfPermission(this,ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED&&ActivityCompat.checkSelfPermission(this,ACCESS_COARSE_LOCATION)!=PackageManager.PERMISSION_GRANTED){
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient,mLocationRequest,this);

    }

    private void fileSize() {
        File file=new File(mCurrentPhotoPath);
        if(file.length()==0)
        {

            Intent intent=new Intent(LoginPage.this,MainActivity.class);
            startActivity(intent);

        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {

        msLatitude=String.valueOf(location.getLatitude());
        msLongitude=String.valueOf(location.getLongitude());
        mLatitude.setText(msLatitude);
        mLongitude.setText(msLongitude);
        String mLocation="location";
        Background background = new Background();
        background.execute(mLocation,mId,msLongitude,msLatitude);
    }

    private  void requestPermission(){
        ActivityCompat.requestPermissions(this,new String[]{ACCESS_FINE_LOCATION},1);
        ActivityCompat.requestPermissions(this,new String[]{ACCESS_COARSE_LOCATION},1);
    }

    private void dispatchTakePicturInten(){
        Intent takePictureIntent= new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if(takePictureIntent.resolveActivity(getPackageManager())!=null)
        {
            File photoFile=null;
            try{
                photoFile=createImageFile();
            }catch (IOException e){
                e.printStackTrace();
            }

            if(photoFile!=null)
            {
                Uri photoUri = FileProvider.getUriForFile(this,"com.example.krishnapratap.automatepayroll",photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,photoUri);
                startActivityForResult(takePictureIntent,REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    private File createImageFile() throws IOException {
        String timestamp =new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        mDateTime=timestamp;
        String imageFileName = "JPEG_"+timestamp+"_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName,".jpg",storageDir);

        mCurrentPhotoPath=image.getAbsolutePath();

        return  image;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        fileSize();
        String timestamp=mDateTime;
        Background background=new Background();
        background.execute("Image",mId,mCurrentPhotoPath,timestamp,msLatitude,msLongitude);
        mImageView.setImageDrawable(Drawable.createFromPath(mCurrentPhotoPath));
    }

    private String getDateTime()  {
        return new SimpleDateFormat("dd-MM-YYYY/HH:mm:ss").format(new Date());
    }

    @Override
    public void onBackPressed() {

        if(sUsername!=null)
        {
            Intent startMain = new Intent(Intent.ACTION_MAIN);
            startMain.addCategory(Intent.CATEGORY_HOME);
            startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(startMain);
        }
    }
}
