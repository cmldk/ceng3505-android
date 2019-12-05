package com.example.imagedownloader;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class MainActivity extends AppCompatActivity {

    EditText txtURL;
    Button btnDownload;
    ImageView imgView;

    ProgressDialog PD;

    private static final int REQUEST_EXTERNAL_STORAGE = 1;

    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtURL = (EditText) findViewById(R.id.txtURL);
        btnDownload = (Button) findViewById(R.id.btnDownload);
        imgView = (ImageView) findViewById(R.id.imgView);

        btnDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int permission = ActivityCompat.checkSelfPermission(
                        MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
                if (permission != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(MainActivity.this, PERMISSIONS_STORAGE, REQUEST_EXTERNAL_STORAGE);
                }
                ImageDownloader imageDownloader = new ImageDownloader();
                imageDownloader.execute(txtURL.getText().toString());
            }
        });
    }

    class ImageDownloader extends AsyncTask<String,Integer, Bitmap>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            PD = new ProgressDialog(MainActivity.this);
            PD.setMax(100);
            PD.setIndeterminate(false);
            PD.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            PD.setTitle("Downloading");
            PD.setMessage("Please wait...");
            PD.show();
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            PD.setProgress(values[0]);
        }

        @Override
        protected Bitmap doInBackground(String... strings) {
            String filename = "temp.jpg";
            String imagePath = (Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)).toString() + "/" + filename;
            Bitmap image = downloadImage(strings[0],imagePath);

            return image;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            float w = bitmap.getWidth();
            float h = bitmap.getHeight();

            int W = 400;
            int H = (int) ( (h*W)/w);

            PD.dismiss();
            Bitmap newBitmap = Bitmap.createScaledBitmap(bitmap, W, H, false);
            imgView.setImageBitmap(newBitmap);
        }

        public Bitmap downloadImage(String strURL, String imagePath){
            try {
                URL url = new URL(strURL);
                URLConnection connection = url.openConnection();
                connection.connect();

                int fileSize = connection.getContentLength();
                InputStream inputStream = new BufferedInputStream(url.openStream(),8192);
                OutputStream outputStream = new FileOutputStream(imagePath);

                int total=0;
                byte data[] = new byte[1024];

                int size;
                while ((size = inputStream.read()) != -1){
                    outputStream.write(data,0,size);
                    total += size;

                    int percentage = (int) (((double)total / fileSize) * 100);

                    publishProgress(percentage);
                }
                outputStream.close();
                inputStream.close();

            } catch (IOException e) {
                Log.e("Download",strURL,e);
            }

            return BitmapFactory.decodeFile(imagePath);
        }
    }
}
