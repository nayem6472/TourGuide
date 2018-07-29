package com.nayem.tourguide.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.hardware.usb.UsbRequest;

import com.nayem.tourguide.model.UserModel;

public class UserManager {
    SQLiteDatabase sqLiteDatabase;
    DatabaseHelper databaseHelper;
    Cursor cursor;

    public UserManager(Context context){
        databaseHelper = new DatabaseHelper(context);
        sqLiteDatabase = databaseHelper.getWritableDatabase();
    }

    public long addUser(String userID, String password,String mobile){
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.USER_INFO_COL_USER_ID,userID);
        contentValues.put(DatabaseHelper.USER_INFO_COL_PASSWORD,password);

        long queryResult = sqLiteDatabase.insert(DatabaseHelper.USER_INFO_TABLE_NAME,null,contentValues);
        int uid = getUID(userID);
        long queryResult1 =0;
        if(uid>0){
            contentValues = new ContentValues();
            contentValues.put(DatabaseHelper.USER_PROFILE_COL_MOBILE,mobile);
            contentValues.put(DatabaseHelper.USER_PROFILE_COL_USER_ID,uid);
            queryResult1 = sqLiteDatabase.insert(DatabaseHelper.USER_PROFILE_TABLE_NAME,null,contentValues);
        }
        if (queryResult>0 && queryResult1>0){
            return uid;
        }else {
            return 0;
        }


    }

    public int getUID(String userID){
        int uid = 0;
        cursor = sqLiteDatabase.rawQuery("SELECT "+DatabaseHelper.USER_INFO_COL_ID+" FROM "+DatabaseHelper.USER_INFO_TABLE_NAME+" WHERE "+DatabaseHelper.USER_INFO_COL_USER_ID+" = '"+userID+"'",null);
        if (cursor.moveToFirst()){
            uid = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.USER_INFO_COL_ID));
        }

        return uid;

    }

    public int loginCheck(String userID, String password){
        cursor = sqLiteDatabase.rawQuery("SELECT * FROM "+DatabaseHelper.USER_INFO_TABLE_NAME+" WHERE "+DatabaseHelper.USER_INFO_COL_USER_ID+" = '"+userID+"' AND "+DatabaseHelper.USER_INFO_COL_PASSWORD+" = '"+password+"'",null);
        if (cursor.moveToFirst()){
            return cursor.getInt(cursor.getColumnIndex(DatabaseHelper.USER_INFO_COL_ID));
        }else {
            return 0;
        }
    }

    public long updateUserProfile(UserModel userModel){
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.USER_PROFILE_COL_USER_NAME,userModel.getFullName());
        contentValues.put(DatabaseHelper.USER_PROFILE_COL_MOBILE,userModel.getMobile());
        contentValues.put(DatabaseHelper.USER_PROFILE_COL_EMAIL,userModel.getEmail());
        contentValues.put(DatabaseHelper.USER_PROFILE_COL_ADDRESS,userModel.getAddress());

        long queryResult = sqLiteDatabase.update(DatabaseHelper.USER_PROFILE_TABLE_NAME,contentValues,DatabaseHelper.USER_PROFILE_COL_USER_ID+" =? ",new String[]{String.valueOf(userModel.getuID())});

        return queryResult;
    }

    public UserModel getUserProfile(int uid){
        cursor = sqLiteDatabase.rawQuery("SELECT * FROM "+DatabaseHelper.USER_PROFILE_TABLE_NAME+" WHERE "+DatabaseHelper.USER_PROFILE_COL_USER_ID+" = '"+uid+"'",null);
        UserModel userModel = null;
        if (cursor.moveToFirst()){
            String name = cursor.getString(cursor.getColumnIndex(DatabaseHelper.USER_PROFILE_COL_USER_NAME));
            String mobile = cursor.getString(cursor.getColumnIndex(DatabaseHelper.USER_PROFILE_COL_MOBILE));
            String email = cursor.getString(cursor.getColumnIndex(DatabaseHelper.USER_PROFILE_COL_EMAIL));
            String address = cursor.getString(cursor.getColumnIndex(DatabaseHelper.USER_PROFILE_COL_ADDRESS));
            userModel = new UserModel(name,email,mobile,address);
        }
        return userModel;
    }


}
