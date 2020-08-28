package com.softsolstudio.farmertrader.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.softsolstudio.farmertrader.R;
import com.softsolstudio.farmertrader.activities.WeatherActivity;

public class ProviderHomeFragment extends Fragment implements View.OnClickListener{
    View view;
    CardView weather_card,cropsrate_card,crops_detail;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.provider_home_fragment,container,false);
        initialization();
        return view;
    }

    private void initialization() {
        FloatingActionButton fab = view.findViewById(R.id.Saddservice);
        weather_card=view.findViewById(R.id.Sweather_cardview);
        cropsrate_card=view.findViewById(R.id.SmarketRates_cardview);
        crops_detail=view.findViewById(R.id.Scrops_cardview);
        weather_card.setOnClickListener(this);
        crops_detail.setOnClickListener(this);
        cropsrate_card.setOnClickListener(this);
        fab.setOnClickListener(this);

    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        getActivity().setTitle("Home");
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.Sweather_cardview:
                Intent intent=new Intent(getContext(), WeatherActivity.class);
                startActivity(intent);
                break;
            case R.id.Scrops_cardview:
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://plantix.net/plant-disease/en/")));
                break;
            case R.id.SmarketRates_cardview:
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.amis.pk/Reports/DistrictMajor.aspx")));
                break;
            case R.id.Saddservice:
                ServiceProviderCategory Fragment = new ServiceProviderCategory();
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.provider_main_frame, Fragment);
                fragmentTransaction.addToBackStack("fragment");
                fragmentTransaction.commit();
                break;
        }
    }
}
