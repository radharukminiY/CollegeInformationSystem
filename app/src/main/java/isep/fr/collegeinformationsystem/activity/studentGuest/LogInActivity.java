package isep.fr.collegeinformationsystem.activity.studentGuest;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import isep.fr.collegeinformationsystem.R;
import isep.fr.collegeinformationsystem.WebServiceRequest.LogInCheck;
import isep.fr.collegeinformationsystem.WebServiceUtil.ConnectivityReceiver;
import isep.fr.collegeinformationsystem.WebServiceUtil.ServerResponseListener;
import isep.fr.collegeinformationsystem.activity.admin.AdminHomeActivity;
import isep.fr.collegeinformationsystem.database.AppSharedPreferences;

public class LogInActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "LogInActivity";

    private ImageView roundUserIcon;
    private EditText username, userPass;
    private ImageView showPass;
    private TextView forgotPass, gustLogIn;
    private Button logIn;

    private boolean show = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        //set user/admin profile pic
        roundUserIcon = (ImageView) findViewById(R.id.profile_image);
        username = (EditText) findViewById(R.id.user_edt_username);
        userPass = (EditText) findViewById(R.id.user_edt_pass);
        showPass = (ImageView) findViewById(R.id.imv_show_pass);

        forgotPass = (TextView) findViewById(R.id.tv_forgetPass);
        logIn = (Button) findViewById(R.id.btn_login);
        gustLogIn = (TextView) findViewById(R.id.tv_guest);

        showPass.setOnClickListener(this);
        forgotPass.setOnClickListener(this);
        logIn.setOnClickListener(this);
        gustLogIn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imv_show_pass:
                if (show) {
                    userPass.setTransformationMethod(new PasswordTransformationMethod());
                    if (userPass.length() != 0) {
                        userPass.setSelection(userPass.length());
                    }
                    show = false;
                } else {

                    userPass.setTransformationMethod(null);
                    if (userPass.length() != 0) {
                        userPass.setSelection(userPass.length());
                    }
                    show = true;
                }
                Toast.makeText(getApplicationContext(), "show clicked ", Toast.LENGTH_SHORT).show();
                break;
            case R.id.tv_forgetPass:
                Toast.makeText(getApplicationContext(), "forgot clicked ", Toast.LENGTH_SHORT).show();
                break;
            case R.id.btn_login:
                /*if(username.getText().toString() != null && !username.getText().toString().isEmpty()){

                }else{

                }*/
                String userName = username.getText().toString();
                String userPassword = userPass.getText().toString();

                if(ConnectivityReceiver.isConnected()){
                    loginCredentials(userName, userPassword);
                }else{
                    Toast.makeText(getApplicationContext(), "please connect internet ", Toast.LENGTH_SHORT).show();
                }


                break;

            case R.id.tv_guest:

                if(ConnectivityReceiver.isConnected()){
                    loginCredentials("Guest", "guest");
                }else{
                    Toast.makeText(getApplicationContext(), "please connect internet ", Toast.LENGTH_SHORT).show();
                }
                break;

        }
    }

    private void loginCredentials(String userName, String userPassword) {

        //calling login service
        LogInCheck logInCheck = new LogInCheck(getApplicationContext(), userName, userPassword, new logInCheckResponse());
        logInCheck.LogInCheck();
    }

    private class logInCheckResponse implements ServerResponseListener {

        @Override
        public void onResponseData(Object resultObj) {
            Log.d(TAG, "user responce = " + resultObj.toString());

            try {
                if (resultObj != null) {
                    JSONObject object = new JSONObject(resultObj.toString());

                    String status = object.getString("status");
                    String jsonObj = object.getString("data");


                    Log.d(TAG, "print another String" + jsonObj);
                    JSONObject res_obj = new JSONObject(jsonObj);

                    if(status.equals("0")){

                        /*String userId = res_obj.getString(Constants.USER_ID);
                        String userName = res_obj.getString(Constants.USER_NAME);
                        String userMail = res_obj.getString(Constants.USER_MAIL_ID);
                        String userCourse = res_obj.getString(Constants.USER_COURSE);
                        String userType = res_obj.getString(Constants.USER_TYPE);
                        String userGender = res_obj.getString(Constants.USER_GENDER);
                        String userMobile = res_obj.getString(Constants.USER_MOBILE_NUM);
                        String userProfile = res_obj.getString(Constants.USER_PROFILE); */
                        String userType = null;
                        userType = AppSharedPreferences.getInstance().getUserType();

                        if(userType != null && !userType.isEmpty()) {
                            if (userType.equals("admin")) {
                                Intent intentFive = new Intent(getApplicationContext(), AdminHomeActivity.class);
                                intentFive.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                startActivity(intentFive);
                                finish();
                            } else {
                                Intent i = new Intent(getApplicationContext(), StudentHomeActivity.class);
                                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                startActivity(i);
                                finish();

                            }

                        }else{

                        }

                    }else{


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
