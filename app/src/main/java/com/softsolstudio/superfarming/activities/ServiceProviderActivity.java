package com.softsolstudio.superfarming.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.softsolstudio.superfarming.R;
import com.softsolstudio.superfarming.fragments.AboutUsFragment;
import com.softsolstudio.superfarming.fragments.ContactUsFragment;
import com.softsolstudio.superfarming.fragments.ProviderChatFragment;
import com.softsolstudio.superfarming.fragments.ProviderHomeFragment;
import com.softsolstudio.superfarming.fragments.ProviderSerivesFragment;
import com.softsolstudio.superfarming.fragments.UserProfile;
import com.softsolstudio.superfarming.fragments.farmerChatFragment;
import com.softsolstudio.superfarming.models.UserModelClass;
import com.softsolstudio.superfarming.utils.SharedPrefManager;

public class ServiceProviderActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    Toolbar toolbar;
    TextView ownername, ownermail;
    ImageView userPhoto;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.service_provider_activity);
        toolbar = findViewById(R.id.toolbar_provider);
        //toolbar.setTitle("Saller Home");
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.serviceprovider_drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.owner_nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        ownername = navigationView.getHeaderView(0).findViewById(R.id.provider_name);
        ownermail = navigationView.getHeaderView(0).findViewById(R.id.provider_email);
        userPhoto = navigationView.getHeaderView(0).findViewById(R.id.provider_imageView);
        final UserModelClass userModelClass = SharedPrefManager.getInstance(ServiceProviderActivity.this).getUser();
        if (userModelClass != null) {
            ownername.setText(userModelClass.getUser_name());
            ownermail.setText(userModelClass.getUser_email());
            Glide.with(ServiceProviderActivity.this).load(userModelClass.getUser_photo()).dontAnimate().fitCenter().placeholder(R.drawable.ic_profile_pic).into(userPhoto);
        }

        //I added this if statement to keep the selected fragment when rotating the device
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.provider_main_frame,
                    new ProviderHomeFragment()).commit();
        }
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.serviceprovider_drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (getFragmentManager().getBackStackEntryCount() > 0) {
                getFragmentManager().popBackStack();
            }else {
                super.onBackPressed();
            }
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int id = menuItem.getItemId();
        if (id == R.id.provider_profile) {
            getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            getSupportFragmentManager().beginTransaction().replace(R.id.provider_main_frame,
                    new UserProfile()).addToBackStack("fragment").commit();
        } else if (id == R.id.provider_home) {
            getSupportFragmentManager().beginTransaction().replace(R.id.provider_main_frame,
                    new ProviderHomeFragment()).commit();

        } else if (id == R.id.provider_message) {
            getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            getSupportFragmentManager().beginTransaction().replace(R.id.provider_main_frame,
                    new ProviderChatFragment()).addToBackStack("fragment").commit();

        } else if (id == R.id.provider_services) {
            getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            getSupportFragmentManager().beginTransaction().replace(R.id.provider_main_frame,
                    new ProviderSerivesFragment()).addToBackStack("fragment").commit();
        }
        else if (id == R.id.provider_nav_contact) {
            getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            getSupportFragmentManager().beginTransaction().replace(R.id.provider_main_frame,
                    new ContactUsFragment()).addToBackStack("fragment").commit();

        }else if (id == R.id.provider_nav_about) {
            getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            getSupportFragmentManager().beginTransaction().replace(R.id.provider_main_frame,
                    new AboutUsFragment()).addToBackStack("fragment").commit();

        }else if (id == R.id.provider_weather) {
            Intent intent=new Intent(ServiceProviderActivity.this, WeatherActivity.class);
            startActivity(intent);
        }
        else if (id == R.id.provider_logout) {
            SharedPrefManager.getInstance(ServiceProviderActivity.this).logOut();
            startActivity(new Intent(this, LoginSignUpActivity.class));
            this.finish();
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.serviceprovider_drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
