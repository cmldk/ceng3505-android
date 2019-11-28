package com.example.mysocialapp;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    ListView listView;
    List<Post> posts = new ArrayList<>();
    Button btnPost;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = findViewById(R.id.listView);
        PostAdapter adapter = new PostAdapter(this,posts);
        listView.setAdapter(adapter);

        textView = findViewById(R.id.textView);
        textView.setText("Cemal Dak");

        btnPost = (Button) findViewById(R.id.button);
        btnPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, PostActivity.class);
                startActivityForResult(intent, 1);
            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1 && resultCode == Activity.RESULT_OK){
            Post post = new Post();
            post.setMessage(data.getCharSequenceExtra("msg").toString());
            post.setImage((Bitmap) data.getParcelableExtra("bitmap"));
            posts.add(post);
            ((PostAdapter) listView.getAdapter()).notifyDataSetChanged();
        }
    }
}
