package com.softsolstudio.superfarming.adapters;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.softsolstudio.superfarming.R;
import com.softsolstudio.superfarming.fragments.AddNewServiceFragment;
import com.softsolstudio.superfarming.fragments.FarmerServiceVeiwFragment;
import com.softsolstudio.superfarming.fragments.UserServiceViewFragment;
import com.softsolstudio.superfarming.models.UserModelClass;
import com.softsolstudio.superfarming.models.UserServicesModel;
import com.softsolstudio.superfarming.utils.SharedPrefManager;

import java.util.ArrayList;

public class UserServicesAdapter extends RecyclerView.Adapter<UserServicesAdapter.ViewHolder> {
    Context context;
    ArrayList<UserServicesModel> arrayList;
    public UserServicesAdapter(Context context, ArrayList<UserServicesModel> itemList) {
    this.context=context;
    this.arrayList=itemList;
    }

    @NonNull
    @Override
    public UserServicesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.user_services_row,viewGroup,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserServicesAdapter.ViewHolder viewHolder, int i) {
        final UserServicesModel model=arrayList.get(i);
        viewHolder.product_title.setText(model.getSub_category());
        viewHolder.product_desc.setText(model.getDescription());
        Glide.with(context).load(model.getService_image()).dontAnimate().fitCenter().placeholder(R.drawable.applogo).into(viewHolder.imageView);
        viewHolder.product_cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UserModelClass userModelClass= SharedPrefManager.getInstance(context).getUser();
                if (userModelClass!=null){
                    if (TextUtils.equals(userModelClass.getUser_type(),"Farmer")){
                        Log.d("FarmerServiceView","image adapter class"+model.getUsername());
                        AppCompatActivity activity = (AppCompatActivity) view.getContext();
                        Fragment myFragment = new FarmerServiceVeiwFragment();
                        Bundle args_data = new Bundle();
                        args_data.putString("user_id", model.getUser_id());
                        args_data.putString("user_name", model.getUsername());
                        args_data.putString("user_mobile", model.getMobile());
                        args_data.putString("user_address", model.getAddress());
                        args_data.putString("user_firebase", model.getFirebase_id());
                        args_data.putString("service_title", model.getSub_category());
                        args_data.putString("service_desc", model.getDescription());
                        args_data.putString("service_price", model.getPrice());
                        args_data.putString("service_image", model.getService_image());
                        myFragment.setArguments(args_data);
                        activity.getSupportFragmentManager().beginTransaction().replace(R.id.main_frame, myFragment).addToBackStack(null).commit();
                    }else if (TextUtils.equals(userModelClass.getUser_type(),"Trader")){
                        Log.d("FarmerServiceView","image adapter class"+model.getUsername());
                        AppCompatActivity activity = (AppCompatActivity) view.getContext();
                        Fragment myFragment = new UserServiceViewFragment();
                        Bundle args_data = new Bundle();
                        args_data.putString("service_id", model.getService_id());
                        args_data.putString("service_title", model.getSub_category());
                        args_data.putString("service_desc", model.getDescription());
                        args_data.putString("service_price", model.getPrice());
                        args_data.putString("service_image", model.getService_image());
                        myFragment.setArguments(args_data);
                        activity.getSupportFragmentManager().beginTransaction().replace(R.id.trader_main_frame, myFragment).addToBackStack(null).commit();
                    }else if (TextUtils.equals(userModelClass.getUser_type(),"Service provider")){
                        Log.d("FarmerServiceView","image adapter class"+model.getUsername());
                        AppCompatActivity activity = (AppCompatActivity) view.getContext();
                        Fragment myFragment = new UserServiceViewFragment();
                        Bundle args_data = new Bundle();
                        args_data.putString("service_id", model.getService_id());
                        args_data.putString("service_category", model.getService_category());
                        args_data.putString("service_sub", model.getSub_category());
                        args_data.putString("service_desc", model.getDescription());
                        args_data.putString("service_price", model.getPrice());
                        args_data.putString("service_image", model.getService_image());
                        myFragment.setArguments(args_data);
                        activity.getSupportFragmentManager().beginTransaction().replace(R.id.provider_main_frame, myFragment).addToBackStack(null).commit();

                    }
                }
            }
        });
        viewHolder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showImage(model.getService_image());
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CardView product_cardView;
        TextView product_title,product_desc;
        ImageView imageView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            product_cardView=itemView.findViewById(R.id.user_services_cardview);
            product_title=itemView.findViewById(R.id.servicetitle);
            imageView=itemView.findViewById(R.id.imageViewService);
            product_desc=itemView.findViewById(R.id.tv_servicedesc);
        }
    }
    public void showImage(String imageUri) {
        Dialog builder = new Dialog(context);
        builder.requestWindowFeature(Window.FEATURE_NO_TITLE);
        builder.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));
        builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                //nothing;
            }
        });

        ImageView imageView = new ImageView(context);
        //imageView.setImageURI(imageUri);
        if (imageUri != null) {
            Glide.with(context).load(imageUri).dontAnimate().fitCenter().placeholder(R.drawable.applogo).into(imageView);
        }
        //Picasso.get().load(imageUri).placeholder(R.drawable.logo).into(imageView);
        builder.addContentView(imageView, new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        builder.show();
    }
}
