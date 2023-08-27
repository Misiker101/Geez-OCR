package com.neural.geez_ocr;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


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

public class UploadFileToServer extends AsyncTask<String, Integer, String> {

    private String url = "http://" + "10.0.2.2" + ":" + 5000 + "/";
    private String postBodyString;
    private MediaType mediaType;
    private RequestBody requestBody;

    //
    private long totalSize = 0;
    private ProgressDialog progressDialog;
    private Context context;
    OnDataSendToActivity dataSendToActivity;

    UploadFileToServer(Context context, Activity activity) {
        this.context = context;
        dataSendToActivity = (OnDataSendToActivity)activity;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog = new ProgressDialog(context);
        progressDialog.setCancelable(false);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setMessage("Please Wait..");
        progressDialog.show();
    }


    @Override
    protected String doInBackground(String... strings) {
        return null;
    }


    protected void onProgressUpdate(Integer... progress) {
        super.onProgressUpdate(progress);
        progressDialog.setProgress(progress[0]);
        if(progress[0]==100) {
            progressDialog.dismiss();
            progressDialog = new ProgressDialog(context);
            progressDialog.setCancelable(false);
            progressDialog.setMessage("Recognizing");
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.show();
        }
    }

    @Override
    protected void onPostExecute(String s) {
        Log.d("LOG", s);
        System.out.print(s);
        progressDialog.dismiss();
        dataSendToActivity.sendData(s);
    }

}
