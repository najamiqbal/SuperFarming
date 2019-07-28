package com.softsolstudio.superfarming.adapters;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.softsolstudio.superfarming.R;
import com.softsolstudio.superfarming.fragments.AddNewServiceFragment;
import com.softsolstudio.superfarming.models.categoryModel;

import java.util.ArrayList;

public class TraderCategoryAdapter extends RecyclerView.Adapter<TraderCategoryAdapter.ViewHolder> {
    Context context;
    ArrayList<categoryModel> arrayList;
    public TraderCategoryAdapter(FragmentActivity activity, ArrayList<categoryModel> itemList) {
        this.context=activity;
        this.arrayList=itemList;
    }

    @NonNull
    @Override
    public TraderCategoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.category_row,viewGroup,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TraderCategoryAdapter.ViewHolder viewHolder, int i) {
        final categoryModel model=arrayList.get(i);
        viewHolder.title.setText(model.getType());
        viewHolder.imageView.setImageResource(model.getIcon());
        viewHolder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppCompatActivity activity = (AppCompatActivity) view.getContext();
                Fragment myFragment = new AddNewServiceFragment();
                Bundle args_data = new Bundle();
                args_data.putString("Type", model.getType());
                myFragment.setArguments(args_data);
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.trader_main_frame, myFragment).addToBackStack(null).commit();
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        TextView title;
        ImageView imageView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView=itemView.findViewById(R.id.cardview_category);
            title=itemView.findViewById(R.id.card_view_image_title);
            imageView=itemView.findViewById(R.id.card_view_image);
        }
    }
}
