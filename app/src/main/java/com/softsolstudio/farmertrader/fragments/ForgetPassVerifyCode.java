package com.softsolstudio.farmertrader.fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.softsolstudio.farmertrader.R;

public class ForgetPassVerifyCode extends Fragment {
    View view;
    Button verify;
    TextView timer;
    EditText Code;
    Handler handler;
    int count = 120;
    String verificationId, mobile="";
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    private FirebaseAuth mAuth;
    private ProgressDialog pDialog;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.forget_pass_verfiycode_fargment,container,false);
        initialization();
        return view;
    }

    private void initialization() {
        pDialog = new ProgressDialog(getContext());
        pDialog.setCancelable(false);
        verify = view.findViewById(R.id.forgetpass_btn_verify);
        timer = view.findViewById(R.id.forgetpass_resettimer);
        handler = new Handler(getActivity().getMainLooper());
        Code = view.findViewById(R.id.forgetpass_firebaseCode);
        if (getArguments()!=null){
            verificationId = getArguments().getString("ID");
            mobile = getArguments().getString("Mobile");
        }
        Log.d("Mobile_registration", "data mobile number" + mobile);
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
                            if (!mobile.isEmpty()) {
                                ResetPassFragment fragment=new ResetPassFragment();
                                Bundle args = new Bundle();
                                args.putString("mobile", mobile);
                                fragment.setArguments(args);
                                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                                fragmentTransaction.replace(R.id.fragment_container, fragment);
                                fragmentTransaction.commit();

                            }else {
                                Toast.makeText(getContext(), "Empty string", Toast.LENGTH_SHORT).show();
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

}
