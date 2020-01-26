package isep.fr.collegeinformationsystem.activity.admin;

import android.Manifest;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.smarteist.autoimageslider.IndicatorAnimations;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import java.util.ArrayList;

import isep.fr.collegeinformationsystem.R;
import isep.fr.collegeinformationsystem.activity.studentGuest.LogInActivity;
import isep.fr.collegeinformationsystem.adapter.HomeImageSliderAdapter;
import isep.fr.collegeinformationsystem.database.AppSharedPreferences;
import isep.fr.collegeinformationsystem.permissions.PermissionResultCallback;
import isep.fr.collegeinformationsystem.permissions.PermissionUtils;

public class AdminHomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener, ActivityCompat.OnRequestPermissionsResultCallback, PermissionResultCallback {

    private static final String TAG = "StudentHomeActivity";


    Button btnCourse, btnDepartment, banStudent, btnEvents;
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
        setContentView(R.layout.activity_admin_home);
        permissionUtils = new PermissionUtils(AdminHomeActivity.this);
        permissions.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        permissions.add(Manifest.permission.MANAGE_DOCUMENTS);
        //app need permissions
        Permissions();


        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("ADMIN HOME");
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

        TextView navText = (TextView) headerView.findViewById(R.id.nav_tv_username);
        navText.setText(AppSharedPreferences.getInstance().getUserName());

        /*RelativeLayout header = (RelativeLayout) headerView.findViewById(R.id.nav_profile_pic);
        header.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), AdminProfileActivity.class);
                startActivity(intent);

            }
        });*/

        initializeViews();

        assetManager = this.getAssets();


    }


    private void initializeViews() {

        tvDiscript = findViewById(R.id.tvReviewDescription);
        tvShow = findViewById(R.id.textView1);
        llDate = findViewById(R.id.ll_imp_dates);


        btnDepartment = findViewById(R.id.btn_dept);
        btnCourse = findViewById(R.id.btn_cours);
        banStudent = findViewById(R.id.btn_students);
        btnEvents = findViewById(R.id.btn_events);

        tvShow.setOnClickListener(this);

        btnDepartment.setOnClickListener(this);
        btnCourse.setOnClickListener(this);
        banStudent.setOnClickListener(this);
        btnEvents.setOnClickListener(this);

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
            //home navigation
//            startActivity(new Intent(getApplicationContext(), AdminHomeActivity.class));

        } else if (id == R.id.nav_dept) {

            //navigation depart
            startActivity(new Intent(getApplicationContext(), AdminDepartmentViewActivity.class));

        } else if (id == R.id.nav_cour) {

            startActivity(new Intent(getApplicationContext(), AdminCourseViewActivity.class));

        } else if (id == R.id.nav_student) {

            startActivity(new Intent(getApplicationContext(), AdminStudentViewActivity.class));

        } else if (id == R.id.nav_events) {

            startActivity(new Intent(getApplicationContext(), AdminEventsActivity.class));

        }else if (id == R.id.nav_logout) {

            Intent intent = new Intent(getApplicationContext(), LogInActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private boolean checkInternetConnection() {

        ConnectivityManager cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
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
                Intent intentThree = new Intent(getApplicationContext(), AdminDepartmentViewActivity.class);
                startActivity(intentThree);
                break;
            case R.id.btn_cours:
                Intent intentFour = new Intent(getApplicationContext(), AdminCourseViewActivity.class);
                startActivity(intentFour);
                break;
            case R.id.btn_students:
                Intent intentStu = new Intent(getApplicationContext(), AdminStudentViewActivity.class);
                startActivity(intentStu);
                break;
            case R.id.btn_events:
                Intent intentEvent = new Intent(getApplicationContext(), AdminEventsActivity.class);
                startActivity(intentEvent);
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
