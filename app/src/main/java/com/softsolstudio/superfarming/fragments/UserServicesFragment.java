package com.softsolstudio.superfarming.fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.softsolstudio.superfarming.R;
import com.softsolstudio.superfarming.adapters.UserServicesAdapter;
import com.softsolstudio.superfarming.models.UserModelClass;
import com.softsolstudio.superfarming.models.UserServicesModel;
import com.softsolstudio.superfarming.utils.SharedPrefManager;
import com.softsolstudio.superfarming.utils.VolleyRequestsent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class UserServicesFragment extends Fragment {
    View view;
    RecyclerView recyclerView;
    ArrayList<UserServicesModel> ItemList;
    private ProgressDialog pDialog;
    UserServicesAdapter mAdapter;
    String getUserPostsUrl="";
    String Usertype_value="",Usertype_key="";
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.user_services_fragment,container,false);
        Bundle bundl=getArguments();
        if (bundl!= null){
            Usertype_value=getArguments().getString("TypeValue");
            Log.d("singin","Category is"+Usertype_value);
        }else {
            Toast.makeText(getActivity(), "Basic info not save", Toast.LENGTH_SHORT).show();
        }
        initialization();
        return view;
    }

    private void initialization() {
        pDialog = new ProgressDialog(getContext());
        pDialog.setCancelable(false);
        recyclerView = view.findViewById(R.id.user_recycler_services);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        //GridLayoutManager gridLayoutManager=new GridLayoutManager(MainActivity.this,2);
        ItemList = new ArrayList<>();
        recyclerView.setLayoutManager(linearLayoutManager);
        //if (!Usertype_value.isEmpty()){
            if (TextUtils.equals(Usertype_value,"Service provider")){
                Usertype_key="user_type";
                getUserPostsUrl="https://houseofsoftwares.com/android-api-2/Api.php?action=getServices";
                GetUserServices(Usertype_value,Usertype_key,getUserPostsUrl);
            }else if (TextUtils.equals(Usertype_value,"Trader")){
                Usertype_key="user_type";
                getUserPostsUrl="https://houseofsoftwares.com/android-api-2/Api.php?action=getServices";
                GetUserServices(Usertype_value,Usertype_key,getUserPostsUrl);
            }else {
                UserModelClass userModelClass= SharedPrefManager.getInstance(getContext()).getUser();
                if (userModelClass!=null){
                    Usertype_key="user_id";
                    getUserPostsUrl="https://houseofsoftwares.com/android-api-2/Api.php?action=getUserServices";
                    GetUserServices(userModelClass.getUser_id(),Usertype_key,getUserPostsUrl);
                }
            }
        //}

    }

    private void GetUserServices(final String user_id,final String key,final String api_url) {
        pDialog.setMessage("please Wait....");
        pDialog.show();
        Log.d("Response is", "CHECK RESPONSE"+user_id+" "+key+" "+api_url+" ");
        StringRequest stringRequest = new StringRequest(Request.Method.POST, api_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Response is", "CHECK RESPONSE"+response.toString());
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                        Log.d("status", "CHECK" + jsonObject.getString("status"));
                        if (jsonObject.getString("status").equals("true")) {
                            UserServicesModel model=new UserServicesModel();
                            model.setUser_id(jsonObject.getString("user_id"));
                            model.setService_id(jsonObject.getString("service_id"));
                            model.setUsername(jsonObject.getString("username"));
                            model.setMobile(jsonObject.getString("mobile"));
                            model.setEmail(jsonObject.getString("email"));
                            model.setAddress(jsonObject.getString("address"));
                            model.setUser_type(jsonObject.getString("user_type"));
                            model.setUserimage(jsonObject.getString("image"));
                            model.setFirebase_id(jsonObject.getString("firebase_id"));
                            model.setService_category(jsonObject.getString("service_category"));
                            model.setSub_category(jsonObject.getString("sub_category"));
                            model.setDescription(jsonObject.getString("description"));
                            model.setPrice(jsonObject.getString("price"));
                            model.setService_image(jsonObject.getString("service_image"));
                            ItemList.add(model);
                        } else {
                            pDialog.dismiss();
                            Toast.makeText(getContext(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                        }
                    }
                    pDialog.dismiss();
                    if (ItemList != null) {
                        mAdapter = new UserServicesAdapter(getContext(),ItemList);
                        recyclerView.setAdapter(mAdapter);
                    } else {
                        Toast.makeText(getContext(), "NO DATA", Toast.LENGTH_SHORT).show();
                    }
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
                params.put(key,user_id );
                return params;

            }
        };
        VolleyRequestsent.getInstance().addRequestQueue(stringRequest);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        getActivity().setTitle("Services");
        super.onViewCreated(view, savedInstanceState);
    }
}
