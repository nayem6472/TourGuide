package com.nayem.tourguide.database;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.nayem.tourguide.model.MomentModel;

import java.util.ArrayList;

public class MomentManager {
    SQLiteDatabase sqLiteDatabase;
    DatabaseHelper databaseHelper;

    public MomentManager(Context context) {
        databaseHelper = new DatabaseHelper(context);
        sqLiteDatabase = databaseHelper.getWritableDatabase();
    }

    public long addMoment(MomentModel momentModel) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.MOMENT_COL_EVENT_ID, momentModel.geteID());
        contentValues.put(DatabaseHelper.MOMENT_COL_TITLE, momentModel.getTitle());
        contentValues.put(DatabaseHelper.MOMENT_COL_DETAILS, momentModel.getDetails());
        contentValues.put(DatabaseHelper.MOMENT_COL_IMAGE, momentModel.getImage());
        contentValues.put(DatabaseHelper.MOMENT_COL_SYS_DT_TM, momentModel.getDate());

        long queryResult = sqLiteDatabase.insert(DatabaseHelper.MOMENT_TABLE_NAME, null, contentValues);
        return queryResult;


    }

    public ArrayList<MomentModel> getEventAllMoment(int eid) {
        ArrayList<MomentModel> allMoments = new ArrayList<>();
        String sqlString = "SELECT * FROM " + DatabaseHelper.MOMENT_TABLE_NAME +
                " WHERE " + DatabaseHelper.MOMENT_COL_EVENT_ID + " = " + eid + " ORDER BY " + DatabaseHelper.MOMENT_COL_SYS_DT_TM + " DESC";
        Cursor cursor = sqLiteDatabase.rawQuery(sqlString, null);
        MomentModel momentModel;

        if (cursor.moveToFirst()) {
            do {
                int mid = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.MOMENT_COL_ID));
                String title = cursor.getString(cursor.getColumnIndex(DatabaseHelper.MOMENT_COL_TITLE));
                String details = cursor.getString(cursor.getColumnIndex(DatabaseHelper.MOMENT_COL_DETAILS));
                String img = cursor.getString(cursor.getColumnIndex(DatabaseHelper.MOMENT_COL_IMAGE));
                String mdate = cursor.getString(cursor.getColumnIndex(DatabaseHelper.MOMENT_COL_SYS_DT_TM));


                momentModel = new MomentModel(mid,title,details,img,mdate);
                allMoments.add(momentModel);
            } while (cursor.moveToNext());
        }else{
            allMoments = null;
        }
        return allMoments;
    }

    public long updateMoment (MomentModel momentModel){

        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.MOMENT_COL_TITLE, momentModel.getTitle());
        contentValues.put(DatabaseHelper.MOMENT_COL_DETAILS, momentModel.getDetails());
        contentValues.put(DatabaseHelper.MOMENT_COL_IMAGE, momentModel.getImage());
        contentValues.put(DatabaseHelper.MOMENT_COL_SYS_DT_TM, momentModel.getDate());

        long queryResult = sqLiteDatabase.update(DatabaseHelper.MOMENT_TABLE_NAME,contentValues,
                DatabaseHelper.MOMENT_COL_ID+" =? ",new String [] {String.valueOf(momentModel.getmID())});
        return queryResult;

    }

    public long deleteMoment(int mid){
        long queryResult = sqLiteDatabase.delete(DatabaseHelper.MOMENT_TABLE_NAME,DatabaseHelper.MOMENT_COL_ID+" =? ",
                new String[]{String.valueOf(mid)});

        return queryResult;
    }






}
