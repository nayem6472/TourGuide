package com.nayem.tourguide.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.nayem.tourguide.R;
import com.nayem.tourguide.adapter.MomentRowAdapter;
import com.nayem.tourguide.database.DatabaseHelper;
import com.nayem.tourguide.database.MomentManager;
import com.nayem.tourguide.model.MomentModel;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import static android.provider.UserDictionary.AUTHORITY;

public class MomentsActivity extends AppCompatActivity {
    private static final int CAMERA_REQUEST_INTENT = 1;
    private static final int GALLERY_IMAGE_REQUEST_INTENT = 2;
    ImageView currentImage;
    Bitmap imageBitmap;
    String currentImagePath;
    Uri imageUri;

    int eid;

    EditText titleET,descriptionET;
    Button addMoment;
    SharedPreferences sharedPreferences;

    MomentManager momentManager;
    private ListView momentListView;
    private MomentRowAdapter momentRowAdapter;
    private ArrayList<MomentModel> momentModels=null;
    private DatabaseHelper databaseHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_moments);
        titleET= (EditText) findViewById(R.id.momentTitleEditText);
        descriptionET= (EditText) findViewById(R.id.momentDescriptionEditText);
        addMoment= (Button) findViewById(R.id.addMomentButton);
        currentImage= (ImageView) findViewById(R.id.currentImageIV);
        momentListView= (ListView) findViewById(R.id.momentDetailsListView);

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        

        momentManager=new MomentManager(this);

        eid = getIntent().getIntExtra("eid",0);

        callAdapter();

        addMoment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String date = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
                String title = titleET.getText().toString().trim();
                String description = descriptionET.getText().toString().trim();


                if (titleET.getText().toString() != null && currentImagePath != null) {
                    MomentModel momentModel = new MomentModel(0, eid, title, description, currentImagePath, date);
                    long momentAddResult = momentManager.addMoment(momentModel);
                    if (momentAddResult > 0) {
                        Toast.makeText(MomentsActivity.this, "Moment Added", Toast.LENGTH_SHORT).show();
                        titleET.setText(null);
                        descriptionET.setText(null);
                        currentImage.setImageURI(null);
                        callAdapter();
                    }

                }else {
                    Toast.makeText(MomentsActivity.this, "Fill up at least title and picture ", Toast.LENGTH_SHORT).show();

                }
            }
        });




    }

    private void callAdapter() {
        momentModels=momentManager.getEventAllMoment(eid);
        if (momentModels != null) {
            momentRowAdapter = new MomentRowAdapter(this, momentModels);
            momentListView.setAdapter(momentRowAdapter);
        }
    }

    public void startCamera(View view) {
        Intent cameraIntent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //if (cameraIntent.resolveActivity(getPackageManager())!=null){
            File photoFile = null;
            try {
                photoFile=getImageFile();
            }catch (IOException e){
                Toast.makeText(this,""+e.getMessage(),Toast.LENGTH_SHORT).show();
            }
            if (photoFile != null){
                /*Intent cameraIntent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE,FileProvider.getUriForFile(this, AUTHORITY, photoFile));
                cameraIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);*/
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
                startActivityForResult(cameraIntent,CAMERA_REQUEST_INTENT);
            }

       // }

    }

    private File getImageFile() throws IOException {
        String timeStamp=new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        //Uri photoURI = FileProvider.getUriForFile(this, this.getApplicationContext().getPackageName() + ".my.package.name.provider", createImageFile());
        String imageFileName="JPEG_"+timeStamp+"_";
        File storageDirectory= Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File image=File.createTempFile(imageFileName,".jpg",storageDirectory);
        currentImagePath=image.getAbsolutePath();
        return  image;

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST_INTENT && resultCode== RESULT_OK){

           // int targetW=currentImage.getWidth();
           // int targetH=currentImage.getHeight();

            BitmapFactory.Options options=new BitmapFactory.Options();
            options.inJustDecodeBounds=true;
            BitmapFactory.decodeFile(currentImagePath,options);
           // int photoW=options.outWidth;
            //int photoH=options.outHeight;

          //  int scaleFactor=Math.min(photoW/targetW,photoH/targetH);
            options.inJustDecodeBounds=false;
            options.inSampleSize=8;

            imageBitmap=BitmapFactory.decodeFile(currentImagePath,options);
            Matrix mat = new Matrix();

            mat.postRotate(Integer.parseInt("-270"));
            Bitmap bMapRotate = Bitmap.createBitmap(imageBitmap, 0, 0,imageBitmap.getWidth(),imageBitmap.getHeight(), mat, true);
            currentImage.setImageBitmap(bMapRotate);
            //currentImage.setImageBitmap(imageBitmap);
            Toast.makeText(this,"Moment Added.",Toast.LENGTH_SHORT).show();


        }
        if (requestCode == GALLERY_IMAGE_REQUEST_INTENT && resultCode == RESULT_OK){
            Uri selectedImage=data.getData();
            String[] filePathColumn={MediaStore.Images.Media.DATA};
            Cursor cursor=getContentResolver().query(selectedImage,filePathColumn,null,null,null);
            cursor.moveToFirst();
            int columnIndex=cursor.getColumnIndex(filePathColumn[0]);
            currentImagePath=cursor.getString(columnIndex);
            cursor.close();

           // int targetW=currentImage.getWidth();
            //int targetH=currentImage.getHeight();

            BitmapFactory.Options options=new BitmapFactory.Options();
            options.inJustDecodeBounds=true;
            BitmapFactory.decodeFile(currentImagePath,options);

           // int photoW=options.outWidth;
            //int photoH=options.outHeight;
           // int scaleFactor=Math.min(photoW/targetW,photoH/targetH);

            options.inJustDecodeBounds=false;
            options.inSampleSize=8;

            imageBitmap=BitmapFactory.decodeFile(currentImagePath,options);
            Matrix mat = new Matrix();

            mat.postRotate(Integer.parseInt("-180"));
            Bitmap bMapRotate = Bitmap.createBitmap(imageBitmap, 0, 0,imageBitmap.getWidth(),imageBitmap.getHeight(), mat, true);
            currentImage.setImageBitmap(bMapRotate);
            Toast.makeText(this,""+currentImagePath,Toast.LENGTH_SHORT).show();
        }

    }

    public void pickImageFromGallery(View view) {
        Intent getPictureIntent=new Intent();
        getPictureIntent.setType("image/*");
        getPictureIntent.setAction(Intent.ACTION_PICK);
        startActivityForResult(Intent.createChooser(getPictureIntent,"select picture"),GALLERY_IMAGE_REQUEST_INTENT);
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
            Intent logoutIntent=new Intent(MomentsActivity.this,LoginActivity.class);
            startActivity(logoutIntent);
            finish();
        }
        if(item.getItemId() == R.id.aboutUsMenu) {
            Intent intent=new Intent(MomentsActivity.this,AboutUsActivity.class);
            startActivity(intent);
        }
        if (item.getItemId() == R.id.homeMenu){
            Intent homeIntent=new Intent(MomentsActivity.this,ViewAllEventActivity.class);
            startActivity(homeIntent);
        }
        return true;

    }

}
