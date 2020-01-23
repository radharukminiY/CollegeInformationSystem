package isep.fr.collegeinformationsystem.database;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;



import isep.fr.collegeinformationsystem.Constants;

public class AppSharedPreferences {

    private static final String TAG = "AppSharedPreferences";

    private static AppSharedPreferences mInstance;
    private Context context;

    private SharedPreferences sharedPreferences;

    private AppSharedPreferences() {
    }

    public static AppSharedPreferences getInstance() {
        if (mInstance == null) {
            mInstance = new AppSharedPreferences();
        }
        return mInstance;
    }

    public void Initialize(Context ctxt) {
        context = ctxt;
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public String getUserId() {
        return sharedPreferences.getString(Constants.USER_ID, "");
    }

    public void setUserId(String UserId) {
        SharedPreferences.Editor e = sharedPreferences.edit();
        e.putString(Constants.USER_ID, UserId);
        e.apply();
    }

    public String getUserName() {
        return sharedPreferences.getString(Constants.USER_NAME, "");
    }

    public void setUserName(String UserName) {
        SharedPreferences.Editor e = sharedPreferences.edit();
        e.putString(Constants.USER_NAME, UserName);
        e.apply();
    }

    public String getUserMail() {
        return sharedPreferences.getString(Constants.USER_MAIL, "");
    }

    public void setUserMail(String UserMail) {
        SharedPreferences.Editor e = sharedPreferences.edit();
        e.putString(Constants.USER_MAIL, UserMail);
        e.apply();
    }

    public String getUserPassword() {
        return sharedPreferences.getString(Constants.USER_PASSWORD, "");
    }

    public void setUserPassword(String UserPassword) {
        SharedPreferences.Editor e = sharedPreferences.edit();
        e.putString(Constants.USER_PASSWORD, UserPassword);
        e.apply();
    }

    public String getUserCourse() {
        return sharedPreferences.getString(Constants.USER_COURSE, "");
    }

    public void setUserCourse(String UserCourse) {
        SharedPreferences.Editor e = sharedPreferences.edit();
        e.putString(Constants.USER_COURSE, UserCourse);
        e.apply();
    }

    public String getUserType() {
        return sharedPreferences.getString(Constants.USER_TYPE, "");
    }

    public void setUserType(String UserType) {
        SharedPreferences.Editor e = sharedPreferences.edit();
        e.putString(Constants.USER_TYPE, UserType);
        e.apply();
    }

    public String getUserGender() {
        return sharedPreferences.getString(Constants.USER_GENDER, "");
    }

    public void setUserGender(String UserGender) {
        SharedPreferences.Editor e = sharedPreferences.edit();
        e.putString(Constants.USER_GENDER, UserGender);
        e.apply();
    }

    public String getUserMobile() {
        return sharedPreferences.getString(Constants.USER_GENDER, "");
    }

    public void setUserMobile(String UserGender) {
        SharedPreferences.Editor e = sharedPreferences.edit();
        e.putString(Constants.USER_GENDER, UserGender);
        e.apply();
    }

    public String getUserProfile() {
        return sharedPreferences.getString(Constants.USER_PROFILE, "");
    }

    public void setUserProfile(String UserProfile) {
        SharedPreferences.Editor e = sharedPreferences.edit();
        e.putString(Constants.USER_PROFILE, UserProfile);
        e.apply();
    }
}


