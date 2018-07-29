package com.nayem.tourguide.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.icu.text.SimpleDateFormat;
import android.net.ParseException;
import android.os.Build;
import android.support.annotation.RequiresApi;

import com.nayem.tourguide.model.ExpenseModel;

import java.sql.Date;
import java.util.ArrayList;

public class ExpenseManager {
    SQLiteDatabase sqLiteDatabase;
    DatabaseHelper databaseHelper;

    public ExpenseManager(Context context) {
        databaseHelper = new DatabaseHelper(context);
        sqLiteDatabase = databaseHelper.getWritableDatabase();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public long addExpense(int eid, String title, int amount) {

        ContentValues contentValues = new ContentValues();

        contentValues.put(DatabaseHelper.EXPENSE_COL_EVENT_ID,eid);
        contentValues.put(DatabaseHelper.EXPENSE_COL_TITLE, title);
        contentValues.put(DatabaseHelper.EXPENSE_COL_AMOUNT, amount);

        long queryResult = sqLiteDatabase.insert(DatabaseHelper.EXPENSE_TABLE_NAME, null, contentValues);
        return queryResult;

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public ArrayList<ExpenseModel> getAllExpense(int eid) {
        ArrayList<ExpenseModel> allExpenses = new ArrayList<>();
        String sqlString = "SELECT * FROM " + DatabaseHelper.EXPENSE_TABLE_NAME +
                " WHERE " + DatabaseHelper.EXPENSE_COL_EVENT_ID + " = " + eid + " ORDER BY " + DatabaseHelper.EXPENSE_COL_SYS_DT_TM + " DESC";
        Cursor cursor = sqLiteDatabase.rawQuery(sqlString, null);
        ExpenseModel expenseModel;

        if (cursor.moveToFirst()) {
            do {
                int exid = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.EXPENSE_COL_ID));
                String title = cursor.getString(cursor.getColumnIndex(DatabaseHelper.EXPENSE_COL_TITLE));
                int amount = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.EXPENSE_COL_AMOUNT));
//                SimpleDateFormat dateFormat = new SimpleDateFormat("DD-MM-YYYY");
                String date = null;
                try {
                    date = cursor.getString(cursor.getColumnIndex(DatabaseHelper.EXPENSE_COL_SYS_DT_TM));
                    //date = dateFormat.format(date);
                } catch (ParseException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                expenseModel = new ExpenseModel(exid,title,amount,date);
                allExpenses.add(expenseModel);
            } while (cursor.moveToNext());
        }else{
            allExpenses = null;
        }

        return allExpenses;
    }

    public int getTotalExpenseAmount(int eid){
        String sqlString = "SELECT SUM("+DatabaseHelper.EXPENSE_COL_AMOUNT+") FROM " + DatabaseHelper.EXPENSE_TABLE_NAME +
                " WHERE " + DatabaseHelper.EXPENSE_COL_EVENT_ID + " = " + eid;
        Cursor cursor = sqLiteDatabase.rawQuery(sqlString, null);

        if (cursor.moveToFirst()) {
            return cursor.getInt(0);
        }else {
            return 0;
        }
    }



}
