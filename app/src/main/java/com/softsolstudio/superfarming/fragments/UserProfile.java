package com.softsolstudio.superfarming.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.softsolstudio.superfarming.R;
import com.softsolstudio.superfarming.models.UserModelClass;
import com.softsolstudio.superfarming.utils.SharedPrefManager;

public class UserProfile extends Fragment {
    View view;
    TextView buyer_name,buyer_email,buyer_address,buyer_mobile,buyer_password;
    ImageView buyer_photo;
    Button buyer_editProfile_btn;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.user_profile,container,false);
        initialization();
        return view;
    }

    private void initialization() {
        buyer_address=view.findViewById(R.id.tv_buyer_address);
        buyer_email=view.findViewById(R.id.tv_buyer_email);
        buyer_mobile=view.findViewById(R.id.tv_buyer_mobile);
        buyer_name=view.findViewById(R.id.tv_buyer_name);
        buyer_password=view.findViewById(R.id.tv_buyer_password);
        buyer_photo=view.findViewById(R.id.IV_buyer_photo);
        buyer_editProfile_btn=view.findViewById(R.id.edit_buyer_profile_btn);
        buyer_editProfile_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UserModelClass userModelClass= SharedPrefManager.getInstance(getContext()).getUser();
                if (userModelClass!=null){
                    String userType=userModelClass.getUser_type();
                    if (TextUtils.equals(userType,"0")){
                        EditProfileFragment Fragment = new EditProfileFragment();
                        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.trader_main_frame, Fragment);
                        fragmentTransaction.addToBackStack("forgetpass_fragment");
                        fragmentTransaction.commit();
                    }else if (TextUtils.equals(userType,"1")){

                    }else if (TextUtils.equals(userType,"")){

                    }
                }



            }
        });
        bindProfileData();
    }
    private void bindProfileData() {
        UserModelClass userModelClass= SharedPrefManager.getInstance(getContext()).getUser();
        if (userModelClass!=null){
            buyer_name.setText(userModelClass.getUser_name());
            buyer_password.setText(userModelClass.getUser_password());
            buyer_mobile.setText(userModelClass.getUser_mobile());
            buyer_email.setText(userModelClass.getUser_email());
            buyer_address.setText(userModelClass.getUser_address());
            Glide.with(getContext()).load(userModelClass.getUser_photo()).dontAnimate().fitCenter().placeholder(R.drawable.ic_profile_pic).into(buyer_photo);

        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        getActivity().setTitle("Profile");
        super.onViewCreated(view, savedInstanceState);
    }
}
