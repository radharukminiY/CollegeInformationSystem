package isep.fr.collegeinformationsystem.activity.admin;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.OpenableColumns;

import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;

import isep.fr.collegeinformationsystem.Constants;
import isep.fr.collegeinformationsystem.R;
import isep.fr.collegeinformationsystem.WebServiceRequest.CreateNewDepartment;
import isep.fr.collegeinformationsystem.WebServiceUtil.ServerResponseListener;
import isep.fr.collegeinformationsystem.activity.studentGuest.LogInActivity;
import isep.fr.collegeinformationsystem.database.AppSharedPreferences;
import isep.fr.collegeinformationsystem.interfaces.FileUtil;

public class InsertDepartmentActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    private static final String TAG = "InsertDepartment";

    EditText deptName, deptShortName, deptSubj;
    TextView deptFile;
    ImageView deptFileUpload;
    Button createDept;
    private int PICK_DOC = 1811;

    String docPath = null;

    private ProgressDialog progressDoalog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_department);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Create New Department");
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

        deptName = (EditText) findViewById(R.id.edt_admin_dept_name);
        deptShortName = (EditText) findViewById(R.id.edt_admin_dept_short_name);
        deptSubj = (EditText) findViewById(R.id.edt_admin_dept_subjects);
        deptFile = (TextView) findViewById(R.id.tv_admin_dept_file);

        deptFileUpload = (ImageView) findViewById(R.id.imv_admin_dept_file);
        createDept = (Button) findViewById(R.id.btn_admin_create_dept);


        deptFileUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                File file = new File(Environment.getExternalStorageDirectory(), Constants.DOWNLOADS_LC);
                Log.d(TAG, file.toString());
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType(Constants.APPLICATION_TYPE_PDF);
                startActivityForResult(intent, PICK_DOC);

            }
        });

        createDept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String deptNam = deptName.getText().toString();
                String deptShortNam = deptShortName.getText().toString();
                String deptSubje = deptSubj.getText().toString();
                String deptFilePath = deptFile.getText().toString();

                if (!deptNam.isEmpty() && !deptShortNam.isEmpty() && !deptSubje.isEmpty() && !deptFilePath.isEmpty()) {

                    progressDoalog = new ProgressDialog(InsertDepartmentActivity.this);
                    progressDoalog.setMessage("Please wait....");
                    progressDoalog.show();


                    CreateNewDepartment createNewDepartment = new CreateNewDepartment(getApplicationContext(), deptNam, deptShortNam, deptSubje, docPath, new createDeptResponce());
                    createNewDepartment.CreateNewDepartment();

                } else {
                    Toast.makeText(getApplicationContext(), "fill all fields", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {

            if (requestCode == PICK_DOC) {
                // Get the Uri of the selected file
                Uri uri = data.getData();
                Log.d(TAG, "select document uri - " + uri.toString());

                String uriString = uri.toString();
                File myFile = new File(uriString);
                String path = myFile.getAbsolutePath();
                String displayName = null;

                //docPath = uriString;

                try {
                    File file = FileUtil.from(InsertDepartmentActivity.this,uri);
                    Log.d("file", "File...:::: uti - "+file .getPath()+" file -" + file + " : " + file .exists());

                    docPath = file.getPath();

                } catch (IOException e) {
                    e.printStackTrace();
                }


                Log.d(TAG, "select document path - " + docPath);

                if (uriString.startsWith(Constants.CONTENT)) {
                    Cursor cursor = null;
                    try {
                        cursor = getContentResolver().query(uri, null, null, null, null);
                        if (cursor != null && cursor.moveToFirst()) {
                            displayName = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                            deptFile.setText(displayName);
                            Log.d(TAG, "displayname - " + displayName);
                        }
                    } finally {
                        assert cursor != null;
                        cursor.close();
                    }
                } else if (uriString.startsWith(Constants.FILE)) {
                    displayName = myFile.getName();

                    deptFile.setText(displayName);
                    Log.d(TAG, "displayname - " + displayName);
                }


            }
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

    private class createDeptResponce implements ServerResponseListener {

        @Override
        public void onResponseData(Object resultObj) {
            Log.d(TAG, "dept responce = " + resultObj.toString());

            try {
                JSONObject object = new JSONObject(resultObj.toString());

                String status = object.getString("status");

                progressDoalog.dismiss();
                if (status.equals("0")) {

                    finish();
                    onBackPressed();

                    Log.d(TAG, "create dept is success ");
                } else {
                    Toast.makeText(getApplicationContext(),"Failed Creat Department",Toast.LENGTH_SHORT).show();
                }


            } catch (Exception e) {
                progressDoalog.dismiss();
                Log.d(TAG, "getting string status  error - " + e.toString());
                Toast.makeText(getApplicationContext(),"Failed Creat Department",Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }


        }
    }
}