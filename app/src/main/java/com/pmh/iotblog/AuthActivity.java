package com.pmh.iotblog;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.pmh.iotblog.Fragments.SignInFragment;
import com.pmh.iotblog.Fragments.SignUpFragment;

public class AuthActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);
        getSupportFragmentManager().beginTransaction().replace(R.id.frameAuthContainer,
                new SignInFragment()).commit();
    }
}