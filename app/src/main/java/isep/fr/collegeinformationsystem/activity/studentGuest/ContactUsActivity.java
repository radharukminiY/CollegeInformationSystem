package isep.fr.collegeinformationsystem.activity.studentGuest;

import android.app.DownloadManager;
import android.content.ActivityNotFoundException;
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
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import isep.fr.collegeinformationsystem.Constants;
import isep.fr.collegeinformationsystem.R;
import isep.fr.collegeinformationsystem.WebServiceUtil.ConnectivityReceiver;
import isep.fr.collegeinformationsystem.activity.admin.AdminEventsActivity;
import isep.fr.collegeinformationsystem.database.AppSharedPreferences;

public class ContactUsActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    Button btnSend;
    EditText edtName, edtMail, edtPhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Contact Us");
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

        if(!AppSharedPreferences.getInstance().getUserType().equals("student")){
            Menu nav_Menu = navigationView.getMenu();
            nav_Menu.findItem(R.id.nav_events).setVisible(false);
        }

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


        btnSend = (Button) findViewById(R.id.btn_send);
        edtName = (EditText) findViewById(R.id.contact_edt_name);
        edtMail = (EditText) findViewById(R.id.contact_edt_email);
        edtPhone = (EditText) findViewById(R.id.contact_edt_phone);

        edtName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        edtMail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        edtPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        btnSend.setOnClickListener(this);
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
        switch (v.getId()) {

            case R.id.btn_send:

                if (edtName.length() != 0 &&
                        edtMail.length() != 0 &&
                        edtPhone.length() != 0) {
                    String extraText = "Name : " + edtName.getText().toString() + "\n" + "Email : " + edtMail.getText().toString() + "\n" + "Phone No : " + edtPhone.getText().toString();

                    try {
                        Intent intent = new Intent(Intent.ACTION_SEND);
                        String[] recipients = {"isep@isep.fr"};
                        intent.putExtra(Intent.EXTRA_EMAIL, recipients);
                        intent.putExtra(Intent.EXTRA_SUBJECT, "ISEP College Info App Contact");
                        intent.putExtra(Intent.EXTRA_TEXT, extraText);
                        intent.setType("text/html");
                        intent.setPackage("com.google.android.gm");
                        startActivity(Intent.createChooser(intent, "Send mail"));
                    } catch (ActivityNotFoundException e) {
                        //TODO smth
                    }

                } else {
                    Toast.makeText(getApplicationContext(), "please fill all fields", Toast.LENGTH_SHORT).show();
                }


                break;
        }

    }
}
