package com.softsolstudio.superfarming.fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.softsolstudio.superfarming.R;
import com.softsolstudio.superfarming.adapters.UserServicesAdapter;
import com.softsolstudio.superfarming.models.UserModelClass;
import com.softsolstudio.superfarming.models.UserServicesModel;
import com.softsolstudio.superfarming.utils.SharedPrefManager;
import com.softsolstudio.superfarming.utils.VolleyRequestsent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class UserServiceViewFragment extends Fragment implements View.OnClickListener {
    View view;
    TextView tv_serviceCategory, tv_serviceSubcategory, tv_servicePrice, tv_serviceDesc;
    ImageView service_image;
    Button editService_btn, deleteService_btn;
    String userId = "", serviceTitle = "", serviceCategory = "", serviceDesc = "", servicePrice = "", serviceImage = "", serviceId = "";
    String deleteService = "http://jugnoosinternational.com/android-api/Api.php?action=deleteService";
    private ProgressDialog pDialog;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.user_service_view_fragment, container, false);
        if (getArguments() != null) {
            serviceId = getArguments().getString("service_id");
            serviceTitle = getArguments().getString("service_category");
            serviceCategory = getArguments().getString("service_sub");
            serviceDesc = getArguments().getString("service_desc");
            servicePrice = getArguments().getString("service_price");
            serviceImage = getArguments().getString("service_image");
            Log.d("FarmerServiceView", "image" + serviceImage + " " + serviceId + " " + serviceTitle + " " + serviceDesc + " " + serviceCategory);
        }
        initialization();
        return view;
    }

    private void initialization() {
        pDialog = new ProgressDialog(getContext());
        pDialog.setCancelable(false);
        tv_serviceCategory = view.findViewById(R.id.u_tv_servicecategory);
        tv_serviceDesc = view.findViewById(R.id.u_tv_servicedesc);
        tv_servicePrice = view.findViewById(R.id.u_tv_serviceprice);
        tv_serviceSubcategory = view.findViewById(R.id.u_tv_serviceSubcategory);
        service_image = view.findViewById(R.id.u_iv_itemPic);
        editService_btn = view.findViewById(R.id.editService_btn);
        deleteService_btn = view.findViewById(R.id.deleteService_btn);
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
        switch (view.getId()) {
            case R.id.deleteService_btn:
                DeleteService(serviceId);
                break;
            case R.id.editService_btn:
                UserModelClass modelClass= SharedPrefManager.getInstance(getContext()).getUser();
                if (modelClass!=null){
                    if (TextUtils.equals(modelClass.getUser_type(),"Trader")){
                        EditUserServiceFragment Fragment = new EditUserServiceFragment();
                        Bundle args = new Bundle();
                        args.putString("service_id", serviceId);
                        args.putString("service_category", serviceCategory);
                        args.putString("service_sub", serviceTitle);
                        args.putString("service_desc", serviceDesc);
                        args.putString("service_price", servicePrice);
                        args.putString("service_image", serviceImage);
                        Fragment.setArguments(args);
                        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.trader_main_frame, Fragment);
                        fragmentTransaction.addToBackStack("forgetpass_fragment");
                        fragmentTransaction.commit();
                    }else if (TextUtils.equals(modelClass.getUser_type(),"Service provider")){
                        EditUserServiceFragment Fragment = new EditUserServiceFragment();
                        Bundle args = new Bundle();
                        args.putString("service_id", serviceId);
                        args.putString("service_category", serviceCategory);
                        args.putString("service_sub", serviceTitle);
                        args.putString("service_desc", serviceDesc);
                        args.putString("service_price", servicePrice);
                        args.putString("service_image", serviceImage);
                        Fragment.setArguments(args);
                        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.provider_main_frame, Fragment);
                        fragmentTransaction.addToBackStack("forgetpass_fragment");
                        fragmentTransaction.commit();
                    }
                }
                break;
        }
    }

    private void DeleteService(final String serviceId) {
        pDialog.setMessage("please Wait....");
        pDialog.show();
        Log.d("Response is", "CHECK RESPONSE" + serviceId);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, deleteService, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Response is", "CHECK RESPONSE" + response.toString());
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                        Log.d("status", "CHECK" + jsonObject.getString("status"));
                        if (jsonObject.getString("status").equals("true")) {
                            Toast.makeText(getContext(), "Deleted", Toast.LENGTH_SHORT).show();
                            getActivity().onBackPressed();
                        } else {
                            pDialog.dismiss();
                            Toast.makeText(getContext(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                        }
                    }
                    pDialog.dismiss();
                } catch (JSONException e) {
                    pDialog.dismiss();
                    e.printStackTrace();
                    Toast.makeText(getContext(), "error" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pDialog.dismiss();
                Toast.makeText(getContext(), "Some Error", Toast.LENGTH_SHORT).show();


            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("service_id", serviceId);
                return params;

            }
        };
        VolleyRequestsent.getInstance().addRequestQueue(stringRequest);
    }
}
