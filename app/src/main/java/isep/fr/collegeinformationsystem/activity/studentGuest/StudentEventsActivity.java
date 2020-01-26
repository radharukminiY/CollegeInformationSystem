package isep.fr.collegeinformationsystem.activity.studentGuest;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.navigation.NavigationView;

import isep.fr.collegeinformationsystem.Constants;
import isep.fr.collegeinformationsystem.R;
import isep.fr.collegeinformationsystem.WebServiceRequest.GetAllEventInfoService;
import isep.fr.collegeinformationsystem.WebServiceUtil.ConnectivityReceiver;
import isep.fr.collegeinformationsystem.WebServiceUtil.ServerResponseListener;
import isep.fr.collegeinformationsystem.activity.admin.AdminEventsActivity;
import isep.fr.collegeinformationsystem.adapter.StudentEventListAdapter;
import isep.fr.collegeinformationsystem.database.AppSharedPreferences;
import isep.fr.collegeinformationsystem.model.EventsModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class StudentEventsActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    private static final String TAG = "StudentEventsActivity";

    StudentEventListAdapter eventListAdapter;
    public ArrayList<EventsModel> eventsModels = new ArrayList<>();
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_college_events);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("ISEP EVENTS");
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


        //inetialise recyclerview

        recyclerView = (RecyclerView) findViewById(R.id.student_event_recyclerView);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);


    }

    @Override
    protected void onResume() {
        super.onResume();
        getAllEvents();
    }

    private void getAllEvents() {
        GetAllEventInfoService getAllEventInfoService = new GetAllEventInfoService(getApplicationContext(), "", new getAllEventsResponse());
        getAllEventInfoService.GetAllEventInfoService();
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            Intent intent = new Intent(this, StudentHomeActivity.class);
            startActivity(intent);
            // Handle the camera action
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
            Intent intent = new Intent(this, AdminEventsActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_map) {
            if (checkInternetConnection()) {
                Intent intent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse("https://www.google.com/maps/search/?api=1&query=48.824397,2.279804"));
                startActivity(intent);
            }

        } else if (id == R.id.nav_contactUs) {
            Intent intent = new Intent(this, ContactUsActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_logout) {
            clearUserData();
            Intent intent = new Intent(getApplicationContext(), LogInActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
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

    }

    public class getAllEventsResponse implements ServerResponseListener {

        @Override
        public void onResponseData(Object resultObj) {
            Log.d(TAG, "user responce = " + resultObj.toString());

            try {
                JSONObject object = new JSONObject(resultObj.toString());

                String status = object.getString("status");


                if (status.equals("0")) {

                    eventsModels.clear();
                    JSONArray jsonArray = object.getJSONArray(Constants.DATA);
                    if (jsonArray.length() != 0) {
                        for (int i = 0; i < jsonArray.length(); i++) {

                            JSONObject eventObj = jsonArray.getJSONObject(i);

                            String eventId = eventObj.getString(Constants.EVENT_ID);
                            String eventTitle = eventObj.getString(Constants.EVENT_TITLE);
                            String eventDateTime = eventObj.getString(Constants.EVENT_DATE_TIME);
                            String eventType = eventObj.getString(Constants.EVENT_TYPE);
                            String eventDisc = eventObj.getString(Constants.EVENT_DISCRIPTION);
                            String eventAddress = eventObj.getString(Constants.EVENT_PLACE);
                            String eventHall = eventObj.getString(Constants.EVENT_HALL);
                            String eventImage = eventObj.getString(Constants.EVENT_IMAGE);

                            eventsModels.add(new EventsModel(eventId, eventTitle, eventType, eventDateTime, eventDisc, eventImage, eventAddress, eventHall));


                        }


                        eventListAdapter = new StudentEventListAdapter(StudentEventsActivity.this, eventsModels);
                        recyclerView.setAdapter(eventListAdapter);


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
