package isep.fr.collegeinformationsystem.activity.admin;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;

import android.util.Log;
import android.view.GestureDetector;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import isep.fr.collegeinformationsystem.Constants;
import isep.fr.collegeinformationsystem.R;
import isep.fr.collegeinformationsystem.WebServiceRequest.GetAllStaffInfoService;
import isep.fr.collegeinformationsystem.WebServiceUtil.ServerResponseListener;
import isep.fr.collegeinformationsystem.activity.studentGuest.LogInActivity;
import isep.fr.collegeinformationsystem.adapter.ProfessorViewAdapter;
import isep.fr.collegeinformationsystem.database.AppSharedPreferences;
import isep.fr.collegeinformationsystem.interfaces.ItemClickListener;
import isep.fr.collegeinformationsystem.model.ProfessorModel;

public class AdminProfessorActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    private static final String TAG = "AdminProfessorActivity";
    public ProfessorViewAdapter professorViewAdapter;
    public ArrayList<ProfessorModel> professorModels = new ArrayList<>();
    RecyclerView recyclerView;
    String department;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_professor);
        //Toolbar initialise
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("ISEP STAFF");
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

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/

                Intent intent = new Intent(getApplicationContext(), InsertProfessorActivity.class);
                startActivity(intent);

            }
        });






        //inetialise recyclerview

        recyclerView = (RecyclerView) findViewById(R.id.professor_recyclerView);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);

        Bundle bundle = getIntent().getExtras();
        department = bundle.getString(Constants.DEPT_SHORT_NAME);

        //getAllStafData();

        professorViewAdapter = new ProfessorViewAdapter(this, professorModels);
        recyclerView.setAdapter(professorViewAdapter);


        itemClickListener();

    }

    @Override
    protected void onResume() {
        super.onResume();

        getAllStafData();
    }

    private void getAllStafData() {

        if(department!= null)
        {

            GetAllStaffInfoService getAllStaffInfoService = new GetAllStaffInfoService(this, "",new getAllStaffResponse());
            getAllStaffInfoService.GetAllStaffInfoService();



        }
    }

    private void itemClickListener() {

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(this,
                recyclerView, new ItemClickListener(){

            @Override
            public void onItemClick(View v, int pos) {

                ProfessorModel professorModel = professorViewAdapter.getItem(pos);

                Toast.makeText(getApplicationContext(),"clicked "+professorModel.getProfName(),Toast.LENGTH_SHORT).show();

                //show full details in dialoug box

            }

            @Override
            public void onItemLongClick(View v, int pos) {

            }
        }));
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

    }


    class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private ItemClickListener clicklistener;
        private GestureDetector gestureDetector;

        public RecyclerTouchListener(Context context, final RecyclerView recycleView, final ItemClickListener clicklistener) {

            this.clicklistener = clicklistener;
            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View child = recycleView.findChildViewUnder(e.getX(), e.getY());
                    if (child != null && clicklistener != null) {
                        clicklistener.onItemLongClick(child, recycleView.getChildAdapterPosition(child));
                    }
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && clicklistener != null && gestureDetector.onTouchEvent(e)) {
                clicklistener.onItemClick(child, rv.getChildAdapterPosition(child));
            }

            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {

        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }
    }


    private class getAllStaffResponse implements ServerResponseListener {

        @Override
        public void onResponseData(Object resultObj) {
            Log.d(TAG, "get all staff responce = " + resultObj.toString());

            try {
                JSONObject object = new JSONObject(resultObj.toString());

                String status = object.getString("status");


                if (status.equals("0")) {
                    professorModels.clear();

                    JSONArray jsonArray = object.getJSONArray(Constants.DATA);

                    if (jsonArray.length() != 0) {
                        for (int i = 0; i < jsonArray.length(); i++) {

                            JSONObject staffObj = jsonArray.getJSONObject(i);

                            String staffId = staffObj.getString(Constants.STAFF_ID);
                            String staffName = staffObj.getString(Constants.STAFF_NAME);
                            String staffMail = staffObj.getString(Constants.STAFF_MAIL_ID);
                            String staffQualify = staffObj.getString(Constants.STAFF_QUALIFICATION);
                            String staffDept = staffObj.getString(Constants.STAFF_DEPT);
                            String staffDealSub = staffObj.getString(Constants.STAFF_DEAL_SUB);
                            String staffMob = staffObj.getString(Constants.STAFF_MOBILE_NUM);
                            String staffGender = staffObj.getString(Constants.STAFF_GENDER);
                            String staffImage = staffObj.getString(Constants.STAFF_PROFILE);

                            if(staffDept.contains("\r\n\r\n")){
                                staffDept = staffDept.replace("\r\n\r\n","");
                            }

                            if(department != null && department.equals(staffDept)){
                                professorModels.add(new ProfessorModel(staffId, staffName, staffMail, staffQualify,staffDept, staffDealSub,  staffMob,staffGender, staffImage));

                            }



                        }

                        professorViewAdapter = new ProfessorViewAdapter(getApplicationContext(), professorModels);
                        recyclerView.setAdapter(professorViewAdapter);

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
    }


}