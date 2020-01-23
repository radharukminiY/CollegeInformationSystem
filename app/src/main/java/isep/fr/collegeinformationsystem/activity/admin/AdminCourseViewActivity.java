package isep.fr.collegeinformationsystem.activity.admin;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.navigation.NavigationView;

import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import isep.fr.collegeinformationsystem.Constants;
import isep.fr.collegeinformationsystem.R;
import isep.fr.collegeinformationsystem.WebServiceRequest.GetAllDeptInfoService;
import isep.fr.collegeinformationsystem.WebServiceUtil.ServerResponseListener;
import isep.fr.collegeinformationsystem.activity.studentGuest.LogInActivity;
import isep.fr.collegeinformationsystem.adapter.AdminCourseViewAdapter;
import isep.fr.collegeinformationsystem.database.AppSharedPreferences;
import isep.fr.collegeinformationsystem.model.AdminCourseModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class AdminCourseViewActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    private static final String TAG = "AdminCourseView";


    public AdminCourseViewAdapter courseListAdapter;
    public ArrayList<AdminCourseModel> courses = new ArrayList<>();
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_course_view);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("ISEP Departments");
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

        /*RelativeLayout header = (RelativeLayout) headerView.findViewById(R.id.nav_profile_pic);
        header.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), AdminProfileActivity.class);
                startActivity(intent);

            }
        });*/


        //inetialise recyclerview

        recyclerView = (RecyclerView) findViewById(R.id.admin_dept_recyclerView);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);

        GetAllDeptInfoService getAllDeptInfoService = new GetAllDeptInfoService(getApplicationContext(), "", new getAllDeptResponse());
        getAllDeptInfoService.GetAllDeptInfoService();

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


    private class getAllDeptResponse implements ServerResponseListener {

        @Override
        public void onResponseData(Object resultObj) {
            Log.d(TAG, "dept responce = " + resultObj.toString());

            try {
                if (resultObj != null) {
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

                                courses.add(new AdminCourseModel(String.valueOf(deptId),deptName, deptShortName, deptSylDoc, deptSubjects));

                            }

                            courseListAdapter = new AdminCourseViewAdapter(getApplicationContext(), courses);
                            recyclerView.setAdapter(courseListAdapter);

                        } else {
                            // Nodata is getting please set no data in the list
                        }

                    } else {

                    }


                } else {
                    //enable user name to editable
//                    edtUserName.setFocusableInTouchMode(true);
                }

            } catch (Exception e) {
                Log.d(TAG, "getting string status  error - " + e.toString());
                e.printStackTrace();
            }


        }
    }
}