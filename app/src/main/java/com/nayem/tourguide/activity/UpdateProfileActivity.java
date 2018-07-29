package com.nayem.tourguide.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.nayem.tourguide.R;
import com.nayem.tourguide.database.UserManager;
import com.nayem.tourguide.model.UserModel;

public class UpdateProfileActivity extends AppCompatActivity {

    EditText nameET, emailET, mobileET, addressET;
    Button updateBtn;

    UserManager userManager;
    UserModel userModel;
    int uid;

    ImageView backToHome;

    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);

        nameET = (EditText) findViewById(R.id.fullNameEditText);
        emailET = (EditText) findViewById(R.id.emailEditText);
        mobileET = (EditText) findViewById(R.id.mobileEditText);
        addressET = (EditText) findViewById(R.id.addressEditText);

        updateBtn = (Button) findViewById(R.id.updateProfileButton);

        backToHome= (ImageView) findViewById(R.id.backToHomeFromUpdateInfo);

        backToHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent homeIntent=new Intent(UpdateProfileActivity.this,ViewAllEventActivity.class);
                startActivity(homeIntent);
            }
        });

        userManager = new UserManager(this);

        SharedPreferences sharedPreferences = getSharedPreferences("user_info",MODE_PRIVATE);
        uid = sharedPreferences.getInt("uid",0);

        userModel = userManager.getUserProfile(uid);

        nameET.setText(userModel.getFullName());
        mobileET.setText(userModel.getMobile());
        emailET.setText(userModel.getEmail());
        addressET.setText(userModel.getAddress());

        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name,mobile,email,address;
                name = nameET.getText().toString();
                mobile = mobileET.getText().toString();
                email = emailET.getText().toString();
                address = addressET.getText().toString();

                userModel = new UserModel(uid,name,email,mobile,address);
                long result = userManager.updateUserProfile(userModel);

                if (result>0){
                    Toast.makeText(UpdateProfileActivity.this, "Information Updated", Toast.LENGTH_SHORT).show();
                    Intent homeIntent=new Intent(UpdateProfileActivity.this,ViewAllEventActivity.class);
                    startActivity(homeIntent);
                }
            }
        });
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
            Intent logoutIntent=new Intent(UpdateProfileActivity.this,LoginActivity.class);
            startActivity(logoutIntent);
            finish();
        }
        if(item.getItemId() == R.id.aboutUsMenu) {
            Intent intent=new Intent(UpdateProfileActivity.this,AboutUsActivity.class);
            startActivity(intent);

        }
        if (item.getItemId() == R.id.homeMenu){
            Intent homeIntent=new Intent(UpdateProfileActivity.this,ViewAllEventActivity.class);
            startActivity(homeIntent);
        }
        return true;

    }
}
