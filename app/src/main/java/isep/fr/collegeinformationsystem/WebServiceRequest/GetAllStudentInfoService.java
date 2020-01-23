package isep.fr.collegeinformationsystem.WebServiceRequest;

import android.content.Context;
import android.os.StrictMode;
import android.util.Log;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import isep.fr.collegeinformationsystem.Constants;
import isep.fr.collegeinformationsystem.WebServiceUtil.ServerResponseListener;
import isep.fr.collegeinformationsystem.WebServiceUtil.VolleySigleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class GetAllStudentInfoService {

    private static final String TAG = "GetAllStudentInfo";
    Context mContext;
    private ServerResponseListener mServerResponseListener = null;
    private String userType;


    public GetAllStudentInfoService(Context context, final String userType, ServerResponseListener aServerResponseListener) {

        mContext = context;
        this.userType = userType;
        mServerResponseListener = aServerResponseListener;
    }

    public void GetAllStudentInfoService() {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                Constants.GET_ALL_STUDENTS_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Do something with response string
                        Log.d(TAG, "print student response" + String.valueOf(response));

                        try {
                            JSONObject object = new JSONObject(response);
                            String status = object.getString("status");

                            if (status.equals("0")) {

                                JSONArray jsonArray = object.getJSONArray(Constants.DATA);
                                if (jsonArray.length() != 0) {
                                    for (int i = 0; i < jsonArray.length(); i++) {

                                        JSONObject jobj = jsonArray.getJSONObject(i);
                                        String studentId = jobj.getString(Constants.USER_ID);
                                        String studentName = jobj.getString(Constants.USER_NAME);
                                        String studentMail = jobj.getString(Constants.USER_MAIL_ID);
                                        String studentPass = jobj.getString(Constants.USER_PASSWORD);
                                        String studentType = jobj.getString(Constants.USER_TYPE);
                                        String studentMobile = jobj.getString(Constants.USER_MOBILE_NUM);
                                        String studentGender = jobj.getString(Constants.USER_GENDER);
                                        String studentDept = jobj.getString(Constants.USER_DEPT);
                                        String studentImage = jobj.getString(Constants.USER_PROFILE);


                                      /*  StudentModel ch = new StudentModel(adminId, channelImage, description, channelName, tag, creationDate, category, channelId, channelPrivacy, subscribers);
                                        channelList.add(ch);*/

                                    }

                                } else {
                                   // Nodata is getting please set no data in the list
                                }



                                mServerResponseListener.onResponseData(object);

                            } else {
                                mServerResponseListener.onResponseData(object);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();

                            Log.d(TAG, "image sending response error " + e);
                            JSONObject obj = new JSONObject();

                            try {
                                obj.put("status", 1);
                                obj.put("message", e.toString());
                            } catch (JSONException ex) {
                                ex.printStackTrace();
                            }

                            mServerResponseListener.onResponseData(obj);
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

                params.put(Constants.USER_TYPE, userType);

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
