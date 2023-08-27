package com.neural.geez_ocr;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class WordActivity extends AppCompatActivity {

    ImageView userpic;
    AppCompatButton processBtn;
    Bitmap bitmap;
    LottieAnimationView scanning, scanning1;
    ScrollView scrollView;
    LinearLayout linearLayout;
    Uri mImageUri;

    TextView responseText;

    // flask
    private String url = "http://" + "10.141.114.62" + ":" + 5000 + "/";
    private String postBodyString;
    private MediaType mediaType;
    private RequestBody requestBody;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_word);

        processBtn = findViewById(R.id.processBtnCamera);
        userpic = findViewById(R.id.set_profile_image);
        scanning = findViewById(R.id.animationView);
        scrollView = findViewById(R.id.scrollview_camera);
        scanning1 = findViewById(R.id.secondView);
        linearLayout = findViewById(R.id.major_layout);
        responseText = findViewById(R.id.responseText);

        String imageUrl = getIntent().getStringExtra("picture");
        mImageUri = Uri.parse(imageUrl);

        try {
            bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), mImageUri);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        Picasso.with(this).load(mImageUri).into(userpic);
        //userpic.setImageBitmap(bitmap);

        processBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Drawable drawable = ContextCompat.getDrawable(WordActivity.this, R.drawable.black_background);
                linearLayout.setBackground(drawable);
                scanning.playAnimation();
                scanning1.playAnimation();

//                postRequest("your message here", url);
                connectServer(v);

            }
        });
    }

    private RequestBody buildRequestBody(String msg) {
        postBodyString = msg;
        mediaType = MediaType.parse("text/plain");
        requestBody = RequestBody.create(postBodyString, mediaType);
        return requestBody;
    }


    private void postRequest(String URL, RequestBody message) {

        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request
                .Builder()
                .url(URL)
                .post(message)
                .build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(final Call call, final IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(WordActivity.this, "Something went wrong:" + " " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        call.cancel();
                        responseText.setText("Failed to Connect to Server. Please Try Again.");
                        Log.d("FAIL", e.getMessage());
                    }
                });

            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                runOnUiThread(new Runnable() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void run() {
                        try {
                            String[] res = response.toString().split(",");
                            if(res[1].trim().equals("code=200")) {
                                int SDK_INT = android.os.Build.VERSION.SDK_INT;
                                if (SDK_INT > 8)
                                {
                                    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                                            .permitAll().build();
                                    StrictMode.setThreadPolicy(policy);
                                    //your codes here
                                    scanning.cancelAnimation();
                                    scanning1.cancelAnimation();
                                    assert response.body() != null;
                                    responseText.setText("Server's Response:\n" + response.body().string());
                                }
                            }
                            else
                                responseText.setText("Oops! Something went wrong. \n Please try again.");

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });


            }
        });
    }

    public void connectServer(View v) {

        responseText.setText("Sending the Files. Please Wait ...");

        EditText rowNumber = findViewById(R.id.server_url);
        String row = rowNumber.getText().toString();

        String postUrl = url + "/predict_word/";
        MultipartBody.Builder multipartBodyBuilder = new MultipartBody.Builder().setType(MultipartBody.FORM);

        byte[] byteArray = null;

        try {
            InputStream inputStream = getContentResolver().openInputStream(mImageUri);
            ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
            int bufferSize = 1024;
            byte[] buffer = new byte[bufferSize];
            int len = 0;

            while ((len = inputStream.read(buffer)) != -1) {
                byteBuffer.write(buffer, 0, len);
            }
            byteArray = byteBuffer.toByteArray();
            inputStream.close();
        }catch(Exception e) {
            responseText.setText("Please Make Sure the Selected File is an Image.");
            return;
        }


        multipartBodyBuilder.addFormDataPart("image", "input_img.jpg", RequestBody.create(MediaType.parse("image/*jpg"), byteArray));

        RequestBody postBodyImage = multipartBodyBuilder.build();

        postRequest(postUrl, postBodyImage);
    }


}