package isep.fr.collegeinformationsystem.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import isep.fr.collegeinformationsystem.Constants;

import static isep.fr.collegeinformationsystem.Constants.APP_ALL_USERS;
import static isep.fr.collegeinformationsystem.Constants.ID;


public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String TAG = "DatabaseHelper";

    public static final String DATABASE_NAME = "app";

    private static final int DATABASE_VERSION = 1;

    private static DatabaseHelper mInstance = null;


    private DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    public static synchronized void init(Context context) {
        // If the instance is null then initialize the instance.
        if (mInstance == null) {
            mInstance = new DatabaseHelper(context);
        }
    }

    public static DatabaseHelper getInstance() {
        return mInstance;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        String CREATE_ALL_USERS_TABLE = "create table if not exists " + APP_ALL_USERS + "("
                + ID + " integer primary key,"
                + Constants.USER_LOCAL_ID + " text,"
                + Constants.USER_U_NAME + " text,"
                + Constants.USER_TYPE + " text,"
                + Constants.USER_MAIL + " text,"
                + Constants.USER_DEPT + " text,"
                + Constants.USER_PRFLE_IMG + " text)";


        String[] quaries = {CREATE_ALL_USERS_TABLE};


        for (String query : quaries) {
            sqLiteDatabase.execSQL(query);
        }


    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("Drop table if exists " + APP_ALL_USERS);

    }
}
