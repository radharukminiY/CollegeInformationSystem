package isep.fr.collegeinformationsystem.WebServiceRequest;

import android.content.Context;
import android.os.StrictMode;
import android.util.Log;

import isep.fr.collegeinformationsystem.Constants;
import isep.fr.collegeinformationsystem.WebServiceUtil.MultipartUtility;
import isep.fr.collegeinformationsystem.WebServiceUtil.ServerResponseListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.util.List;

public class UpdateDeptInfoService {
    private static final String TAG = "UpdateDeptInfoService";
    public Context mContext;
    private ServerResponseListener mServerResponseListener = null;
    private String deptId;
    private String deptName;
    private String deptShortName;
    private String deptSubjects;
    private String deptSylDoc;

    public UpdateDeptInfoService(Context context,final String deptId, final String deptName, final String deptShortName, final String deptSubjects, final String deptSylDoc, ServerResponseListener aServerResponseListener) {

        mContext = context;
        this.deptId = deptId;
        this.deptName = deptName;
        this.deptShortName = deptShortName;
        this.deptSubjects = deptSubjects;
        this.deptSylDoc = deptSylDoc;

        mServerResponseListener = aServerResponseListener;
    }

    public void UpdateDeptInfoService() {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        File profileFile = new File(deptSubjects);
        String fileName = profileFile.getName();
        try {

            String charset = "UTF-8";
            MultipartUtility multipart = new MultipartUtility(Constants.UPDATE_DEPT_URL, charset);
            //add parameters
            multipart.addFormField(Constants.DEPT_ID_LC, deptId);
            multipart.addFormField(Constants.DEPT_NAME, deptName);
            multipart.addFormField(Constants.DEPT_SHORT_NAME, deptShortName);
            multipart.addFormField(Constants.DEPT_SUBJECTS, deptSubjects);

            if (deptSylDoc != null && profileFile.exists()) {
                multipart.addFilePart(Constants.USER_PROFILE, fileName);
                byte[] buffer = new byte[1024];
                int bytesRead = 0;
                long totalBytesRead = 0;

                FileInputStream inputStream = new FileInputStream(profileFile);

                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    multipart.writeFileBytes(buffer, 0, bytesRead);
                    totalBytesRead += bytesRead;

                }

                inputStream.close();

            }


            List<String> response = multipart.finish();
            for (String line : response) {

                if (line != null) {

                    try {

                        JSONObject obj = new JSONObject(line);
                        Log.d(TAG, "chat message response receiver image " + String.valueOf(obj));

                        String status = obj.getString("status");

                        if (status.equalsIgnoreCase("0")) {

                            mServerResponseListener.onResponseData(obj);

                        } else {
                            mServerResponseListener.onResponseData(obj);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.d(TAG, "image sending response error " + e);
                        JSONObject obj = new JSONObject();
                        obj.put("status", 1);
                        obj.put("message", e.toString());

                        mServerResponseListener.onResponseData(obj);

                    }
                }

            }
        } catch (Exception ex) {
            System.err.println(ex);
            Log.d(TAG, "image sending error " + ex);
            // sending responce success or not ....
            mServerResponseListener.onResponseData(ex);

        }
    }
}
