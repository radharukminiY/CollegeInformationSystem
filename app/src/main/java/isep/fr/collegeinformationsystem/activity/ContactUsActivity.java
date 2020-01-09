package isep.fr.collegeinformationsystem.activity;

import android.app.DownloadManager;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.core.view.GravityCompat;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import com.google.android.material.navigation.NavigationView;
import isep.fr.collegeinformationsystem.R;

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

            RelativeLayout header = (RelativeLayout) headerView.findViewById(R.id.nav_profile_pic);
            header.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(getApplicationContext(), StudentProfileActivity.class);
                    startActivity(intent);

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
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                // Handle the camera action
            } else if (id == R.id.nav_downloadPdf) {

                if (checkInternetConnection()) {
                    String fileUrl = "http://www.bmsb2019.org/file/BMSB2019_CFP_draft_190411-r1.pdf";
                    String fileName = "IEEE2020CFP.pdf";

                    DownloadManager.Request request = new DownloadManager.Request(Uri.parse(fileUrl));
                    request.setDescription("BMSB2019_CFP");
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
                if (checkInternetConnection()) {
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
                        String extraText = "Name : "+edtName.getText().toString() + "\n" + "Email : "+edtMail.getText().toString() + "\n" + "Phone No : " + edtPhone.getText().toString();

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
                        Toast.makeText(getApplicationContext(),"please fill all fields",Toast.LENGTH_SHORT).show();
                    }


                    break;
            }

        }
    }




