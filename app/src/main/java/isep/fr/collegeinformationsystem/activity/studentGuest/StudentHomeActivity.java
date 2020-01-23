package isep.fr.collegeinformationsystem.activity.studentGuest;

import android.Manifest;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import isep.fr.collegeinformationsystem.Constants;
import isep.fr.collegeinformationsystem.R;
import isep.fr.collegeinformationsystem.WebServiceUtil.ConnectivityReceiver;
import isep.fr.collegeinformationsystem.activity.admin.AdminEventsActivity;
import isep.fr.collegeinformationsystem.adapter.HomeImageSliderAdapter;
import isep.fr.collegeinformationsystem.database.AppSharedPreferences;
import isep.fr.collegeinformationsystem.permissions.PermissionResultCallback;
import isep.fr.collegeinformationsystem.permissions.PermissionUtils;
import com.google.android.material.navigation.NavigationView;
import com.smarteist.autoimageslider.IndicatorAnimations;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import java.util.ArrayList;

public class StudentHomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener, ActivityCompat.OnRequestPermissionsResultCallback, PermissionResultCallback {

    private static final String TAG = "StudentHomeActivity";


    Button btnDepartment, btnCourse, banVochers, btnEvents, btnContactUs;
    TextView tvDiscript, tvShow;
    LinearLayout llDate;
    ImageView imbDate;
    AssetManager assetManager;
    boolean isCheck = true;
    boolean imbIsChecked = true;
    ArrayList<String> permissions = new ArrayList<>();
    PermissionUtils permissionUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        permissionUtils = new PermissionUtils(StudentHomeActivity.this);
        permissions.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        permissions.add(Manifest.permission.MANAGE_DOCUMENTS);
        //app need permissions
        Permissions();


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        navigationView.setItemIconTintList(null);
        navigationView.setNavigationItemSelectedListener(this);

        if (!AppSharedPreferences.getInstance().getUserType().equals("student")) {
            Menu nav_Menu = navigationView.getMenu();
            nav_Menu.findItem(R.id.nav_events).setVisible(false);
        }

        TextView navText = (TextView) headerView.findViewById(R.id.nav_tv_username);
        navText.setText(AppSharedPreferences.getInstance().getUserName());

