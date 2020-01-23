package isep.fr.collegeinformationsystem.activity.admin;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;

import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.navigation.NavigationView;


import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import isep.fr.collegeinformationsystem.Constants;
import isep.fr.collegeinformationsystem.R;
import isep.fr.collegeinformationsystem.WebServiceRequest.GetAllDeptInfoService;
import isep.fr.collegeinformationsystem.WebServiceRequest.UpdateStaffInfoService;
import isep.fr.collegeinformationsystem.WebServiceUtil.ServerResponseListener;
import isep.fr.collegeinformationsystem.activity.studentGuest.LogInActivity;
import isep.fr.collegeinformationsystem.database.AppSharedPreferences;
import isep.fr.collegeinformationsystem.interfaces.CircleTransform;
import isep.fr.collegeinformationsystem.interfaces.FileUtil;

public class AdminStaffUpdateActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    private static final String TAG = "InsertProfessor";

    ImageView staffProfView, staffProfFile;
    EditText staffName, staffMail, staffMobile, staffDealSubj, staffQualify;
    Spinner staffGender, staffDept;
    Button staffCreate;
    private int PICK_GALLERY = 1708;
    List<String> course;
    String userProfilrImage = null;
    private ProgressDialog progressDoalog;

    String userSelectDept = null;


    String uId = null;
    String uName = null;
    String uMail = null;
    String uMobil = null;
    String subj = null;
    String uGender = null;
    String uQual = null;
    String uDept = null;
    String uProfilePath = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_staff_update);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Update Staff Info");
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

        TextView navText = (TextView) headerView.findViewById(R.id.nav_tv_username);
        navText.setText(AppSharedPreferences.getInstance().getUserName());

       /* RelativeLayout header = (RelativeLayout) headerView.findViewById(R.id.nav_profile_pic);
        header.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), AdminProfileActivity.class);
                startActivity(intent);

            }
        });*/

        course = new ArrayList<>();
        course.add("Select Department");

        staffProfView = (ImageView) findViewById(R.id.imgProfile);
        staffProfFile = (ImageView) findViewById(R.id.p2p_enable_indicator);

        staffName = (EditText) findViewById(R.id.staff_edt_name);
        staffMail = (EditText) findViewById(R.id.staff_edt_mail);
        staffMobile = (EditText) findViewById(R.id.staff_edt_mob);
        staffQualify = (EditText) findViewById(R.id.staff_edt_qual);
        staffDealSubj = (EditText) findViewById(R.id.staff_edt_subj);

        staffGender = (Spinner) findViewById(R.id.staff_spin_gender);
        staffDept = (Spinner) findViewById(R.id.staff_spin_dept);

        staffCreate = (Button) findViewById(R.id.btn_create_staff);

        Intent valuesIntent = getIntent();
        Bundle extraBundle = valuesIntent.getExtras();

        if (extraBundle != null) {


            String urId = extraBundle.getString(Constants.STAFF_ID);
            String urName = extraBundle.getString(Constants.STAFF_NAME);
            String urMail = extraBundle.getString(Constants.STAFF_MAIL_ID);
            String urMob = extraBundle.getString(Constants.STAFF_MOBILE_NUM);
            String urSubj = extraBundle.getString(Constants.STAFF_DEAL_SUB);
            String urGen = extraBundle.getString(Constants.STAFF_GENDER);
            String urDept = extraBundle.getString(Constants.STAFF_DEPT);
            String urQual = extraBundle.getString(Constants.STAFF_QUALIFICATION);
            String urProf = extraBundle.getString(Constants.STAFF_PROFILE);

            uId = urId;


            staffName.setText(urName);
            staffMail.setText(urMail);
            staffMobile.setText(urMob);
            staffQualify.setText(urQual);
            staffDealSubj.setText(urSubj);

           /* staffGender = (Spinner) findViewById(R.id.staff_spin_gender);
            staffDept = (Spinner) findViewById(R.id.staff_spin_dept);*/


            String imgUrl = Constants.PROFILE_STAFF_IMG_URL + urProf;

            Log.d(TAG, "user pro url " + imgUrl);

            userProfilrImage = imgUrl;
            Glide.with(AdminStaffUpdateActivity.this)
                    .load(imgUrl)
                    .apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.DATA)
                            .transform(new CircleTransform(this)))
                    .into(staffProfView);


        }


        getAllDepartmentData();


        staffProfFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getImageFromGallery();
            }
        });


        staffDept.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                userSelectDept = course.get(i);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        staffCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (checkInternetConnection()) {

                    uName = staffName.getText().toString();
                    uMail = staffMail.getText().toString();
                    uMobil = staffMobile.getText().toString();
                    uQual = staffQualify.getText().toString();
                    subj = staffDealSubj.getText().toString();


                    uGender = staffGender.getSelectedItem().toString();


                    uDept = userSelectDept;
                    uProfilePath = userProfilrImage;

                    Log.d(TAG,"user selected depart "+uDept);


                    if (uId!=null && !uId.isEmpty() && uName != null && !uName.isEmpty() && uMail != null && !uMail.isEmpty() && uMobil != null && !uMobil.isEmpty() &&
                            subj != null && !subj.isEmpty() && uQual != null && !uQual.isEmpty() && uGender != null && !uGender.isEmpty() && !uGender.equals("Select Gender") && uDept != null && !uDept.isEmpty() && !uDept.equals("Select Course") && uProfilePath != null && !uProfilePath.isEmpty()) {

                        progressDoalog = new ProgressDialog(AdminStaffUpdateActivity.this);
                        progressDoalog.setMessage("Please wait....");
                        progressDoalog.show();

                        UpdateStaffInfoService updateStaffInfoService = new UpdateStaffInfoService(getApplicationContext(),uId, uName, uMail, uQual, subj, uMobil, uGender, uProfilePath, uDept, new updateStaffResponse());
                        updateStaffInfoService.UpdateStaffInfoService();

                    } else {
                        if (uProfilePath == null) {
                            Toast.makeText(getApplicationContext(), "please select image from gallery ", Toast.LENGTH_SHORT).show();
                        } else if (uGender.equals("Select Gender")) {
                            Toast.makeText(getApplicationContext(), "please select Gender ", Toast.LENGTH_SHORT).show();
                        } else if (uDept.equals("Select Course")) {
                            Toast.makeText(getApplicationContext(), "please select Department ", Toast.LENGTH_SHORT).show();
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


    private void getAllDepartmentData() {
        GetAllDeptInfoService getAllDeptInfoService = new GetAllDeptInfoService(getApplicationContext(), "", new getAllDeptResponse());
        getAllDeptInfoService.GetAllDeptInfoService();
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

            Glide.with(AdminStaffUpdateActivity.this)
                    .load(Uri.fromFile(new File(userProfilrImage)))
                    .apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.DATA)
                            .transform(new CircleTransform(this)))
                    .into(staffProfView);


        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            //home navigation
            startActivity(new Intent(getApplicationContext(), AdminHomeActivity.class));

        } else if (id == R.id.nav_dept) {

            //navigation depart
            startActivity(new Intent(getApplicationContext(), AdminDepartmentViewActivity.class));

        } else if (id == R.id.nav_cour) {

            startActivity(new Intent(getApplicationContext(), AdminCourseViewActivity.class));

        } else if (id == R.id.nav_student) {

            startActivity(new Intent(getApplicationContext(), AdminStudentViewActivity.class));

        } else if (id == R.id.nav_events) {

            startActivity(new Intent(getApplicationContext(), AdminEventsActivity.class));

        } else if (id == R.id.nav_logout) {

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

    }


    public class getAllDeptResponse implements ServerResponseListener {

        @Override
        public void onResponseData(Object resultObj) {
            Log.d(TAG, "user responce = " + resultObj.toString());

            try {
                JSONObject object = new JSONObject(resultObj.toString());

                String status = object.getString("status");


                if (status.equals("0")) {
                    JSONArray jsonArray = object.getJSONArray(Constants.DATA);

                    if (jsonArray.length() != 0) {
                        for (int i = 0; i < jsonArray.length(); i++) {

                            JSONObject deptObj = jsonArray.getJSONObject(i);

                            //String deptId = staffObj.getString(Constants.DEP);
                            int deptId = deptObj.getInt(Constants.DEPT_ID);
                            String deptName = deptObj.getString(Constants.DEPT_NAME);
                            String deptShortName = deptObj.getString(Constants.DEPT_SHORT_NAME);
                            String deptSubjects = deptObj.getString(Constants.DEPT_SUBJECTS);
                            String deptSylDoc = deptObj.getString(Constants.DEPT_SYLL_DOC);

                            course.add(deptShortName);

                        }

                        Log.d(TAG, "spinner values " + course.toString());

                        //String[] strCourse = GetStringArray(course);

                        ArrayAdapter<String> course_adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, course);
                        course_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        staffDept.setAdapter(course_adapter);


                    } else {
                        // Nodata is getting please set no data in the list
                    }

                } else {

                }


            } catch (Exception e) {
                Log.d(TAG, "getting string status  error - " + e.toString());
                e.printStackTrace();
            }


        }

        private String[] GetStringArray(ArrayList<String> arr) {

            // declaration and initialise String Array
            String str[] = new String[arr.size()];

            // ArrayList to Array Conversion
            for (int j = 0; j < arr.size(); j++) {

                // Assign each value to String array
                str[j] = arr.get(j);
            }

            return str;
        }
    }

    private class updateStaffResponse implements ServerResponseListener {

        @Override
        public void onResponseData(Object resultObj) {
            Log.d(TAG, "update staff response = " + resultObj.toString());

            try {
                JSONObject object = new JSONObject(resultObj.toString());

                String status = object.getString("status");

                progressDoalog.dismiss();
                if (status.equals("0")) {

                    finish();
                    onBackPressed();

                    Log.d(TAG, "update staff is success ");
                } else {
                    Toast.makeText(getApplicationContext(), "Failed update staff", Toast.LENGTH_SHORT).show();
                }


            } catch (Exception e) {
                progressDoalog.dismiss();
                Log.d(TAG, "getting string status  error - " + e.toString());
                Toast.makeText(getApplicationContext(), "Failed update staff", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }


        }
    }
}

