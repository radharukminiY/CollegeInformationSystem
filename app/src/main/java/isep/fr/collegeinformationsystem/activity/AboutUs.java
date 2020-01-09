package isep.fr.collegeinformationsystem.activity;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.core.view.GravityCompat;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import com.google.android.material.navigation.NavigationView;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import isep.fr.collegeinformationsystem.R;

public class AboutUs extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    private TextView tvAboutOne, tvAboutTwo, tvViewMore, tvViewMoreOne;

    boolean isCheck = true;
    boolean isChecked = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);

        isCheck = true;
        isChecked = true;

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("About Us");
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        navigationView.setItemIconTintList(null);
        navigationView.setNavigationItemSelectedListener(this);

        RelativeLayout header = (RelativeLayout) headerView.findViewById(R.id.nav_profile_pic);
        header.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), StudentProfileActivity.class);
                startActivity(intent);

            }
        });



        initializeViews();


    }

    private void initializeViews() {

        tvAboutOne = (TextView)findViewById(R.id.tv_about_para1);
        tvAboutTwo = (TextView)findViewById(R.id.tv_about_para2);

        tvViewMore = (TextView)findViewById(R.id.tv_AboutView);
        tvViewMoreOne = (TextView)findViewById(R.id.tv_AboutView1);


        //onclick liseasr
        tvViewMore.setOnClickListener(this);
        tvViewMoreOne.setOnClickListener(this);


    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            // Handle the camera action
        }  else if (id == R.id.nav_downloadPdf) {

            if(checkInternetConnection()){
                String fileUrl = "http://www.bmsb2019.org/file/BMSB2019_CFP_draft_190411-r1.pdf";
                String fileName = "IEEE2020CFP.pdf";

                DownloadManager.Request request = new DownloadManager.Request(Uri.parse(fileUrl));
                request.setDescription("Some descrition");
                request.setTitle("IEEE2020CFP.pdf");

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                    request.allowScanningByMediaScanner();
                    request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                }
                request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName);

                DownloadManager manager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
                manager.enqueue(request);
            }


        } else if (id == R.id.nav_impDates) {
            Intent intent = new Intent(this, EventsActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_map) {
            if(checkInternetConnection()) {
                Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                        Uri.parse("https://www.google.com/maps/search/?api=1&query=48.824397,2.279804"));
                startActivity(intent);
            }

        } else if (id == R.id.nav_contactUs) {
            Intent intent = new Intent(this, ContactUsActivity.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private boolean checkInternetConnection() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(getApplicationContext().CONNECTIVITY_SERVICE);
        // test for connection
        if (cm.getActiveNetworkInfo() != null
                && cm.getActiveNetworkInfo().isAvailable()
                && cm.getActiveNetworkInfo().isConnected()) {

            return true;
        } else {

            Toast.makeText(getApplicationContext(),"No internet connection",Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.tv_AboutView:
                if (isChecked) {
                    tvAboutOne.setMaxLines(11);
                    tvViewMore.setText("View Less ^");
                    isChecked = false;
                } else {
                    tvAboutOne.setMaxLines(3);
                    tvViewMore.setText("View More...");
                    isChecked = true;
                }
                break;
            case R.id.tv_AboutView1:

                if (isCheck) {
                    tvAboutTwo.setMaxLines(9);
                    tvViewMoreOne.setText("View Less ^");
                    isCheck = false;
                } else {
                    tvAboutTwo.setMaxLines(3);
                    tvViewMoreOne.setText("View More...");
                    isCheck = true;
                }

                break;

        }


    }
}
