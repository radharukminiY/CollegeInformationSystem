package isep.fr.collegeinformationsystem.WebServiceRequest;

import android.content.Context;
import android.os.StrictMode;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import isep.fr.collegeinformationsystem.Constants;
import isep.fr.collegeinformationsystem.database.AppSharedPreferences;
import isep.fr.collegeinformationsystem.model.StudentModel;
import isep.fr.collegeinformationsystem.WebServiceUtil.ServerResponseListener;
import isep.fr.collegeinformationsystem.WebServiceUtil.VolleySigleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class LogInCheck {

    private static final String TAG = "LogInCheck";
    Context mContext;
    private ServerResponseListener mServerResponseListener = null;
    private StudentModel mRegisterDetailsResponseData;

    private String username;
    private String password;

    public LogInCheck(Context context,final String username, final String password, ServerResponseListener aServerResponseListener) {

        mContext = context;
        this.username = username;
        this.password = password;
        this.mServerResponseListener = aServerResponseListener;
    }

    public void LogInCheck() {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                Constants.LOGIN_USER,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Do something with response string
                        Log.d(TAG, "print responce String" + String.valueOf(response));

                        try {
                            JSONObject object = new JSONObject(response);
                            String status = object.getString("status");
                            String jsonObj = object.getString("data");

                            JSONObject res_obj = new JSONObject(jsonObj);

                            if(status.equals("0")){

                                String userId = res_obj.getString(Constants.USER_ID);
                                String userName = res_obj.getString(Constants.USER_NAME);
                                String userMail = res_obj.getString(Constants.USER_MAIL_ID);
                                String userCourse = res_obj.getString(Constants.USER_COURSE);
                                String userType = res_obj.getString(Constants.USER_TYPE);
                                String userGender = res_obj.getString(Constants.USER_GENDER);
                                String userMobile = res_obj.getString(Constants.USER_MOBILE_NUM);
                                String userProfile = res_obj.getString(Constants.USER_PROFILE);
                                String userPassword = res_obj.getString(Constants.USER_PASSWORD);


                                AppSharedPreferences.getInstance().setUserId(userId);
                                AppSharedPreferences.getInstance().setUserName(userName);
                                AppSharedPreferences.getInstance().setUserMail(userMail);
                                AppSharedPreferences.getInstance().setUserCourse(userCourse);
                                AppSharedPreferences.getInstance().setUserGender(userGender);
                                AppSharedPreferences.getInstance().setUserType(userType);
                                AppSharedPreferences.getInstance().setUserMobile(userMobile);
                                AppSharedPreferences.getInstance().setUserProfile(userProfile);
                                AppSharedPreferences.getInstance().setUserPassword(userPassword);



                                mServerResponseListener.onResponseData(response);
                            }else{

                                mServerResponseListener.onResponseData(response);

                            }



                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(mContext,e.toString(),Toast.LENGTH_SHORT).show();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Do something when get error
                        Log.e(TAG, error.toString());
                    }
                }) {
            @Override
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded; charset=UTF-8";
            }

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put(Constants.USER_NAME, username);
                params.put(Constants.USER_PASSWORD, password);
                return params;
            }

            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                if (response.headers == null) {
                    // cant just set a new empty map because the member is final.
                    response = new NetworkResponse(
                            response.statusCode,
                            response.data,
                            Collections.<String, String>emptyMap(), // this is the important line, set an empty but non-null map.
                            response.notModified,
                            response.networkTimeMs);


                }

                return super.parseNetworkResponse(response);
            }
        };

        // Add StringRequest to the RequestQueue
        VolleySigleton.getInstance(mContext).addToRequestQueue(stringRequest);
    }
}
