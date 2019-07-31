package com.softsolstudio.superfarming.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.softsolstudio.superfarming.R;
import com.softsolstudio.superfarming.adapters.ServiceProviderCategorAdapter;
import com.softsolstudio.superfarming.adapters.ServiceProviderSubCategorAdapter;
import com.softsolstudio.superfarming.models.SubCategoryModel;
import com.softsolstudio.superfarming.models.categoryModel;

import java.util.ArrayList;

public class ServiceProviderSubCategory extends Fragment {
    View view;
    RecyclerView recyclerView;
    ArrayList<SubCategoryModel> ItemList;
    String service_category="";
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.service_provider_sub_category,container,false);
        Bundle bundl=getArguments();
        if (bundl!= null){
            service_category=getArguments().getString("Type");
            Log.d("singin","Category is"+service_category);
        }else {
            Toast.makeText(getActivity(), "Basic info not save", Toast.LENGTH_SHORT).show();
        }
        initialization();
        return view;
    }
    private void initialization() {
        Log.d("singin","Category is"+service_category);
        recyclerView = view.findViewById(R.id.S_sub_recycler_category);
        //LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        GridLayoutManager gridLayoutManager=new GridLayoutManager(getActivity(),2);
        recyclerView.setLayoutManager(gridLayoutManager);
        if (TextUtils.equals(service_category,"Cultivation")){
            ItemList = new ArrayList<SubCategoryModel>();
            ItemList.add(new SubCategoryModel("Cultivation","Disc Harrow",R.drawable.ic_fertilizer));
            ItemList.add(new SubCategoryModel("Cultivation","Rotavator",R.drawable.ic_fertilizer));
            ItemList.add(new SubCategoryModel("Cultivation","Tillers",R.drawable.ic_fertilizer));
            ItemList.add(new SubCategoryModel("Cultivation","Reeper",R.drawable.ic_fertilizer));
            ItemList.add(new SubCategoryModel("Cultivation","Wheat thrasher",R.drawable.ic_fertilizer));
            ItemList.add(new SubCategoryModel("Cultivation","Border disc",R.drawable.ic_fertilizer));
            ItemList.add(new SubCategoryModel("Cultivation","Speed drill",R.drawable.ic_fertilizer));
            if (ItemList != null) {
                Log.d("done","withlist");
                ServiceProviderSubCategorAdapter Adapter=new ServiceProviderSubCategorAdapter(getActivity(),ItemList);
                recyclerView.setAdapter(Adapter);
            } else {
                Toast.makeText(getActivity(), "No data", Toast.LENGTH_SHORT).show();
            }
        }else if (TextUtils.equals(service_category,"Harvest")){
            ItemList = new ArrayList<SubCategoryModel>();
            ItemList.add(new SubCategoryModel("Harvest","Ptato harvester",R.drawable.ic_fertilizer));
            ItemList.add(new SubCategoryModel("Harvest","Wheat harvester",R.drawable.ic_fertilizer));
            ItemList.add(new SubCategoryModel("Harvest","Rice harvester",R.drawable.ic_fertilizer));
            ItemList.add(new SubCategoryModel("Harvest","Stravester",R.drawable.ic_fertilizer));
            ItemList.add(new SubCategoryModel("Harvest","Corn harvester",R.drawable.ic_fertilizer));
            if (ItemList != null) {
                Log.d("done","withlist");
                ServiceProviderSubCategorAdapter Adapter=new ServiceProviderSubCategorAdapter(getActivity(),ItemList);
                recyclerView.setAdapter(Adapter);
            } else {
                Toast.makeText(getActivity(), "No data", Toast.LENGTH_SHORT).show();
            }

        }else if (TextUtils.equals(service_category,"Transport")){
            ItemList = new ArrayList<SubCategoryModel>();
            ItemList.add(new SubCategoryModel("Transport","Farm trolley",R.drawable.ic_fertilizer));
            ItemList.add(new SubCategoryModel("Transport","Container",R.drawable.ic_fertilizer));
            ItemList.add(new SubCategoryModel("Transport","Open troller",R.drawable.ic_fertilizer));
            if (ItemList != null) {
                Log.d("done","withlist");
                ServiceProviderSubCategorAdapter Adapter=new ServiceProviderSubCategorAdapter(getActivity(),ItemList);
                recyclerView.setAdapter(Adapter);
            } else {
                Toast.makeText(getActivity(), "No data", Toast.LENGTH_SHORT).show();
            }

        }else if (TextUtils.equals(service_category,"Labour")){
            AddNewServiceFragment Fragment = new AddNewServiceFragment();
            Bundle args_data = new Bundle();
            args_data.putString("Type", service_category);
            Fragment.setArguments(args_data);
            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.provider_main_frame, Fragment);
            fragmentTransaction.addToBackStack("forgetpass_fragment");
            fragmentTransaction.commit();

        }else if (TextUtils.equals(service_category,"Add New")){
            AddNewServiceFragment Fragment = new AddNewServiceFragment();
            Bundle args_data = new Bundle();
            args_data.putString("Type", service_category);
            Fragment.setArguments(args_data);
            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.provider_main_frame, Fragment);
            fragmentTransaction.addToBackStack("forgetpass_fragment");
            fragmentTransaction.commit();
        }else if (service_category==null){

            getFragmentManager().popBackStack();
        }
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        getActivity().setTitle(service_category);
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        getArguments().clear();
    }
}
