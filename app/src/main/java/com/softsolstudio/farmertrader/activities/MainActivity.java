package com.softsolstudio.farmertrader.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.MenuItem;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.softsolstudio.farmertrader.R;
import com.softsolstudio.farmertrader.fragments.AboutUsFragment;
import com.softsolstudio.farmertrader.fragments.ContactUsFragment;
import com.softsolstudio.farmertrader.fragments.CropsMarketRates;
import com.softsolstudio.farmertrader.fragments.FarmerHomeFragment;
import com.softsolstudio.farmertrader.fragments.UserProfile;
import com.softsolstudio.farmertrader.fragments.farmerChatFragment;
import com.softsolstudio.farmertrader.models.UserModelClass;
import com.softsolstudio.farmertrader.utils.SharedPrefManager;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    TextView ownername, ownermail;
    ImageView userPhoto;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        this.showFragment(savedInstanceState);
        ownername = navigationView.getHeaderView(0).findViewById(R.id.farmerName);
        ownermail = navigationView.getHeaderView(0).findViewById(R.id.farmerEmail);
        userPhoto = navigationView.getHeaderView(0).findViewById(R.id.imageViewfarmer);
        final UserModelClass userModelClass = SharedPrefManager.getInstance(MainActivity.this).getUser();
        if (userModelClass != null) {
            ownername.setText(userModelClass.getUser_name());
            ownermail.setText(userModelClass.getUser_email());
            Glide.with(MainActivity.this).load(userModelClass.getUser_photo()).dontAnimate().fitCenter().placeholder(R.drawable.ic_profile_pic).into(userPhoto);
        }

    }
    private void showFragment(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.main_frame, new FarmerHomeFragment(), null)
                    .commit();
        }
    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.main_frame, new FarmerHomeFragment(), null)
                    .commit();
            // Handle the camera action
        }
/*        else if (id == R.id.nav_weather) {
            Intent intent=new Intent(MainActivity.this, WeatherActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_chat) {
            getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            getSupportFragmentManager().beginTransaction().replace(R.id.main_frame,
                    new farmerChatFragment()).addToBackStack("fragment").commit();

        } */
        else if (id == R.id.nav_rate_list) {
/*            getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            getSupportFragmentManager().beginTransaction().replace(R.id.main_frame,
                    new CropsMarketRates()).addToBackStack("fragment").commit();*/
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.amis.pk/Reports/DistrictMajor.aspx")));


        } else if (id == R.id.nav_user_profile) {
            getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            getSupportFragmentManager().beginTransaction().replace(R.id.main_frame,
                    new UserProfile()).addToBackStack("fragment").commit();

        } else if (id == R.id.nav_crops_detail) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://plantix.net/plant-disease/en/")));
        }else if (id == R.id.nav_contact_us) {
            getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            getSupportFragmentManager().beginTransaction().replace(R.id.main_frame,
                    new ContactUsFragment()).addToBackStack("fragment").commit();
        }else if (id == R.id.nav_about_us) {
            getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            getSupportFragmentManager().beginTransaction().replace(R.id.main_frame,
                    new AboutUsFragment()).addToBackStack("fragment").commit();
        } else if (id == R.id.nav_logout) {
            SharedPrefManager.getInstance(MainActivity.this).logOut();
            startActivity(new Intent(this, LoginSignUpActivity.class));
            this.finish();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