        RelativeLayout header = (RelativeLayout) headerView.findViewById(R.id.nav_profile_pic);
        header.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (AppSharedPreferences.getInstance().getUserType().equals("student")) {
                    Intent intent = new Intent(getApplicationContext(), StudentProfileActivity.class);
                    startActivity(intent);
                }


            }
        });

        initializeViews();

        assetManager = this.getAssets();


    }


    private void initializeViews() {

        tvDiscript = findViewById(R.id.tvReviewDescription);
        tvShow = findViewById(R.id.textView1);
        llDate = findViewById(R.id.ll_imp_dates);


        btnDepartment = findViewById(R.id.btn_dept);
        btnCourse = findViewById(R.id.btn_cours);
        banVochers = findViewById(R.id.btn_vocher);
        btnEvents = findViewById(R.id.btn_events);
        btnContactUs = findViewById(R.id.btn_contact);

        tvShow.setOnClickListener(this);

        btnDepartment.setOnClickListener(this);
        btnCourse.setOnClickListener(this);
        banVochers.setOnClickListener(this);
        btnEvents.setOnClickListener(this);
        btnContactUs.setOnClickListener(this);


        if (!AppSharedPreferences.getInstance().getUserType().equals("student")) {
            btnEvents.setVisibility(View.GONE);
        }


        SliderView sliderView = findViewById(R.id.imageSlider);

        HomeImageSliderAdapter adapter = new HomeImageSliderAdapter(this);

        sliderView.setSliderAdapter(adapter);

        sliderView.setIndicatorAnimation(IndicatorAnimations.WORM); //set indicator animation by using SliderLayout.IndicatorAnimations. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
        sliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
        sliderView.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH);
        sliderView.setIndicatorSelectedColor(Color.WHITE);
        sliderView.setIndicatorUnselectedColor(Color.GRAY);
        sliderView.setScrollTimeInSec(4); //set scroll delay in seconds :
        sliderView.startAutoCycle();

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {

        } else if (id == R.id.nav_downloadPdf) {

            if (ConnectivityReceiver.isConnected()) {


                String fileUrl = Constants.SYL_MAIN_DOC_DOWNLOAD_URL;

                String fileName = "IsepVouchers.pdf";

                DownloadManager.Request request = new DownloadManager.Request(Uri.parse(fileUrl));
                request.setDescription("Some descrition");
                request.setTitle(fileName);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                    request.allowScanningByMediaScanner();
                    request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                }
                request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName);

                DownloadManager manager = (DownloadManager) this.getSystemService(Context.DOWNLOAD_SERVICE);
                manager.enqueue(request);
            } else {
                Toast.makeText(getApplicationContext(), "please check your network", Toast.LENGTH_SHORT).show();
            }


        } else if (id == R.id.nav_events) {
            Intent intent = new Intent(getApplicationContext(), AdminEventsActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_map) {
            if (checkInternetConnection()) {
                Intent intent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse("https://www.google.com/maps/search/?api=1&query=48.824397,2.279804"));
                startActivity(intent);
            }

        } else if (id == R.id.nav_contactUs) {

            Intent intent = new Intent(getApplicationContext(), ContactUsActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_logout) {
            clearUserData();
            Intent intent = new Intent(getApplicationContext(), LogInActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    private void clearUserData() {
        AppSharedPreferences.getInstance().setUserId("");
        AppSharedPreferences.getInstance().setUserName("");
        AppSharedPreferences.getInstance().setUserMail("");
        AppSharedPreferences.getInstance().setUserCourse("");
        AppSharedPreferences.getInstance().setUserGender("");
        AppSharedPreferences.getInstance().setUserType("");
        AppSharedPreferences.getInstance().setUserMobile("");
        AppSharedPreferences.getInstance().setUserProfile("");
    }

    private boolean checkInternetConnection() {

        ConnectivityManager cm = (ConnectivityManager) getSystemService(getApplicationContext().CONNECTIVITY_SERVICE);
        // test for connection
        if (cm.getActiveNetworkInfo() != null
                && cm.getActiveNetworkInfo().isAvailable()
                && cm.getActiveNetworkInfo().isConnected()) {

            return true;
        } else {


            Toast.makeText(getApplicationContext(), "No internet connection", Toast.LENGTH_SHORT).show();
            return false;
        }

    }


    @Override
    public void onClick(View v) {


        switch (v.getId()) {

            case R.id.textView1:
                if (isCheck) {
                    tvDiscript.setMaxLines(14);
                    tvShow.setText("View Less ^");
                    isCheck = false;
                } else {
                    tvDiscript.setMaxLines(3);
                    tvShow.setText("View More...");
                    isCheck = true;
                }
                break;
            case R.id.btn_dept:
                Intent intentThree = new Intent(getApplicationContext(), StudentDepartmentActivity.class);
                startActivity(intentThree);
                break;
            case R.id.btn_cours:
                Intent intentFour = new Intent(getApplicationContext(), StudentCourseActivity.class);
                startActivity(intentFour);
                break;
            case R.id.btn_events:
                Intent intentEvent = new Intent(getApplicationContext(), StudentEventsActivity.class);
                startActivity(intentEvent);
                break;
            case R.id.btn_vocher:

                if (ConnectivityReceiver.isConnected()) {


                    String fileUrl = Constants.SYL_MAIN_DOC_DOWNLOAD_URL;

                    String fileName = "IsepVouchers.pdf";

                    DownloadManager.Request request = new DownloadManager.Request(Uri.parse(fileUrl));
                    request.setDescription("Some descrition");
                    request.setTitle(fileName);

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                        request.allowScanningByMediaScanner();
                        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                    }
                    request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName);

                    DownloadManager manager = (DownloadManager) this.getSystemService(Context.DOWNLOAD_SERVICE);
                    manager.enqueue(request);
                } else {
                    Toast.makeText(getApplicationContext(), "please check your network", Toast.LENGTH_SHORT).show();
                }

                break;
            case R.id.btn_contact:
                Intent intentSix = new Intent(getApplicationContext(), ContactUsActivity.class);
                startActivity(intentSix);
                break;
        }

    }

    private void Permissions() {
        permissionUtils.check_permission(permissions, "App needs permissions", 43);
    }

    @Override

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        // redirects to utils
        permissionUtils.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void PermissionGranted(int request_code) {
        Log.d(TAG, "granted");

    }

    @Override
    public void PartialPermissionGranted(int request_code, ArrayList<String> granted_permissions) {

    }

    @Override
    public void PermissionDenied(int request_code) {

    }

    @Override
    public void NeverAskAgain(int request_code) {

    }


}
