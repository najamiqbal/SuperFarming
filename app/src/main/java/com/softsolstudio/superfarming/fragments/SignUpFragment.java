package com.softsolstudio.superfarming.fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.softsolstudio.superfarming.R;
import com.softsolstudio.superfarming.utils.AppUtils;
import com.softsolstudio.superfarming.utils.VolleyRequestsent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignUpFragment extends Fragment implements View.OnClickListener{
    View view;
    private static final int RESULT_LOAD_IMAGE = 2;
    String IsUserExist = "https://houseofsoftwares.com/android-api-2/Api.php?action=isUserExist";
    static final int REQUEST_IMAGE_CAPTURE = 7;
    Boolean imgClicked = false;
    Bitmap bitmap;
    private ProgressDialog pDialog;
    ImageView iv_upload_image_buyer;
    TextView tv_uplaod_image_buyer;
    EditText et_name_buyer, et_mobile_buyer, et_address_buyer, et_email_buyer, et_password_buyer, et_confirm_password_buyer;
    Button registration_btn_buyer;
    RadioGroup radioGroup_item_type;
    String buyer_type = "", buyer_name = "",buyer_phone="", buyer_email = "", buyer_mobile = "", buyer_address = "", buyer_password = "", buyer_confirm_password = "", buyer_image = "";
    //firebase
    private String mVerificationId;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    private FirebaseAuth mAuth;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.signup_fragment,container,false);
        initialization();
        return view;
    }

    private void initialization() {
        radioGroup_item_type=view.findViewById(R.id.radioItemType);
        iv_upload_image_buyer = view.findViewById(R.id.iv_profile_photo_buyer);
        tv_uplaod_image_buyer = view.findViewById(R.id.tv_UploadProfilePhoto_buyer);
        et_name_buyer = view.findViewById(R.id.user_name);
        et_email_buyer = view.findViewById(R.id.user_email);
        et_mobile_buyer = view.findViewById(R.id.user_mobile_number);
        et_address_buyer = view.findViewById(R.id.user_address);
        et_password_buyer = view.findViewById(R.id.user_password);
        et_confirm_password_buyer = view.findViewById(R.id.user_confirm_password);
        registration_btn_buyer = view.findViewById(R.id.register_btn_buyer);
        registration_btn_buyer.setOnClickListener(this);
        tv_uplaod_image_buyer.setOnClickListener(this);
        iv_upload_image_buyer.setOnClickListener(this);
        pDialog = new ProgressDialog(getContext());
        pDialog.setCancelable(false);

        //FireBase Auth Phone Number verification
        mAuth = FirebaseAuth.getInstance();
        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {

                Toast.makeText(getContext(), "user Verified", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                //progressDialog.dismiss();
                Log.e("check1122", e.toString());
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
                VerifyCodeFragment fragment=new VerifyCodeFragment();
                Bundle args = new Bundle();
                args.putString("ID", mVerificationId);
                args.putString("Mobile", buyer_phone);
                args.putString("Name", buyer_name);
                args.putString("Address", buyer_address);
                args.putString("Email", buyer_email);
                args.putString("Status", buyer_type);
                args.putString("Photo", buyer_image);
                args.putString("Password", buyer_password);
                fragment.setArguments(args);
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, fragment);
                fragmentTransaction.commit();

            }

        };
        //End of phone number verification

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.register_btn_buyer:
                if (validate()) {
                    Toast.makeText(getContext(), "Nice Work", Toast.LENGTH_SHORT).show();
                    buyer_phone="+92"+buyer_mobile;
                    IsUserExist(buyer_phone);
                }
                break;
            case R.id.iv_profile_photo_buyer:
                selectImage();
                break;
            case R.id.tv_UploadProfilePhoto_buyer:
                selectImage();
                break;
        }
    }


    //Validating data
    private boolean validate() {
        boolean valid = true;
        buyer_name = et_name_buyer.getText().toString();
        buyer_email = et_email_buyer.getText().toString();
        buyer_mobile = et_mobile_buyer.getText().toString();
        buyer_address = et_address_buyer.getText().toString();
        buyer_password = et_password_buyer.getText().toString();
        buyer_confirm_password = et_confirm_password_buyer.getText().toString();
        if (bitmap!=null){
            buyer_image = getStringImage(bitmap);
        }

        Log.d("IMG", "THIS is image" + buyer_image);

        if (buyer_name.isEmpty()) {
            et_name_buyer.setError("Pleaase enter name");
            valid = false;
        } else {
            et_name_buyer.setError(null);
        }
        if (buyer_email.isEmpty()) {
            et_email_buyer.setError("Please enter email");
            valid = false;
        } else {
            et_email_buyer.setError(null);
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(buyer_email).matches()) {
            et_email_buyer.setError("Email formate is wrong");
            valid = false;
        } else {
            et_email_buyer.setError(null);
        }
        if (buyer_mobile.isEmpty()) {
            et_mobile_buyer.setError("Please enter mobile");
            valid = false;
        } else {
            et_mobile_buyer.setError(null);
        }
        if (buyer_address.isEmpty()) {
            et_address_buyer.setError("Please enter address");
            valid = false;
        } else {
            et_address_buyer.setError(null);
        }
        if (buyer_password.isEmpty() || buyer_confirm_password.isEmpty() || !buyer_confirm_password.equals(buyer_password)) {
            et_password_buyer.setError("Password don't Match");
            et_confirm_password_buyer.setError("Password don't Match");
            valid = false;
        } else {
            et_password_buyer.setError(null);
            et_confirm_password_buyer.setError(null);
        }
        if (radioGroup_item_type.getCheckedRadioButtonId() != -1) {
            int selectedId = radioGroup_item_type.getCheckedRadioButtonId();
            // find the radiobutton by returned id
            RadioButton radioSexButton = (RadioButton) view.findViewById(selectedId);
            buyer_type = radioSexButton.getText().toString();
            Log.d("Sign Up","User type"+buyer_type);
        }
        if (buyer_image.isEmpty()) {
            Toast.makeText(getContext(), "Please select image", Toast.LENGTH_SHORT).show();
            valid = false;
        }
        if (buyer_type.isEmpty()) {
            Toast.makeText(getContext(), "Please select user type", Toast.LENGTH_SHORT).show();
            valid = false;
        }
        if (!isValidPassword(buyer_password)) {
            et_password_buyer.setError("Please Enter Correct Password");
            valid = false;
        } else {
            et_password_buyer.setError(null);
        }
        return valid;
    }
    //*****************************************************************
    public static boolean isValidPassword(final String password) {

        Pattern pattern;
        Matcher matcher;
        final String PASSWORD_PATTERN = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[!@#$%^&*()_+])[A-Za-z\\d][A-Za-z\\d!@#$%^&*()_+]{7,19}$";
        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(password);

        return matcher.matches();

    }
    public String getStringImage(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 10, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    private void IsUserExist(final String mobilenumber) {
        Log.e("check1122", "mobile number" + mobilenumber);
        pDialog.setMessage("Registring ...");
        pDialog.show();
        StringRequest stringRequest=new StringRequest(Request.Method.POST, IsUserExist, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray=new JSONArray(response);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                        if (jsonObject.getString("status").equals("true")) {
                            pDialog.dismiss();
                            Toast.makeText(getContext(), "Number Already Registered", Toast.LENGTH_SHORT).show();

                        } else {
                            pDialog.dismiss();
                            Toast.makeText(getContext(), "HELLO", Toast.LENGTH_SHORT).show();
                            PhoneAuthProvider.getInstance().verifyPhoneNumber(
                                    mobilenumber,
                                    120,
                                    java.util.concurrent.TimeUnit.SECONDS,
                                    getActivity(),
                                    mCallbacks);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    pDialog.dismiss();
                    Toast.makeText(getContext(), ""+e.toString(), Toast.LENGTH_SHORT).show();
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
                params.put("mobile", mobilenumber);
                return params;

            }

        };
        VolleyRequestsent.getInstance().addRequestQueue(stringRequest);
    }


    private void selectImage() {
        imgClicked = true;
        final CharSequence[] items = {"Take Photo", "Choose from Library",
                "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Upload Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                //result=Utility.checkFilePermission(getActivity());
                if (items[item].equals("Take Photo")) {
                    final boolean result = AppUtils.checkPermissionCamera(getActivity(), AppUtils.REQUEST_CAMERA_UPLOAD);
                    if (!result) return;
                    Boolean result1 = AppUtils.checkPermissionWrite(getActivity(), AppUtils.REQUEST_CAMERA_UPLOAD);
                    if (!result1) return;
                    if (result && result1) {
                        openCamera();
                    }
                } else if (items[item].equals("Choose from Library")) {
                    Boolean result1 = AppUtils.checkPermissionWrite(getContext(), AppUtils.REQUEST_GALLERY_UPLOAD);
                    if (!result1) return;
                    final boolean result = AppUtils.checkPermissionCamera(getContext(), AppUtils.REQUEST_GALLERY_UPLOAD);
                    if (!result) return;
                    if (result && result1) {
                        openGalleryImage();
                    }
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }


    public void openGalleryImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), RESULT_LOAD_IMAGE);
    }

    public void openCamera() {
        try {
            Intent takeVideoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (takeVideoIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                startActivityForResult(takeVideoIntent, REQUEST_IMAGE_CAPTURE);
            }
        } catch (ActivityNotFoundException anfe) {
            //display an error message
            String errorMessage = "Your device doesn't support capturing images!";
            Toast toast = Toast.makeText(getContext(), errorMessage, Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
// RC_SIGN_IN is the request code you passed into startActivityForResult(...) when starting the sign in flow.
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_IMAGE_CAPTURE) {

                try {
                    final Bundle extras = data.getExtras();
                    bitmap = (Bitmap) extras.get("data");
                    // bitmap = AppUtils.getResizedBitmap(bitmap, 100);
                    //bitmap = MediaStore.Images.Media.getBitmap(getActivity().getApplicationContext().getContentResolver(), data.getData());
                    iv_upload_image_buyer.setImageBitmap(bitmap);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            if (requestCode == RESULT_LOAD_IMAGE && null != data) {

                try {
                    bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), data.getData());
                    //           bitmap = AppUtils.getResizedBitmap(bitmap, 100);
                    Log.d("PHOTO", "YES" + bitmap);
                    iv_upload_image_buyer.setImageBitmap(bitmap);
                    ;
                    //remove_pic.setVisibility(View.VISIBLE);
                } catch (final IOException e) {

                    e.printStackTrace();
                    Toast.makeText(getActivity(), "Unable to add picture", Toast.LENGTH_SHORT).show();

                }
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case AppUtils.REQUEST_GALLERY_UPLOAD:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    final boolean result = AppUtils.checkPermissionCamera(getContext(), AppUtils.REQUEST_GALLERY_UPLOAD);
                    if (!result) return;
                    else {
                        openGalleryImage();
                    }
                }
                break;
            case AppUtils.REQUEST_CAMERA_UPLOAD:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Boolean result1 = AppUtils.checkPermissionWrite(getContext(), AppUtils.REQUEST_CAMERA_UPLOAD);
                    if (!result1) return;
                    else {
                        openCamera();
                    }

                }
                break;
        }
    }
}
