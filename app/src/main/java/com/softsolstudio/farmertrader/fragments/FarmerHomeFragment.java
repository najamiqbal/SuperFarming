package com.softsolstudio.farmertrader.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.softsolstudio.farmertrader.R;
import com.softsolstudio.farmertrader.activities.WeatherActivity;

public class FarmerHomeFragment extends Fragment implements View.OnClickListener{
    View view;
    CardView weather_card,service_card,traders_card,crops_detail;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.farmer_home_fragment,container,false);
        initialization();
        return view;
    }

    private void initialization() {
        weather_card=view.findViewById(R.id.weather_cardview);
        service_card=view.findViewById(R.id.service_cardview);
        traders_card=view.findViewById(R.id.traders_cardview);
        crops_detail=view.findViewById(R.id.crops_cardview);
        weather_card.setOnClickListener(this);
        service_card.setOnClickListener(this);
        traders_card.setOnClickListener(this);
        crops_detail.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.weather_cardview:
                Intent intent=new Intent(getContext(), WeatherActivity.class);
                startActivity(intent);
                break;
            case R.id.service_cardview:
                UserServicesFragment Fragment = new UserServicesFragment();
                Bundle args_data = new Bundle();
                args_data.putString("TypeValue", "Service provider");
                Fragment.setArguments(args_data);
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.main_frame, Fragment);
                fragmentTransaction.addToBackStack("forgetpass_fragment");
                fragmentTransaction.commit();
                break;
            case R.id.traders_cardview:
                UserServicesFragment Fragment2 = new UserServicesFragment();
                Bundle argsdata = new Bundle();
                argsdata.putString("TypeValue", "Trader");
                Fragment2.setArguments(argsdata);
                FragmentTransaction fragmentTransaction2 = getFragmentManager().beginTransaction();
                fragmentTransaction2.replace(R.id.main_frame, Fragment2);
                fragmentTransaction2.addToBackStack("forgetpass_fragment");
                fragmentTransaction2.commit();
                break;
            case R.id.crops_cardview:
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://plantix.net/plant-disease/en/")));
                break;
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        getActivity().setTitle("Home");
        super.onViewCreated(view, savedInstanceState);
    }
}
