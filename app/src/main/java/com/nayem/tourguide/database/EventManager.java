package com.nayem.tourguide.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.nayem.tourguide.model.EventModel;

import java.util.ArrayList;

public class EventManager {
    SQLiteDatabase sqLiteDatabase;
    DatabaseHelper databaseHelper;
    ContentValues contentValues;

    public EventManager(Context context) {
        databaseHelper = new DatabaseHelper(context);
        sqLiteDatabase = databaseHelper.getWritableDatabase();
    }

    public long addEvent(EventModel eventModel) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.EVENT_COL_USER_ID, eventModel.getuID());
        contentValues.put(DatabaseHelper.EVENT_COL_TITLE, eventModel.getTitle());
        contentValues.put(DatabaseHelper.EVENT_COL_DESTINATION, eventModel.getDestination());
        contentValues.put(DatabaseHelper.EVENT_COL_BUDGET, eventModel.getBudget());
        contentValues.put(DatabaseHelper.EVENT_COL_START_DATE, eventModel.getStartDate());
        contentValues.put(DatabaseHelper.EVENT_COL_END_DATE, eventModel.getEndDate());

        long queryResult = sqLiteDatabase.insert(DatabaseHelper.EVENT_TABLE_NAME, null, contentValues);
        return queryResult;


    }

    public ArrayList<EventModel> getUserAllEvent(int uid) {
        ArrayList<EventModel> allEvents = new ArrayList<>();
        String sqlString = "SELECT * FROM " + DatabaseHelper.EVENT_TABLE_NAME +
                " WHERE " + DatabaseHelper.EVENT_COL_USER_ID + " = " + uid + " ORDER BY " + DatabaseHelper.EVENT_COL_SYS_DT_TM + " DESC";
        Cursor cursor = sqLiteDatabase.rawQuery(sqlString, null);
        EventModel eventModel;

        if (cursor.moveToFirst()) {
            do {
                int eid = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.EVENT_COL_ID));
                String title = cursor.getString(cursor.getColumnIndex(DatabaseHelper.EVENT_COL_TITLE));
                String destination = cursor.getString(cursor.getColumnIndex(DatabaseHelper.EVENT_COL_DESTINATION));
                int budget = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.EVENT_COL_BUDGET));
                String startDate = cursor.getString(cursor.getColumnIndex(DatabaseHelper.EVENT_COL_START_DATE));
                String endDate = cursor.getString(cursor.getColumnIndex(DatabaseHelper.EVENT_COL_END_DATE));

                eventModel = new EventModel(eid,destination,title,budget,startDate,endDate);
                allEvents.add(eventModel);
            } while (cursor.moveToNext());
        }else{
            allEvents = null;
        }

        return allEvents;
    }

    public long deleteEvent(int eid){
        long queryResult = sqLiteDatabase.delete(DatabaseHelper.EVENT_TABLE_NAME,DatabaseHelper.EVENT_COL_ID+" =? ",
                new String[]{String.valueOf(eid)});

        return queryResult;
    }

    public long updateEvent (EventModel eventModel){
        contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.EVENT_COL_DESTINATION,eventModel.getDestination());
        contentValues.put(DatabaseHelper.EVENT_COL_BUDGET,eventModel.getBudget());
        contentValues.put(DatabaseHelper.EVENT_COL_START_DATE,eventModel.getStartDate());
        contentValues.put(DatabaseHelper.EVENT_COL_END_DATE,eventModel.getEndDate());

        long queryResult = sqLiteDatabase.update(DatabaseHelper.EVENT_TABLE_NAME,contentValues,
                DatabaseHelper.EVENT_COL_ID+" =? ",new String [] {String.valueOf(eventModel.geteID())});
        return queryResult;

    }


}
