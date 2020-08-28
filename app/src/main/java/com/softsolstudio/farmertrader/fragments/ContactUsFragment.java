package com.softsolstudio.farmertrader.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.softsolstudio.farmertrader.R;
import com.softsolstudio.farmertrader.utils.AppConstants;

public class ContactUsFragment extends Fragment {
    View view;
    TextView textView;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.contact_us_fragment,container,false);
        textView=view.findViewById(R.id.tv_contacts);
        textView.setText(Html.fromHtml(AppConstants.contactUs));
        return view;
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        getActivity().setTitle("Contact Us");
        super.onViewCreated(view, savedInstanceState);
    }
}
