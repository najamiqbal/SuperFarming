package com.softsolstudio.superfarming.fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.softsolstudio.superfarming.R;
import com.softsolstudio.superfarming.utils.VolleyRequestsent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ResetPassFragment extends Fragment {
    View view;
    EditText NewPass,ConfirmPass;
    Button update_btn;
    private ProgressDialog pDialog;
    String mobile="";
    String resetPass_url = "http://jugnoosinternational.com/android-api/Api.php?action=forgetPassword";
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.rest_pass_fragment,container,false);
        initialization();
        return view;
    }

    private void initialization() {
        pDialog = new ProgressDialog(getContext());
        pDialog.setCancelable(false);
        if (getArguments()!=null){
            mobile=getArguments().getString("mobile");
        }
        NewPass=view.findViewById(R.id.NewPass);
        ConfirmPass=view.findViewById(R.id.ConfirmPass);
        update_btn=view.findViewById(R.id.btn_Update_password);
        update_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!NewPass.getText().toString().isEmpty() && !ConfirmPass.getText().toString().isEmpty() && !mobile.isEmpty()){
                    if (NewPass.getText().toString().equals(ConfirmPass.getText().toString())){
                        updatePass(NewPass.getText().toString().trim(),mobile);
                    }
                }
            }
        });


    }
    private void updatePass(final String password, final String mobile) {
        Log.d("","MOBILE NUMBER"+ this.mobile +"\n"+password);
        StringRequest stringRequest=new StringRequest(Request.Method.POST, resetPass_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray=new JSONArray(response);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                        if (jsonObject.getString("status").equals("true")) {
                            pDialog.dismiss();
                            Toast.makeText(getContext(), "Password Updated", Toast.LENGTH_SHORT).show();
                            getActivity().getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                            LoginFragment fragment=new LoginFragment();
                            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                            fragmentTransaction.replace(R.id.fragment_container, fragment);
                            fragmentTransaction.commit();

                        } else {
                            pDialog.dismiss();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    pDialog.dismiss();
                    Toast.makeText(getContext(), "erro catch "+e.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), "Some Error", Toast.LENGTH_SHORT).show();
                pDialog.dismiss();

            }
        }){
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> param=new HashMap<String,String>();
                param.put("password",password);
                param.put("mobile", mobile);
                return param;
            }
        };
        VolleyRequestsent.getInstance().addRequestQueue(stringRequest);
    }
}
