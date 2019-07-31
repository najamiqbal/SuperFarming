package com.softsolstudio.superfarming.fragments;

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

public class UserServiceViewFragment extends Fragment implements View.OnClickListener{
    View view;
    TextView tv_serviceCategory,tv_serviceSubcategory,tv_servicePrice,tv_serviceDesc;
    ImageView service_image;
    Button editService_btn,deleteService_btn;
    String userId="",serviceTitle="",serviceCategory="",serviceDesc="",servicePrice="",serviceImage="",serviceId="";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.user_service_view_fragment,container,false);
        if (getArguments()!=null){
            serviceId=getArguments().getString("service_id");
            serviceTitle=getArguments().getString("service_category");
            serviceCategory=getArguments().getString("service_sub");
            serviceDesc=getArguments().getString("service_desc");
            servicePrice=getArguments().getString("service_price");
            serviceImage=getArguments().getString("service_image");
            Log.d("FarmerServiceView","image"+serviceImage+" "+serviceId+" "+serviceTitle+" "+serviceDesc+" "+serviceCategory);
        }
        initialization();
        return view;
    }

    private void initialization() {
        tv_serviceCategory=view.findViewById(R.id.u_tv_servicecategory);
        tv_serviceDesc=view.findViewById(R.id.u_tv_servicedesc);
        tv_servicePrice=view.findViewById(R.id.u_tv_serviceprice);
        tv_serviceSubcategory=view.findViewById(R.id.u_tv_serviceSubcategory);
        service_image=view.findViewById(R.id.u_iv_itemPic);
        editService_btn=view.findViewById(R.id.editService_btn);
        deleteService_btn=view.findViewById(R.id.deleteService_btn);
        tv_serviceDesc.setText(serviceDesc);
        tv_serviceSubcategory.setText(serviceCategory);
        tv_servicePrice.setText(servicePrice);
        tv_serviceCategory.setText(serviceTitle);
        Glide.with(getContext()).load(serviceImage).dontAnimate().fitCenter().placeholder(R.drawable.applogo).into(service_image);
        editService_btn.setOnClickListener(this);
        deleteService_btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.deleteService_btn:

                break;
            case R.id.editService_btn:

                break;
        }
    }
}
