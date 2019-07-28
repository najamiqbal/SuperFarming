package com.softsolstudio.superfarming.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.softsolstudio.superfarming.R;
import com.softsolstudio.superfarming.adapters.ServiceProviderCategorAdapter;
import com.softsolstudio.superfarming.adapters.TraderCategoryAdapter;
import com.softsolstudio.superfarming.models.categoryModel;

import java.util.ArrayList;

public class ServiceProviderCategory extends Fragment {
    View view;
    RecyclerView recyclerView;
    ArrayList<categoryModel> ItemList;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.service_provider_category,container,false);
        initialization();
        return view;
    }
    private void initialization() {
        recyclerView = view.findViewById(R.id.Srecycler_category);
        //LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        GridLayoutManager gridLayoutManager=new GridLayoutManager(getActivity(),2);

        recyclerView.setLayoutManager(gridLayoutManager);

        ItemList = new ArrayList<categoryModel>();
        ItemList.add(new categoryModel("Cultivation",R.drawable.ic_fertilizer));
        ItemList.add(new categoryModel("Harvest",R.drawable.ic_spray));
        ItemList.add(new categoryModel("Transport",R.drawable.ic_loan));
        ItemList.add(new categoryModel("Labour",R.drawable.ic_loan));
        ItemList.add(new categoryModel("Add New",R.drawable.ic_new));
        if (ItemList != null) {
            Log.d("done","withlist");
            ServiceProviderCategorAdapter Adapter=new ServiceProviderCategorAdapter(getActivity(),ItemList);
            recyclerView.setAdapter(Adapter);
        } else {
            Toast.makeText(getActivity(), "No data", Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        getActivity().setTitle("Choose Category");
        super.onViewCreated(view, savedInstanceState);
    }
}
