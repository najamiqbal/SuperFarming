package com.softsolstudio.superfarming.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.softsolstudio.superfarming.R;
import com.softsolstudio.superfarming.activities.MessageActivity;

public class FarmerServiceVeiwFragment extends Fragment {
    View view;
    String userId="",userName="",userEmail="",userMobile="",userFirebade="",userAddress="",serviceTitle="",serviceDesc="",servicePrice="",serviceImage="";
    TextView tv_Username,tv_Usermobile,tv_UserAddress,tv_serviceTitle,tv_serviceDesc,tv_servicePrice;
    ImageView service_image;
    Button start_btn_chat;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.farmer_service_view_fragment,container,false);
        if (getArguments()!=null){
            userId=getArguments().getString("user_id");
            userName=getArguments().getString("user_name");
            userMobile=getArguments().getString("user_mobile");
            userFirebade=getArguments().getString("user_firebase");
            userAddress=getArguments().getString("user_address");
            serviceTitle=getArguments().getString("service_title");
            serviceDesc=getArguments().getString("service_desc");
            servicePrice=getArguments().getString("service_price");
            serviceImage=getArguments().getString("service_image");
        }
        Log.d("FarmerServiceView","image"+serviceImage+" "+userName+""+userAddress+" "+serviceDesc+" "+serviceTitle);
        initialization();
        return view;
    }

    private void initialization() {
        tv_serviceDesc=view.findViewById(R.id.n_tv_serviceDesc);
        tv_servicePrice=view.findViewById(R.id.n_tv_servicePrice);
        tv_serviceTitle=view.findViewById(R.id.n_tv_servicetitle);
        tv_Username=view.findViewById(R.id.n_tv_name);
        service_image=view.findViewById(R.id.n_iv_itemPic);
        tv_UserAddress=view.findViewById(R.id.n_tv_useraddress);
        tv_Usermobile=view.findViewById(R.id.n_tv_userMobile);
        start_btn_chat=view.findViewById(R.id.startChat_btn);
        start_btn_chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), MessageActivity.class);
                intent.putExtra("userid", userFirebade);
                startActivity(intent);
            }
        });
        tv_Usermobile.setText(userMobile);
        tv_Username.setText(userName);
        tv_UserAddress.setText(userAddress);
        tv_serviceTitle.setText(serviceTitle);
        tv_serviceDesc.setText(serviceDesc);
        tv_servicePrice.setText(servicePrice);
        Glide.with(getContext()).load(serviceImage).dontAnimate().fitCenter().placeholder(R.drawable.applogo).into(service_image);

    }
}
