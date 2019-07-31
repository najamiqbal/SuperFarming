package com.softsolstudio.superfarming.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.softsolstudio.superfarming.models.UserModelClass;
import com.softsolstudio.superfarming.utils.SharedPrefManager;

public class SplashAcitivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UserModelClass userModelClass = SharedPrefManager.getInstance(this).getUser();
        if (userModelClass != null) {
            switch (userModelClass.getUser_type()) {
                case "Farmer": {
                    Intent intent = new Intent(this, MainActivity.class);
                    startActivity(intent);
                    finish();
                    break;
                }
                case "Trader": {
                    Intent intent=new Intent(this,TraderMainActivity.class);
                    startActivity(intent);
                    finish();
                    break;
                }
                case "Service provider":{
                    Intent intent = new Intent(this, ServiceProviderActivity.class);
                    startActivity(intent);
                    finish();
                    break;
                }
            }

        } else {
            Intent intent = new Intent(SplashAcitivity.this, LoginSignUpActivity.class);
            startActivity(intent);
            finish();
        }
    }
}
