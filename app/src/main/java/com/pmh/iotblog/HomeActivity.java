package com.pmh.iotblog;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;

import android.net.Uri;
import android.os.Bundle;


import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.pmh.iotblog.Fragments.HomeFragment;

public class HomeActivity extends AppCompatActivity {

    private FragmentManager fragmentManager;

    private FloatingActionButton fab;
    private static final int GALLERY_ADD_POST = 2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.frameHomeContainer,new HomeFragment()).commit();
        init();
    }

    private void init() {

        fab = findViewById(R.id.fab);

        fab.setOnClickListener(v->{
            Intent i = new Intent(Intent.ACTION_PICK);
            i.setType("image/*");
            startActivityForResult(i,GALLERY_ADD_POST);
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // kiem tra anh dc chon
        if (requestCode == GALLERY_ADD_POST && resultCode == RESULT_OK){
            Uri imgUri  = data.getData();
            Intent i    = new Intent(HomeActivity.this, AddPostActivity.class);
            i.setData(imgUri);
            startActivity(i);
        }
    }


}