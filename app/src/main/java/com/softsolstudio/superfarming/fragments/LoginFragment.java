package com.softsolstudio.superfarming.fragments;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.softsolstudio.superfarming.R;
import com.softsolstudio.superfarming.activities.MainActivity;
import com.softsolstudio.superfarming.activities.ServiceProviderActivity;
import com.softsolstudio.superfarming.activities.TraderMainActivity;
import com.softsolstudio.superfarming.models.UserModelClass;
import com.softsolstudio.superfarming.utils.AppUtils;
import com.softsolstudio.superfarming.utils.SharedPrefManager;
import com.softsolstudio.superfarming.utils.VolleyRequestsent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginFragment extends Fragment {
    View view;
    UserModelClass userModelClass = new UserModelClass();
    private ProgressDialog pDialog;
    String Login_url = "http://jugnoosinternational.com/android-api/Api.php?action=loginUser";
    Button loginbtn,loginbtn_guest;
    TextView forgetpass, registration;
    EditText mobile, pass;
    RadioGroup radioGroup;
    String phone, password;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.login_fragment,container,false);
        initializeVariable();
        return view;
    }

    private void initializeVariable() {
        pDialog = new ProgressDialog(getContext());
        pDialog.setCancelable(false);
        mobile = view.findViewById(R.id.mobile_number);
        pass = view.findViewById(R.id.password);
        loginbtn = view.findViewById(R.id.loginbtn);
        registration = view.findViewById(R.id.newuser);
        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppUtils.hideSoftKeyboard(getActivity());
                if (validate()) {
                    UserLogin(phone, password);
                }
            }
        });
        forgetpass = view.findViewById(R.id.forgetpass);
        forgetpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ForgetPassFragment forgetPassFragment = new ForgetPassFragment();
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, forgetPassFragment);
                fragmentTransaction.addToBackStack("forgetpass_fragment");
                fragmentTransaction.commit();
            }
        });
        registration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SignUpFragment signUpFragment = new SignUpFragment();
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, signUpFragment);
                fragmentTransaction.addToBackStack("forgetpass_fragment");
                fragmentTransaction.commit();
            }
        });
    }

    private void UserLogin(final String phone, final String password) {
        pDialog.setMessage("Login please Wait....");
        pDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Login_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Response is", response.toString());
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                        //2 is use for saller account
                        if (jsonObject.getString("status").equals("true")){
                            //1 for farmer
                            if (jsonObject.getString("user_type").equals("Farmer")) {

                                userModelClass.setUser_name(jsonObject.getString("username"));
                                userModelClass.setUser_mobile(jsonObject.getString("mobile"));
                                userModelClass.setUser_email(jsonObject.getString("email"));
                                userModelClass.setUser_address(jsonObject.getString("address"));
                                userModelClass.setUser_password(jsonObject.getString("password"));
                                userModelClass.setUser_photo(jsonObject.getString("image"));
                                userModelClass.setUserfirebase_id(jsonObject.getString("firebase_id"));
                                userModelClass.setUser_type(jsonObject.getString("user_type"));
                                userModelClass.setUser_id(jsonObject.getString("id"));

                                if (SharedPrefManager.getInstance(getContext()).addUserToPref(userModelClass)) {
                                    pDialog.dismiss();
                                    Intent intent = new Intent(getContext(), MainActivity.class);
                                    startActivity(intent);
                                    getActivity().finish();
                                } else {
                                    pDialog.dismiss();
                                    Toast.makeText(getContext(), "Login Failed", Toast.LENGTH_SHORT).show();
                                }

                            }
                            //2 is use for trader account
                            else if (jsonObject.getString("user_type").equals("Service provider")){

                                userModelClass.setUser_name(jsonObject.getString("username"));
                                userModelClass.setUser_mobile(jsonObject.getString("mobile"));
                                userModelClass.setUser_email(jsonObject.getString("email"));
                                userModelClass.setUser_address(jsonObject.getString("address"));
                                userModelClass.setUser_password(jsonObject.getString("password"));
                                userModelClass.setUser_photo(jsonObject.getString("image"));
                                userModelClass.setUser_type(jsonObject.getString("user_type"));
                                userModelClass.setUserfirebase_id(jsonObject.getString("firebase_id"));
                                userModelClass.setUser_id(jsonObject.getString("id"));

                                if (SharedPrefManager.getInstance(getContext()).addUserToPref(userModelClass)) {
                                    pDialog.dismiss();
                                    Intent intent = new Intent(getContext(), ServiceProviderActivity.class);
                                    startActivity(intent);
                                    getActivity().finish();
                                } else {
                                    pDialog.dismiss();
                                    Toast.makeText(getContext(), "Login Failed", Toast.LENGTH_SHORT).show();
                                }
                            }
                            //3 for service provider
                            else if (jsonObject.getString("user_type").equals("Trader")){

                                userModelClass.setUser_name(jsonObject.getString("username"));
                                userModelClass.setUser_mobile(jsonObject.getString("mobile"));
                                userModelClass.setUser_email(jsonObject.getString("email"));
                                userModelClass.setUser_address(jsonObject.getString("address"));
                                userModelClass.setUser_password(jsonObject.getString("password"));
                                userModelClass.setUser_photo(jsonObject.getString("image"));
                                userModelClass.setUser_type(jsonObject.getString("user_type"));
                                userModelClass.setUserfirebase_id(jsonObject.getString("firebase_id"));
                                userModelClass.setUser_id(jsonObject.getString("id"));

                                if (SharedPrefManager.getInstance(getContext()).addUserToPref(userModelClass)) {
                                    pDialog.dismiss();
                                    Intent intent = new Intent(getContext(), TraderMainActivity.class);
                                    startActivity(intent);
                                    getActivity().finish();
                                } else {
                                    pDialog.dismiss();
                                    Toast.makeText(getContext(), "Login Failed", Toast.LENGTH_SHORT).show();
                                }
                            }

                        }else {
                            pDialog.dismiss();
                            Toast.makeText(getContext(), "Please try again", Toast.LENGTH_SHORT).show();
                        }
                    }

                } catch (JSONException e) {
                    pDialog.dismiss();
                    e.printStackTrace();
                    Toast.makeText(getContext(), "error"+e.getMessage(), Toast.LENGTH_SHORT).show();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pDialog.dismiss();
                Toast.makeText(getContext(), "Some Error Here", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("mobile", phone);
                params.put("password", password);
                return params;

            }
        };
        VolleyRequestsent.getInstance().addRequestQueue(stringRequest);
    }
    //Validating data
    private boolean validate() {
        boolean valid = true;
        phone = "+92"+mobile.getText().toString().trim();
        password = pass.getText().toString().trim();

        if (phone.isEmpty()) {
            mobile.setError("Please Enter Phone");
            valid = false;
        } else {
            mobile.setError(null);
        }

        if (password.isEmpty()) {
            pass.setError("Please Enter Correct Password");
            valid = false;
        } else {
            pass.setError(null);
        }

        return valid;
    }
}
