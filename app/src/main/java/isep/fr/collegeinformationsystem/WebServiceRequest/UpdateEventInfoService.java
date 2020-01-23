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

public class UpdateEventInfoService {
    private static final String TAG = "UpdateEventInfoService";
    public Context mContext;
    private ServerResponseListener mServerResponseListener = null;
    private String eventID;
    private String eventTitle;
    private String eventDateTime;
    private String eventDisrupt;
    private String eventType;
    private String eventPlace;
    private String eventHall;
    private String eventImg;

    public UpdateEventInfoService(Context context, final String eventID, final String eventTitle, final String eventDateTime, final String eventDisrupt, final String eventType, final String eventPlace, final String eventHall, final String eventImg, ServerResponseListener aServerResponseListener) {

        mContext = context;
        this.eventID = eventID;
        this.eventTitle = eventTitle;
        this.eventDateTime = eventDateTime;
        this.eventDisrupt = eventDisrupt;
        this.eventType = eventType;
        this.eventPlace = eventPlace;
        this.eventHall = eventHall;
        this.eventImg = eventImg;
        mServerResponseListener = aServerResponseListener;
    }

    public void UpdateEventInfoService() {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);



        try {

            String charset = "UTF-8";
            MultipartUtility multipart = new MultipartUtility(Constants.UPDATE_EVENT_URL, charset);
            //add parameters
            multipart.addFormField(Constants.EVENT_ID, eventID);
            multipart.addFormField(Constants.EVENT_TITLE, eventTitle);
            multipart.addFormField(Constants.EVENT_DATE_TIME, eventDateTime);
            multipart.addFormField(Constants.EVENT_DISCRIPTION, eventDisrupt);
            multipart.addFormField(Constants.EVENT_TYPE, eventType);
            multipart.addFormField(Constants.EVENT_PLACE, eventPlace);
            multipart.addFormField(Constants.EVENT_HALL, eventHall);

            if (eventImg != null && !eventImg.contains(":8080/isep/") ) {
                File profileFile = new File(eventImg);
                String fileName = profileFile.getName();

                multipart.addFilePart(Constants.EVENT_IMAGE, fileName);

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
