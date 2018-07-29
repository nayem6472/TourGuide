package com.nayem.tourguide.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nayem.tourguide.R;
import com.nayem.tourguide.database.UserManager;

public class LoginActivity extends AppCompatActivity {

    EditText uName,upass, uPutPhone, uPutName, uPutpass;
    TextView signUPtext;
    Button uSignUP, uSignIN;

    LinearLayout loginLayout, signLayout;
    Context context;
    UserManager userManager;

    char loginOrSignupFlag ='L';

    ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        progress = new ProgressDialog(this);
        progress.setCancelable(false);
        progress.setTitle("Loading");
        progress.setMessage("Please Wait...");
        progress.show();

        userManager=new UserManager(getApplicationContext());
        //-------sign in----//
        uName=(EditText) findViewById(R.id.userNameET);
        upass=(EditText) findViewById(R.id.userPassET);
        uSignIN=(Button) findViewById(R.id.signINbtn);

        signUPtext=(TextView) findViewById(R.id.signupTV);

        loginLayout = (LinearLayout) findViewById(R.id.loginLayout);

        //-------sign up----//
        uPutPhone=(EditText) findViewById(R.id.putPhoneNoET);
        uPutName=(EditText) findViewById(R.id.putUserNameET);
        uPutpass=(EditText) findViewById(R.id.putUserPassET);
        uSignUP=(Button) findViewById(R.id.signUPbtn);

        signLayout = (LinearLayout) findViewById(R.id.signupLayout);

        SharedPreferences sharedPreferences = getSharedPreferences("user_info",MODE_PRIVATE);
        int uid = sharedPreferences.getInt("uid",0);
        if(uid>0){
            Intent intent = new Intent(LoginActivity.this,ViewAllEventActivity.class);
            startActivity(intent);
            finish();
        }

    progress.dismiss();
    }

    public void singInclick(View view) {
        progress = new ProgressDialog(this);
        progress.setCancelable(false);
        progress.setTitle("Loading");
        progress.setMessage("Please Wait...");
        progress.show();

        String userID,password;
        userID = uName.getText().toString();
        password = upass.getText().toString();
        int result = userManager.loginCheck(userID,password);
        if (result>0){
            SharedPreferences sharedPreferences = getSharedPreferences("user_info",MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt("uid",result);
            editor.apply();
            editor.commit();
            //Toast.makeText(this, "Login Successful "+result, Toast.LENGTH_SHORT).show();
            clearField();
            Intent intent = new Intent(LoginActivity.this,ViewAllEventActivity.class);
            intent.putExtra("uid",result);
            startActivity(intent);
            finish();

        }else{
            Toast.makeText(LoginActivity.this, "Invalid username or password!", Toast.LENGTH_SHORT).show();
        }
        progress.dismiss();
    }

    public void singUpTextClick(View view) {
        switch (loginOrSignupFlag){
            case 'L':
                loginLayout.setVisibility(View.GONE);
                signLayout.setVisibility(View.VISIBLE);
                signUPtext.setText("Already Registered | Login");
                loginOrSignupFlag = 'S';
                break;
            case 'S':
                signLayout.setVisibility(View.GONE);
                loginLayout.setVisibility(View.VISIBLE);
                signUPtext.setText("New Here | SignUp");
                loginOrSignupFlag='L';
        }
    }

    public void singUPclick(View view) {
        progress = new ProgressDialog(this);
        progress.setCancelable(false);
        progress.setTitle("Loading");
        progress.setMessage("Please Wait...");
        progress.show();

        String userName,password,mobile;
        userName = uPutName.getText().toString();
        password = uPutpass.getText().toString();
        mobile = uPutPhone.getText().toString();

        long result = userManager.addUser(userName,password,mobile);

        if(result>0){
            Toast.makeText(this, "Your account successfully created.", Toast.LENGTH_SHORT).show();
            clearField();
            Intent intent = new Intent(LoginActivity.this,ViewAllEventActivity.class);
            intent.putExtra("uid",result);
            startActivity(intent);
        }
        progress.dismiss();
    }

    public void clearField(){
        uName.setText(null);
        upass.setText(null);
        uPutName.setText(null);
        uPutpass.setText(null);
        uPutPhone.setText(null);
        loginLayout.setVisibility(View.VISIBLE);
        signLayout.setVisibility(View.GONE);
        signUPtext.setText("New Here | SignUp");
    }
}
