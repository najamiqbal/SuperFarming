package com.softsolstudio.farmertrader.fragments;

import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
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
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.softsolstudio.farmertrader.R;
import com.softsolstudio.farmertrader.utils.VolleyRequestsent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class VerifyCodeFragment extends Fragment {
    View view;
    Button verify;
    TextView timer;
    EditText Code;
    Handler handler;
    int count = 120;
    String verificationId, firebaseUserId = "", buyer_mobile = "", buyer_Name = "", buyer_Status = "", buyer_Password = "", buyer_Email = "", buyer_Address = "", buyer_Photo = "", saller_comapany_name = "", saller_comp_address = "", saller_comp_ntn = "",
            saller_comp_desc = "";
    String registration_url = "https://houseofsoftwares.com/android-api-2/Api.php?action=registerUser";
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    private FirebaseAuth mAuth;
    private ProgressDialog pDialog;
    //firebase database
    FirebaseAuth auth;
    DatabaseReference reference;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.verify_code_fragment, container, false);
        initialization();
        return view;
    }

    private void initialization() {
        auth = FirebaseAuth.getInstance();
        pDialog = new ProgressDialog(getContext());
        pDialog.setCancelable(false);
        verify = view.findViewById(R.id.btn_verify);
        timer = view.findViewById(R.id.resettimer);
        handler = new Handler(getActivity().getMainLooper());
        Code = view.findViewById(R.id.firebaseCode);
        if (getArguments() != null) {
            buyer_Status = getArguments().getString("Status");
            verificationId = getArguments().getString("ID");
            buyer_mobile = getArguments().getString("Mobile");
            buyer_Name = getArguments().getString("Name");
            buyer_Email = getArguments().getString("Email");
            buyer_Address = getArguments().getString("Address");
            buyer_Photo = getArguments().getString("Photo");
            buyer_Password = getArguments().getString("Password");
            Log.d("VerifyCode", "Buyer Data  " + buyer_Name);


        }
        Log.d("Mobile_registration", "data" + buyer_mobile + " photo " + buyer_Photo);
        mAuth = FirebaseAuth.getInstance();
        verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!Code.getText().toString().isEmpty()) {
                    PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, Code.getText().toString());
                    // [END verify_with_code]
                    signInWithPhoneAuthCredential(credential);
                } else {
                    Toast.makeText(getContext(), "Please Enter Verification Code", Toast.LENGTH_SHORT).show();
                }

            }
        });
        thread();
    }
    private void thread() {
        new Thread(new Runnable() {
            @Override
            public void run() {

                while (count >= 0) {
                    try {
                        Thread.sleep(1000);

                    } catch (InterruptedException e) {
                        Log.i("TAG", e.getMessage());
                    }
                    Log.i("TAG", "Thread id in while loop: " + Thread.currentThread().getId() + ", Count : " + count);
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            timer.setText("Seconds Left " + count);
                        }
                    });
                    if (count == 0) {
                        // getActivity().onBackPressed();
                        // save the changes
                    }
                    count--;
                }
            }
        }).start();
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getContext(), "Verification Done", Toast.LENGTH_SHORT).show();
                            if (!buyer_mobile.isEmpty()&& !buyer_Password.isEmpty()) {
                                register(buyer_Name,buyer_Email,buyer_Password);
                                /*if (TextUtils.equals(buyer_Status,"3")){
                                    register("najamdd","najamiqbal9@gmail.com","ok123456");
                                   // UserRegistration(buyer_Name,buyer_Email,buyer_mobile,buyer_Address,buyer_Photo,buyer_Status,buyer_Password);
                                    Log.d("VerifyCode","User registration");
                                }else if (TextUtils.equals(buyer_Status,"2")){
                                    SallerRegistraion(buyer_Name,buyer_Email,buyer_mobile,buyer_Address,buyer_Photo,buyer_Status,buyer_Password,saller_comapany_name,saller_comp_address,saller_comp_desc,saller_comp_ntn);
                                    Log.d("VerifyCode","Saller registration");
                                }*/
                            }else {
                                Toast.makeText(getContext(), "Some Info Missing", Toast.LENGTH_SHORT).show();
                            }

                        } else {
                            // Log.w(TAG, "signInWithCredential:failure", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                                Toast.makeText(getContext(), "Invalid Verification", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
    }

    private void register(final String username, String email, String password){

        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            FirebaseUser firebaseUser = auth.getCurrentUser();
                            assert firebaseUser != null;
                            firebaseUserId = firebaseUser.getUid();
                            Log.d("RegistrationActivity","User ID"+firebaseUserId);
                            reference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUserId);

                            HashMap<String, String> hashMap = new HashMap<>();
                            hashMap.put("id", firebaseUserId);
                            hashMap.put("username", username);
                            hashMap.put("imageURL", "default");
                            hashMap.put("status", "offline");

                            hashMap.put("search", username.toLowerCase());

                            reference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){
                                            //register("najamdd","najamiqbal9@gmail.com","ok123456");
                                            UserRegistration(firebaseUserId,buyer_Name,buyer_Email,buyer_mobile,buyer_Address,buyer_Photo,buyer_Status,buyer_Password);
                                            Log.d("VerifyCode","User registration");

                                        Toast.makeText(getContext(), "Add Successfully", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        } else {
                            Toast.makeText(getContext(), "You can't register with this email or password", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void UserRegistration(final String firebaseid, final String buyer_name, final String buyer_email, final String buyer_mobile, final String buyer_address, final String buyer_photo, final String buyer_status, final String buyer_password) {
        pDialog.setMessage("Registring User....");
        pDialog.show();

        StringRequest stringRequest2 = new StringRequest(Request.Method.POST, registration_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                pDialog.hide();
                Log.d("VerifyActivity","buyer method call"+response.toString());
                try {
                    JSONArray jsonArray=new JSONArray(response);
                    for (int i = 0; i < jsonArray.length(); i++) {

                        JSONObject jsonObject = (JSONObject) jsonArray.get(i);

                        if (jsonObject.getString("status").equals("true")) {
                            pDialog.dismiss();
                            Toast.makeText(getContext(), "Registered Successfully", Toast.LENGTH_SHORT).show();
                            // Goto Login Page
                            LoginFragment loginFragment = new LoginFragment();
                            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                            getActivity().getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                            fragmentTransaction.replace(R.id.fragment_container, loginFragment);
                        } else {
                            pDialog.dismiss();
                            Toast.makeText(getContext(), " Sorry try Again", Toast.LENGTH_SHORT).show();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    pDialog.dismiss();
                    Toast.makeText(getContext(), "catch "+e.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pDialog.dismiss();
                Log.d("Response error","Volley response errror is"+error.getMessage());
                Toast.makeText(getActivity(), "Please ty again", Toast.LENGTH_SHORT).show();

            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("username", buyer_name);
                params.put("mobile", buyer_mobile);
                params.put("password", buyer_password);
                params.put("address", buyer_address);
                params.put("image", buyer_photo);
                params.put("user_type", buyer_status);
                params.put("email", buyer_email);
                params.put("firebase_id", firebaseid);
                return params;

            }
        };
        VolleyRequestsent.getInstance().addRequestQueue(stringRequest2);
    }
}
