package com.example.mysocialapp;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

public class PostActivity extends AppCompatActivity {

    Button btnPost;
    ImageView img;
    EditText txtMsg;
    ImageButton btnOk;
    ImageButton btnCancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        img = (ImageView) findViewById(R.id.imageView);
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (intent.resolveActivity(getPackageManager()) != null){
                    startActivityForResult(intent, 1);
                }
            }
        });

        txtMsg = (EditText)findViewById(R.id.editText);
        btnOk = (ImageButton) findViewById(R.id.imageButton);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putCharSequence("msg",txtMsg.getText());
                bundle.putParcelable("bitmap",((BitmapDrawable)img.getDrawable()).getBitmap());
                intent.putExtras(bundle);
                setResult(Activity.RESULT_OK,intent);
                finish();
            }
        });
        btnCancel = (ImageButton) findViewById(R.id.imageButton2);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setResult(Activity.RESULT_CANCELED);
                finish();
            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1 && resultCode == Activity.RESULT_OK){
            Bundle bundle = data.getExtras();
            Bitmap image = (Bitmap) bundle.get("data");
            img.setImageBitmap(image);
        }
    }
}
