package com.example.krishnapratap.automatepayroll;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class AdminHr extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;
    private Toolbar mToolbar;
    private SharedPreferences sharedPreferences;
    private String sUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_hr);


        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        mDrawerLayout = findViewById(R.id.admin_layout);
        mToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.open, R.string.close);
        mToggle.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View sidebar = navigationView.getHeaderView(0);

        TextView nameText = sidebar.findViewById(R.id.name);
        TextView emailText = sidebar.findViewById(R.id.email);
        ImageView imageView = sidebar.findViewById(R.id.imageView);

        sharedPreferences = getSharedPreferences("User_info", MODE_PRIVATE);

        sUsername = sharedPreferences.getString("username", "N/A");
        nameText.setText(sharedPreferences.getString("username", "N/A"));
        emailText.setText(sharedPreferences.getString("Email", "N/A"));

        DownloadImage downloadImage = new DownloadImage(imageView);
        downloadImage.execute(sharedPreferences.getString("Image", "N/A"));

    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        Fragment fragment = null;
        int id = item.getItemId();


        switch (id) {
            case R.id.nav_home:
                fragment = new Home();

                break;
            case R.id.nav_checksalary:
                fragment = new CheckSalary();
                break;
            case R.id.nav_checkemployee:
                fragment = new CheckEmployee();
                break;
            case R.id.nav_viewimage:
                fragment = new ViewImage();
                break;
            case R.id.nav_changepassword:
                fragment = new ChangePassword();

                break;
        }


        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();

            fragmentManager.beginTransaction().replace(R.id.change, fragment).commit();
        }


        DrawerLayout drawer = findViewById(R.id.admin_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.log_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (mToggle.onOptionsItemSelected(item)) {
            return true;
        }

        if (id == R.id.nav_logout) {
            Intent intent = new Intent(AdminHr.this, MainActivity.class);
            startActivity(intent);
            return true;

        }
        return super.onOptionsItemSelected(item);
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

    public class DownloadImage extends AsyncTask<String, Void, Bitmap> {

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
                bitmapFactory.inSampleSize = 9;
                InputStream inputStream = new URL(imageUrl).openStream();
                bitmap = BitmapFactory.decodeStream(inputStream, null, bitmapFactory);



            } catch (IOException e) {
                e.printStackTrace();
            }
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {


            RoundedBitmapDrawable roundedBitmapDrawable=RoundedBitmapDrawableFactory.create(getResources(),bitmap);
            roundedBitmapDrawable.setCircular(true);
            mImageView.setImageDrawable(roundedBitmapDrawable);
        }


    }
}
