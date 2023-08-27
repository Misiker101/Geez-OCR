package com.neural.geez_ocr;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.neural.geez_ocr.adapter.CategoryAdapter;
import com.neural.geez_ocr.adapter.ModelAdapter;
import com.neural.geez_ocr.domain.CategoryDomain;
import com.neural.geez_ocr.domain.ModelDomain;
import com.theartofdev.edmodo.cropper.CropImage;

import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private RecyclerView.Adapter adapter, adapter2;
    private RecyclerView recyclerViewCategoryList, recyclerViewModelList;

    private ConstraintLayout scan_now;
    private LinearLayout galleryBtn;
    Bitmap bitmap;


    private ImageView settingsBtn;
    private FloatingActionButton cameraBtn;
    CameraActivity cameraActivity;

    // later delete

    private static final int GalleryPick = 1;
    private static final int CAMERA_REQUEST = 100;
    private static final int STORAGE_REQUEST = 200;
    private static final int IMAGEPICK_GALLERY_REQUEST = 300;
    private static final int IMAGE_PICKCAMERA_REQUEST = 400;
    String[] cameraPermission;
    String[] storagePermission;
    Uri resultUri;
    boolean gallerySelected = false;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        settingsBtn = (ImageView) findViewById(R.id.setting_main);
        settingsBtn.setClickable(true);

        galleryBtn = findViewById(R.id.galleryBtn);
        galleryBtn.setClickable(true);

        cameraBtn = (FloatingActionButton)findViewById(R.id.cameraBtnMain);
        cameraBtn.setClickable(true);

        // delete later
        loadLocale();
        scan_now = findViewById(R.id.scan_now_first);
        // upto this


        settingsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Drawable drawable = ContextCompat.getDrawable(MainActivity.this, R.drawable.card_back);
                settingsBtn.setBackground(drawable);
                startActivity(new Intent(MainActivity.this, SettingsActivity.class));

            }
        });

        // later delete
        // allowing permissions of gallery and camera
        cameraPermission = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermission = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};

        cameraBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showImagePicDialog();
                gallerySelected = false;
            }
        });

        // delete later
        scan_now.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showImagePicDialog();
                gallerySelected = false;
            }
        });

        galleryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showImagePicDialog();
                gallerySelected = true;
            }
        });
        // upto this
        recyclerViewCategoryList();
        recycleViewModel();
    }

    // also delete this later
    private void showImagePicDialog() {
        String[] options = {getString(R.string.camera), getString(R.string.gallery)};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.pick_image_from));
        builder.setItems(options, (dialog, which) -> {
            if (which == 0) {
                if (!checkCameraPermission()) {
                    requestCameraPermission();
                } else {
                    pickFromGallery();
                }
            } else if (which == 1) {
                if (!checkStoragePermission()) {
                    requestStoragePermission();
                } else {
                    pickFromGallery();
                }
            }
        });
        builder.create().show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case CAMERA_REQUEST: {
                if (grantResults.length > 0) {
                    boolean camera_accepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean writeStorageaccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    if (camera_accepted && writeStorageaccepted) {
                        pickFromGallery();
                    } else {
                        Toast.makeText(this, getString(R.string.enable_camera_and_storage_permission), Toast.LENGTH_LONG).show();
                    }
                }
            }
            break;
            case STORAGE_REQUEST: {
                if (grantResults.length > 0) {
                    boolean writeStorageaccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    if (writeStorageaccepted) {
                        pickFromGallery();
                    } else {
                        Toast.makeText(this, getString(R.string.enable_storage_permission), Toast.LENGTH_LONG).show();
                    }
                }
            }
            break;
        }
    }
    private void recyclerViewCategoryList() {

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerViewCategoryList = findViewById(R.id.recyclerView);
        recyclerViewCategoryList.setLayoutManager(linearLayoutManager);

        ArrayList<CategoryDomain> category = new ArrayList<>();
        category.add(new CategoryDomain(getString(R.string.word), "word_c"));
        category.add(new CategoryDomain(getString(R.string.pdf), "pdff"));
        category.add(new CategoryDomain(getString(R.string.jpeg), "jpeg"));
        category.add(new CategoryDomain(getString(R.string.qr), "qr"));
        category.add(new CategoryDomain(getString(R.string.lang), "lang"));
        category.add(new CategoryDomain(getString(R.string.id), "id"));
        category.add(new CategoryDomain(getString(R.string.book), "book"));

        adapter = new CategoryAdapter(category);
        recyclerViewCategoryList.setAdapter(adapter);
    }

    private void recycleViewModel() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerViewModelList = findViewById(R.id.recyclerView2);
        recyclerViewModelList.setLayoutManager(linearLayoutManager);

        ArrayList<ModelDomain> modelList = new ArrayList<>();
        modelList.add(new ModelDomain(getString(R.string.printed), "printed", "scan printed text", getString(R.string.tesseract), getString(R.string.scan)));
        modelList.add(new ModelDomain(getString(R.string.handwritten), "hannd", "scan Handwritten Text", getString(R.string.conv2d), getString(R.string.scan)));
        modelList.add(new ModelDomain(getString(R.string.process), "process", "show the process", getString(R.string.show_process), getString(R.string.show)));

       // modelList.add(new ModelDomain("book", "process", "show the process", "show process", "Show"));

        adapter2 = new ModelAdapter(modelList);
        recyclerViewModelList.setAdapter(adapter2);
    }

    // delete later
    private void showChangeLanguageDialog() {
        // Array of language to display in alert dialog
        final String[] listItems = {"English", "አማርኛ"};
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

        builder.setTitle(getString(R.string.choose_language));
        builder.setSingleChoiceItems(listItems, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (i == 0) {
                    // English
                    setLocale("en");
                    recreate();
                } else if (i == 1) {
                    //amharic
                    setLocale("am");
                    recreate();
                }

                dialogInterface.dismiss();
            }
        });

        AlertDialog alertDialog = builder.create();

        // Show alert dialog
        alertDialog.show();
    }



    private void setLocale(String lang) {

        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        Configuration configuration = getBaseContext().getResources().getConfiguration();
        configuration.locale = locale;
        super.onConfigurationChanged(configuration);
        getBaseContext().getResources().updateConfiguration(configuration, getBaseContext().getResources().getDisplayMetrics());

        SharedPreferences.Editor editor = getSharedPreferences("Settings", MODE_PRIVATE).edit();
        editor.putString("language", lang);
        editor.apply();

    }

    public void loadLocale() {
        SharedPreferences pref = getSharedPreferences("Settings", Activity.MODE_PRIVATE);
        String language = pref.getString("language", "");
        setLocale(language);

    }

    private Boolean checkStoragePermission() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);
    }

    private void requestStoragePermission() {
        requestPermissions(storagePermission, STORAGE_REQUEST);
    }

    private Boolean checkCameraPermission() {
        boolean result = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == (PackageManager.PERMISSION_GRANTED);
        boolean result1 = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);
        return result && result1;
    }

    private void requestCameraPermission() {
        requestPermissions(cameraPermission, CAMERA_REQUEST);
    }

    // Here we will pick image from gallery or camera
    private void pickFromGallery() {
        CropImage.activity().start(MainActivity.this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                if(gallerySelected) {
                    assert result != null;
                    resultUri = result.getUri();
                    Intent i = new Intent(MainActivity.this, WordActivity.class);
                    i.putExtra("picture", resultUri.toString()); // sending our object.
                    startActivity(i);
                } else {
                    resultUri = result.getUri();
                    Intent i = new Intent(MainActivity.this, CameraActivity.class);
                    i.putExtra("picture", resultUri.toString()); // sending our object.
                    startActivity(i);
                }
            }
        }
    }
}