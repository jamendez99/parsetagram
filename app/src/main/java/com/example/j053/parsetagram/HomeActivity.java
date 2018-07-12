package com.example.j053.parsetagram;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.j053.parsetagram.model.Post;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class HomeActivity extends AppCompatActivity {
    private String mImagePath;
    private EditText etDescription;
    private Button btnCreate;
    private Button btnCapture;
    private Button btnLogout;
    private ImageView ivPreview;
    private static final int REQUEST_CODE  = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        etDescription = findViewById(R.id.etDescription);
        btnCreate = findViewById(R.id.btnCreate);
        btnCreate.setEnabled(false);
        btnCapture = findViewById(R.id.btnCapture);
        btnLogout = findViewById(R.id.btnLogout);
        ivPreview = findViewById(R.id.ivPreview);


        btnCapture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // create Intent to take a picture and return control to the calling application
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                // Create a File reference to access to future access
                if (intent.resolveActivity(getPackageManager()) != null) {
                    // Create the File where the photo should go
                    File photoFile = null;
                    try {
                        photoFile = createImageFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    // Continue only if the File was successfully created
                    if (photoFile != null) {

                        Uri photoURI = FileProvider.getUriForFile(HomeActivity.this,
                                "com.example.android.fileprovider",
                                photoFile);

                        intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                        startActivityForResult(intent, REQUEST_CODE);
                    }
                }
            }
        });

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ParseUser.logOut();
                Intent intent = new Intent(HomeActivity.this, LaunchActivity.class);
                startActivity(intent);
            }
        });

        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String description = etDescription.getText().toString();
                final ParseUser user = ParseUser.getCurrentUser();

                // final File file = new File(getApplicationContext().getFilesDir(), "temp.jpg");
                final File file = new File(mImagePath);
                final ParseFile parseFile = new ParseFile(file);

                createPost(description, parseFile, user);

                Intent intent = new Intent(HomeActivity.this, TimelineActivity.class);
                setResult(TimelineActivity.RESULT_OK, intent);
                finish();
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            btnCreate.setEnabled(true);
            // Bundle extras = data.getExtras();
            // Bitmap imageBitmap = (Bitmap) extras.get("data");
            // ivPreview.setImageBitmap(imageBitmap);
            // persistImage(imageBitmap, "temp");
            ///// Uri uri = extras.get(MediaStore.EXTRA_OUTPUT);
            ivPreview.setImageURI(Uri.parse(mImagePath));
        }
    }

    private void createPost(String description, ParseFile image, ParseUser user) {
        final Post post = new Post();
        post.setDescription(description);
        post.setImage(image);
        post.setUser(user);

        post.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    Log.d("HomeActivity", "Created post successfully");
                } else {
                    e.printStackTrace();
                }
            }
        });
    }


    // Method not used anymore. Used before to create the thumbnail.
    /*
    private void persistImage(Bitmap bitmap, String name) {
        File filesDir = getApplicationContext().getFilesDir();
        File imageFile = new File(filesDir, name + ".jpg");

        OutputStream os;
        try {
            os = new FileOutputStream(imageFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, os);
            os.flush();
            os.close();
        } catch (Exception e) {
            Log.e(getClass().getSimpleName(), "Error writing bitmap", e);
        }
    }*/

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mImagePath = image.getAbsolutePath();
        return image;
    }

}
