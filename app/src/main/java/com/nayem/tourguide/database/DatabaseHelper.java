package com.nayem.tourguide.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper{

    private static final String DATABASE_NAME = "tour_story";
    private static final int DATABASE_VERSION = 1;

    public static final  String USER_INFO_TABLE_NAME = "user_info";
    public static final  String USER_INFO_COL_ID = "uid";
    public static final  String USER_INFO_COL_USER_ID = "user_id";
    public static final  String USER_INFO_COL_PASSWORD = "password";
    public static final  String USER_INFO_COL_SYS_DT_TM = "sysdt";

    public static final  String USER_PROFILE_TABLE_NAME = "user_profile";
    public static final  String USER_PROFILE_COL_ID = "pid";
    public static final  String USER_PROFILE_COL_USER_ID = "uid";
    public static final  String USER_PROFILE_COL_USER_NAME = "user_name";
    public static final  String USER_PROFILE_COL_EMAIL = "email";
    public static final  String USER_PROFILE_COL_MOBILE = "mobile";
    public static final  String USER_PROFILE_COL_ADDRESS = "address";
    public static final  String USER_PROFILE_COL_SYS_DT_TM = "sysdt";

    public static final  String EVENT_TABLE_NAME = "event";
    public static final  String EVENT_COL_ID = "eid";
    public static final  String EVENT_COL_USER_ID = "uid";
    public static final  String EVENT_COL_TITLE = "title";
    public static final  String EVENT_COL_DESTINATION = "destination";
    public static final  String EVENT_COL_BUDGET = "budget";
    public static final  String EVENT_COL_START_DATE = "start_date";
    public static final  String EVENT_COL_END_DATE = "end_date";
    public static final  String EVENT_COL_SYS_DT_TM = "sysdt";

    public static final  String EXPENSE_TABLE_NAME = "expense";
    public static final  String EXPENSE_COL_ID = "exid";
    public static final  String EXPENSE_COL_EVENT_ID = "eid";
    public static final  String EXPENSE_COL_TITLE = "title";
    public static final  String EXPENSE_COL_AMOUNT = "amount";
    public static final  String EXPENSE_COL_SYS_DT_TM = "sysdt";

    public static final  String MOMENT_TABLE_NAME = "moment";
    public static final  String MOMENT_COL_ID = "mid";
    public static final  String MOMENT_COL_EVENT_ID = "eid";
    public static final  String MOMENT_COL_TITLE = "title";
    public static final  String MOMENT_COL_DETAILS = "details";
    public static final  String MOMENT_COL_IMAGE = "image";
    public static final  String MOMENT_COL_SYS_DT_TM = "sysdt";

    private String userInfoTableString = "CREATE TABLE "+USER_INFO_TABLE_NAME+" ("+
            USER_INFO_COL_ID+" INTEGER PRIMARY KEY AUTOINCREMENT,"+
            USER_INFO_COL_USER_ID+" TEXT NOT NULL UNIQUE,"+
            USER_INFO_COL_PASSWORD+" TEXT NOT NULL,"+
            USER_INFO_COL_SYS_DT_TM+" DATE DEFAULT CURRENT_DATE )";

    private String userProfileTableString = "CREATE TABLE "+USER_PROFILE_TABLE_NAME+" ("+
            USER_PROFILE_COL_ID+" INTEGER PRIMARY KEY AUTOINCREMENT,"+
            USER_PROFILE_COL_USER_ID+" INTEGER NOT NULL,"+
            USER_PROFILE_COL_USER_NAME+" TEXT,"+
            USER_PROFILE_COL_MOBILE+" TEXT NOT NULL UNIQUE,"+
            USER_PROFILE_COL_EMAIL+" TEXT UNIQUE,"+
            USER_PROFILE_COL_ADDRESS+" TEXT,"+
            USER_PROFILE_COL_SYS_DT_TM+" DATE DEFAULT CURRENT_DATE," +
            " FOREIGN KEY ("+USER_PROFILE_COL_USER_ID+") REFERENCES "+USER_INFO_TABLE_NAME+"("+USER_INFO_COL_ID+"))";

    private String eventTableString = "CREATE TABLE "+EVENT_TABLE_NAME+" ("+
            EVENT_COL_ID+" INTEGER PRIMARY KEY AUTOINCREMENT,"+
            EVENT_COL_USER_ID+" INTEGER NOT NULL,"+
            EVENT_COL_TITLE+" TEXT NOT NULL,"+
            EVENT_COL_DESTINATION+" TEXT NOT NULL,"+
            EVENT_COL_BUDGET+" INTEGER NOT NULL,"+
            EVENT_COL_START_DATE+" DATE NOT NULL,"+
            EVENT_COL_END_DATE+" DATE NOT NULL,"+
            EVENT_COL_SYS_DT_TM+" DATE DEFAULT CURRENT_DATE ,"+
            " FOREIGN KEY ("+EVENT_COL_USER_ID+") REFERENCES "+USER_INFO_TABLE_NAME+"("+USER_INFO_COL_ID+"))";

    private String expenseTableString = "CREATE TABLE "+EXPENSE_TABLE_NAME+" ("+
            EXPENSE_COL_ID+" INTEGER PRIMARY KEY AUTOINCREMENT,"+
            EXPENSE_COL_EVENT_ID+" INTEGER NOT NULL,"+
            EXPENSE_COL_TITLE+" TEXT NOT NULL,"+
            EXPENSE_COL_AMOUNT+" INTEGER NOT NULL,"+
            EXPENSE_COL_SYS_DT_TM+" DATE DEFAULT CURRENT_DATE ,"+
            " FOREIGN KEY ("+EXPENSE_COL_EVENT_ID+") REFERENCES "+EVENT_TABLE_NAME+"("+EVENT_COL_ID+"))";

    private String momentTableString = "CREATE TABLE "+MOMENT_TABLE_NAME+" ("+
            MOMENT_COL_ID+" INTEGER PRIMARY KEY AUTOINCREMENT,"+
            MOMENT_COL_EVENT_ID+" INTEGER NOT NULL,"+
            MOMENT_COL_TITLE+" TEXT NOT NULL,"+
            MOMENT_COL_DETAILS+" TEXT ,"+
            MOMENT_COL_IMAGE+" TEXT,"+
            MOMENT_COL_SYS_DT_TM+" DATE DEFAULT CURRENT_DATE ,"+
            " FOREIGN KEY ("+MOMENT_COL_EVENT_ID+") REFERENCES "+EVENT_TABLE_NAME+"("+EVENT_COL_ID+"))";



    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(userInfoTableString);
        db.execSQL(userProfileTableString);
        db.execSQL(eventTableString);
        db.execSQL(expenseTableString);
        db.execSQL(momentTableString);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE "+USER_INFO_TABLE_NAME+" if exists");
        db.execSQL("DROP TABLE "+USER_PROFILE_TABLE_NAME+" if exists");
        db.execSQL("DROP TABLE "+EVENT_TABLE_NAME+" if exists");
        db.execSQL("DROP TABLE "+EXPENSE_TABLE_NAME+" if exists");
        db.execSQL("DROP TABLE "+MOMENT_TABLE_NAME+" if exists");
        onCreate(db);

    }
}
