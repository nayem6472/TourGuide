package com.nayem.tourguide.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.nayem.tourguide.R;
import com.nayem.tourguide.adapter.EventExpenseAdapter;
import com.nayem.tourguide.database.ExpenseManager;
import com.nayem.tourguide.model.ExpenseModel;
import android.support.design.widget.Snackbar;

import java.util.ArrayList;

public class ExpenseActivity extends AppCompatActivity {

    ListView expenseListView;

    EditText titleET,amountET;
    TextView totalTV,snackbarTextView;
    Button addButton;
    SharedPreferences sharedPreferences;

    EventExpenseAdapter eventExpenseAdapter;
    ArrayList<ExpenseModel> expenseModels;
    ExpenseManager expenseManager;

    int eid,budget;


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense);

        expenseListView = (ListView) findViewById(R.id.expenseAllListView);

        titleET = (EditText) findViewById(R.id.expenseTitleEditText);
        amountET = (EditText) findViewById(R.id.expenseAmountEditText);

        totalTV = (TextView) findViewById(R.id.expenseTotalTextView);
//        snackbarTextView = (TextView) findViewById(R.id.sncakbarTextView);

        addButton = (Button) findViewById(R.id.expenseAddButton);

        expenseManager= new ExpenseManager(this);

        eid = getIntent().getIntExtra("eid",0);
        budget = getIntent().getIntExtra("budget",0);

        expenseSetAdapter();

        addButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                String title = titleET.getText().toString();
                int amount = Integer.parseInt(amountET.getText().toString());
                long result = expenseManager.addExpense(eid,title,amount);
                if (result>0){
                    Toast.makeText(ExpenseActivity.this, "Expense successfully added.", Toast.LENGTH_SHORT).show();
                    titleET.setText(null);
                    amountET.setText(null);
                    titleET.requestFocus();
                    expenseSetAdapter();
                }
            }
        });


    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void expenseSetAdapter() {
        expenseModels = expenseManager.getAllExpense(eid);
        if (expenseModels !=null){
            eventExpenseAdapter = new EventExpenseAdapter(ExpenseActivity.this,expenseModels);
            expenseListView.setAdapter(eventExpenseAdapter);
            expenseListView.setItemsCanFocus(true);

            int totalExpense = expenseManager.getTotalExpenseAmount(eid);
            totalTV.setText(totalExpense+" TK");

            int checkExpense = checkExpenseWithBudget(totalExpense,budget);
            String msg;
            if (checkExpense==50){
                msg = "You have spent more than 50% of your budget.";
//                Snackbar.make(snackbarTextView,msg,Snackbar.LENGTH_LONG);
                Toast.makeText(ExpenseActivity.this, msg, Toast.LENGTH_LONG).show();
            }else if(checkExpense==80){
                msg = "You have spent more than 80% of your budget.";
                Toast.makeText(ExpenseActivity.this, msg, Toast.LENGTH_LONG).show();
            }else if(checkExpense==100){
                msg = "You have spent 100% of your budget.";
                Toast.makeText(ExpenseActivity.this, msg, Toast.LENGTH_LONG).show();
            }else if(checkExpense==200){
                msg = "You have crossed your budget.";
                Toast.makeText(ExpenseActivity.this, msg, Toast.LENGTH_LONG).show();
            }



        }


    }

    private int checkExpenseWithBudget(int expense,int budget){
        if ((((budget*50)/100)<=expense) && (((budget*80)/100)>expense)){
            return 50;
        }else if((((budget*80)/100)<=expense) && (((budget*100)/100)>expense)){
            return 80;
        }else if ((((budget*100)/100)==expense)){
            return 100;
        }else if ((((budget*100)/100)<expense)){
            return 200;
        }else {
            return 0;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater=new MenuInflater(this);
        menuInflater.inflate(R.menu.menubar,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.logoutMenu){
            sharedPreferences = getSharedPreferences("user_info",MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.remove("uid");
            //editor.remove("");
            editor.apply();
            editor.commit();
            Toast.makeText(getApplicationContext(),"Logout Successful",Toast.LENGTH_SHORT).show();
            Intent logoutIntent=new Intent(ExpenseActivity.this,LoginActivity.class);
            startActivity(logoutIntent);
            finish();
        }
        if(item.getItemId() == R.id.aboutUsMenu) {
            Intent intent=new Intent(ExpenseActivity.this,AboutUsActivity.class);
            startActivity(intent);
        }
        if (item.getItemId() == R.id.homeMenu){
            Intent homeIntent=new Intent(ExpenseActivity.this,ViewAllEventActivity.class);
            startActivity(homeIntent);
        }
        return true;

    }

}
