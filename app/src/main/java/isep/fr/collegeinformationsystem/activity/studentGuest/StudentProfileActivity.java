package isep.fr.collegeinformationsystem.activity.studentGuest;

import android.app.Activity;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;

import isep.fr.collegeinformationsystem.Constants;
import isep.fr.collegeinformationsystem.R;
import isep.fr.collegeinformationsystem.WebServiceRequest.UpdateStudentInfoService;
import isep.fr.collegeinformationsystem.WebServiceUtil.ConnectivityReceiver;
import isep.fr.collegeinformationsystem.WebServiceUtil.ServerResponseListener;
import isep.fr.collegeinformationsystem.activity.admin.AdminEventsActivity;
import isep.fr.collegeinformationsystem.database.AppSharedPreferences;
import isep.fr.collegeinformationsystem.interfaces.CircleTransform;
import isep.fr.collegeinformationsystem.interfaces.FileUtil;

public class StudentProfileActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {
    private static final String TAG = "StudentProfile";

    EditText userMobile, userMail, userPass;
    ImageView userProfile, userProEdit;

    TextView userId, userName, userCourse;
    Spinner userGender;

    private int PICK_GALLERY = 1708;

    Button updateStudent;

    String userProfilrImage = null;


    String uId = null;
    String uName = null;
    String uMail = null;
    String uMobil = null;
    String pass = null;
    String uGender = null;
    String uDept = null;
    String uProfilePath = null;

    private ProgressDialog progressDoalog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_profile);


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

        if(!AppSharedPreferences.getInstance().getUserType().equals("student")){
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


        userId = (TextView) findViewById(R.id.user_id);
        userName = (TextView) findViewById(R.id.user_name);
        userMail = (EditText) findViewById(R.id.user_edt_mail);
        userMobile = (EditText) findViewById(R.id.user_edt_phone);
        userPass = (EditText) findViewById(R.id.user_edt_pass);
        userProfile = (ImageView) findViewById(R.id.imgProfile);
        userProEdit = (ImageView) findViewById(R.id.p2p_enable_indicator);
        userGender = (Spinner) findViewById(R.id.user_spin_gender);
        userCourse = (TextView) findViewById(R.id.user_dept);
        updateStudent = (Button) findViewById(R.id.btn_admin_create_student);


        String profileLoad = Constants.PROFILE_IMG_URL + AppSharedPreferences.getInstance().getUserProfile();

        userProfilrImage = profileLoad;

        Glide.with(StudentProfileActivity.this)
                .load(Uri.fromFile(new File(userProfilrImage)))
                .apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.DATA)
                        .transform(new CircleTransform(this)))
                .into(userProfile);


        userId.setText(AppSharedPreferences.getInstance().getUserId());
        userName.setText(AppSharedPreferences.getInstance().getUserName());
        userMail.setText(AppSharedPreferences.getInstance().getUserMail());
        userMobile.setText(AppSharedPreferences.getInstance().getUserMobile());
        userPass.setText(AppSharedPreferences.getInstance().getUserPassword());
        userCourse.setText(AppSharedPreferences.getInstance().getUserCourse());


        userProEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getImageFromGallery();
            }
        });


        updateStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkInternetConnection()) {

                    uId = userId.getText().toString();
                    uName = userName.getText().toString();
                    uMail = userMail.getText().toString();
                    uMobil = userMobile.getText().toString();
                    pass = userPass.getText().toString();
                    uGender = userGender.getSelectedItem().toString();
                    uDept = userCourse.getText().toString();
                    uProfilePath = userProfilrImage;


                    if (uId != null && !uId.isEmpty() && uName != null && !uName.isEmpty() && uMail != null && !uMail.isEmpty() && uMobil != null && !uMobil.isEmpty() &&
                            pass != null && !pass.isEmpty() && uGender != null && !uGender.isEmpty() && !uGender.equals("Select Gender") && uDept != null && !uDept.isEmpty() && !uDept.equals("Select Course") && uProfilePath != null && !uProfilePath.isEmpty()) {

                        progressDoalog = new ProgressDialog(StudentProfileActivity.this);
                        progressDoalog.setMessage("Please wait....");
                        progressDoalog.show();

                        UpdateStudentInfoService updateStudentInfoService = new UpdateStudentInfoService(getApplicationContext(), uId, uName, uMail, pass, "student", uMobil, uGender, uProfilePath, uDept, new updateStudentResponse());
                        updateStudentInfoService.UpdateStudentInfoService();

                    } else {
                        if (uProfilePath == null) {
                            Toast.makeText(getApplicationContext(), "please select image from gallery ", Toast.LENGTH_SHORT).show();
                        } else if (uGender.equals("Select Gender")) {
                            Toast.makeText(getApplicationContext(), "please select Gender ", Toast.LENGTH_SHORT).show();
                        } else if (uDept.equals("Select Course")) {
                            Toast.makeText(getApplicationContext(), "please select Course ", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getApplicationContext(), "please fill all fields ", Toast.LENGTH_SHORT).show();
                        }

                    }


                } else {
                    Toast.makeText(getApplicationContext(), "please check internet connection", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    private void getImageFromGallery() {

        Intent imageIntent = new Intent(Intent.ACTION_PICK);
        imageIntent.setDataAndType(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI, Constants.IMAGE_TYPE);
        startActivityForResult(imageIntent, PICK_GALLERY);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {

            if (requestCode == PICK_GALLERY) {

                onSelectFromGalleryResult(data);

            }
        }
    }

    private void onSelectFromGalleryResult(Intent data) {

        Uri selectedImage = data.getData();
        File filePath = null;
        try {
            filePath = FileUtil.from(getApplicationContext(), selectedImage);
            userProfilrImage = filePath.getPath();

            Glide.with(StudentProfileActivity.this)
                    .load(Uri.fromFile(new File(userProfilrImage)))
                    .apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.DATA)
                            .transform(new CircleTransform(this)))
                    .into(userProfile);


        } catch (IOException e) {
            e.printStackTrace();
        }


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
    public void onClick(View view) {

    }

    private class updateStudentResponse implements ServerResponseListener {

        @Override
        public void onResponseData(Object resultObj) {
            Log.d(TAG, "update student response = " + resultObj.toString());

            try {
                JSONObject object = new JSONObject(resultObj.toString());

                String status = object.getString("status");

                progressDoalog.dismiss();
                if (status.equals("0")) {

                    finish();
                    onBackPressed();

                    Log.d(TAG, "update student is success ");
                } else {
                    Toast.makeText(getApplicationContext(), "Failed update student", Toast.LENGTH_SHORT).show();
                }


            } catch (Exception e) {
                progressDoalog.dismiss();
                Log.d(TAG, "getting string status  error - " + e.toString());
                Toast.makeText(getApplicationContext(), "Failed Creat Department", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }


        }
    }
}
