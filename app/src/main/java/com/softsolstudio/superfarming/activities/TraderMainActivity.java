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
import com.softsolstudio.superfarming.fragments.CropsMarketRates;
import com.softsolstudio.superfarming.fragments.ProviderHomeFragment;
import com.softsolstudio.superfarming.fragments.TraderChatFragment;
import com.softsolstudio.superfarming.fragments.TraderHomeFragment;
import com.softsolstudio.superfarming.fragments.TraderServicesFragment;
import com.softsolstudio.superfarming.fragments.UserProfile;
import com.softsolstudio.superfarming.models.UserModelClass;
import com.softsolstudio.superfarming.utils.SharedPrefManager;

public class TraderMainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    Toolbar toolbar;
    TextView ownername, ownermail;
    ImageView userPhoto;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.trader_activity);
        toolbar = findViewById(R.id.toolbar_trader);
        //toolbar.setTitle("Saller Home");
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.trader_drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.trader_nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        ownername = navigationView.getHeaderView(0).findViewById(R.id.trader_name);
        ownermail = navigationView.getHeaderView(0).findViewById(R.id.trader_email);
        userPhoto = navigationView.getHeaderView(0).findViewById(R.id.trader_imageView);
        final UserModelClass userModelClass = SharedPrefManager.getInstance(TraderMainActivity.this).getUser();
        if (userModelClass != null) {
            ownername.setText(userModelClass.getUser_name());
            ownermail.setText(userModelClass.getUser_email());
            Glide.with(TraderMainActivity.this).load(userModelClass.getUser_photo()).dontAnimate().fitCenter().placeholder(R.drawable.ic_profile_pic).into(userPhoto);
        }

        //I added this if statement to keep the selected fragment when rotating the device
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.trader_main_frame,
                    new TraderHomeFragment()).commit();
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.trader_drawer_layout);
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
        if (id == R.id.trader_profile) {
            getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            getSupportFragmentManager().beginTransaction().replace(R.id.trader_main_frame,
                    new UserProfile()).addToBackStack("fragment").commit();
        } else if (id == R.id.trader_home) {
            getSupportFragmentManager().beginTransaction().replace(R.id.trader_main_frame,
                    new TraderHomeFragment()).commit();

        } else if (id == R.id.trader_message) {
            getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            getSupportFragmentManager().beginTransaction().replace(R.id.trader_main_frame,
                    new TraderChatFragment()).addToBackStack("fragment").commit();

        } else if (id == R.id.trader_services) {
            getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            getSupportFragmentManager().beginTransaction().replace(R.id.trader_main_frame,
                    new TraderServicesFragment()).addToBackStack("fragment").commit();
        }
        else if (id == R.id.trader_nav_contact) {
            getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            getSupportFragmentManager().beginTransaction().replace(R.id.trader_main_frame,
                    new ContactUsFragment()).addToBackStack("fragment").commit();

        }else if (id == R.id.trader_nav_about) {
            getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            getSupportFragmentManager().beginTransaction().replace(R.id.trader_main_frame,
                    new AboutUsFragment()).addToBackStack("fragment").commit();
        }else if (id == R.id.trader_weather) {
            Intent intent=new Intent(TraderMainActivity.this, WeatherActivity.class);
            startActivity(intent);
        }
        else if (id == R.id.trader_logout) {
            SharedPrefManager.getInstance(TraderMainActivity.this).logOut();
            startActivity(new Intent(this, LoginSignUpActivity.class));
            this.finish();
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.trader_drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
