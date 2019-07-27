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
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.softsolstudio.superfarming.R;
import com.softsolstudio.superfarming.utils.VolleyRequestsent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ForgetPassFragment extends Fragment {
    View view;
    String phone="";
    EditText phone_no;
    Button submit;
    TextView backtologin;
    private ProgressDialog pDialog;
    String Isexist_url = "http://jugnoosinternational.com/android-api/Api.php?action=isUserExist";
    private String mVerificationId;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    FirebaseAuth mAuth;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.forgetpassword_fragment,container,false);
        initialization();
        return view;
    }

    private void initialization() {
        pDialog = new ProgressDialog(getContext());
        pDialog.setCancelable(false);
        backtologin=view.findViewById(R.id.backtologin);
        submit=view.findViewById(R.id.submit_btn);
        phone_no=view.findViewById(R.id.et_phone_no);
        backtologin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                LoginFragment fragment=new LoginFragment();
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, fragment);
                fragmentTransaction.commit();
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (phone_no.getText().toString().length()==10 && !phone_no.getText().toString().isEmpty()){
                    phone="+92"+phone_no.getText().toString().trim();
                    if (!phone.isEmpty()){
                        IsUserExist(phone);
                    }else {
                        Toast.makeText(getContext(), "Please enter mobile number", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(getContext(), "Please enter valid number", Toast.LENGTH_SHORT).show();
                }
            }
        });


        //FireBase Auth Phone Number verification
        FirebaseApp.initializeApp(getActivity());
        mAuth = FirebaseAuth.getInstance();
        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
              /*  progressDialog.dismiss();
                Toast.makeText(UserRegister.this, "VerificationComplete" + "\n" + phone_number.getText() + name.getText() + pin.getText(), Toast.LENGTH_SHORT).show();
                // Log.d(TAG, "onVerificationCompleted:" + credential);
                mVerificationInProgress = false;*/
                //isVerified = true;
                Toast.makeText(getContext(), "user Verified", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                //progressDialog.dismiss();
// Log.w(TAG, "onVerificationFailed", e);
                Toast.makeText(getContext(), "Verification Failed", Toast.LENGTH_SHORT).show();
                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    // Invalid request
                    Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    // ...
                } else if (e instanceof FirebaseTooManyRequestsException) {
                }

            }

            @Override
            public void onCodeSent(String verificationId,
                                   PhoneAuthProvider.ForceResendingToken token) {
                // progressDialog.dismiss();
                // Log.d(TAG, "onCodeSent:" + verificationId);
                Toast.makeText(getContext(), "Verification code has been send on your number", Toast.LENGTH_SHORT).show();
                // Save verification ID and resending token so we can use them later
                mVerificationId = verificationId;
                mResendToken = token;
                //intent.putExtra("ID", mVerificationId);
                // intent.putExtra("Mobile", "+92" + phone_no.getText().toString());
                ForgetPassVerifyCode fragment=new ForgetPassVerifyCode();
                Bundle args = new Bundle();
                args.putString("ID", mVerificationId);
                args.putString("Mobile", phone);
                fragment.setArguments(args);
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, fragment);
                fragmentTransaction.commit();

            }

        };
        //End of phone number verification



    }

    private void IsUserExist(final String phone) {
        Log.e("check1122", "mobile number" + phone);
        pDialog.setMessage("Registring ...");
        pDialog.show();
        StringRequest stringRequest=new StringRequest(Request.Method.POST,Isexist_url , new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray=new JSONArray(response);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                        if (jsonObject.getString("status").equals("true")) {
                            pDialog.dismiss();
                            //Toast.makeText(getContext(), "HELLO", Toast.LENGTH_SHORT).show();
                            PhoneAuthProvider.getInstance().verifyPhoneNumber(
                                    phone,
                                    120,
                                    java.util.concurrent.TimeUnit.SECONDS,
                                    getActivity(),
                                    mCallbacks);

                        } else {
                            pDialog.dismiss();
                            Toast.makeText(getContext(), "Number Not Registered", Toast.LENGTH_SHORT).show();
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
                pDialog.dismiss();
                Toast.makeText(getContext(), "Error Please tyr again"+error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }){

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("mobile", phone);
                return params;
            }

        };
        VolleyRequestsent.getInstance().addRequestQueue(stringRequest);
    }
}
