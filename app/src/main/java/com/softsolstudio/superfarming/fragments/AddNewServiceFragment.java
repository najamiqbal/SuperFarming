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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.softsolstudio.superfarming.R;

public class AddNewServiceFragment extends Fragment {
    View view;
    String service_category="",service_sub_category="",service_desc="",service_price="",service_photo="",user_id="";
    EditText et_service_title,et_service_description,et_service_price;
    ImageView imageView;
    TextView uploadImage;
    Button add_btn;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.add_new_service_fragment,container,false);
        if (getArguments()!=null){
            service_category=getArguments().getString("Type");
            Log.d("singin","LOVE"+service_category);
        }else {
            Toast.makeText(getActivity(), "Basic info not save", Toast.LENGTH_SHORT).show();
        }
        initialization();
        return  view;
    }

    private void initialization() {
        et_service_title=view.findViewById(R.id.service_title);
        et_service_description=view.findViewById(R.id.service_desc);
        et_service_price=view.findViewById(R.id.service_price);
        imageView=view.findViewById(R.id.iv_service_photo);
        uploadImage=view.findViewById(R.id.tv_Upload_image);
        add_btn=view.findViewById(R.id.add_service_btn);
        add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        getActivity().setTitle(service_category);
        super.onViewCreated(view, savedInstanceState);
    }
}
