package com.softsolstudio.farmertrader.fragments;

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
import android.support.v7.app.AlertDialog;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.softsolstudio.farmertrader.R;
import com.softsolstudio.farmertrader.activities.LoginSignUpActivity;
import com.softsolstudio.farmertrader.models.UserModelClass;
import com.softsolstudio.farmertrader.utils.AppUtils;
import com.softsolstudio.farmertrader.utils.SharedPrefManager;
import com.softsolstudio.farmertrader.utils.VolleyRequestsent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EditProfileFragment extends Fragment implements View.OnClickListener{
    View view;
    private static final int RESULT_LOAD_IMAGE = 2;
    String updateProfile = "https://houseofsoftwares.com/android-api-2/Api.php?action=UpdateProfile";
    static final int REQUEST_IMAGE_CAPTURE = 7;
    Boolean imgClicked = false;
    Bitmap bitmap;
    private ProgressDialog pDialog;
    EditText edit_name,edit_mobile,edit_addres,edit_email,edit_current_pass,new_pass;
    ImageView edit_photo;
    TextView edit_imageupload;
    Button edit_info_btn;
    String edit_user_name="",UserId="",edit_user_email="",edit_user_address="",edit_user_mobile="",edit_user_photo="",edit_user_currentPass="",edit_user_newPass="";
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.edit_profile_fragment,container,false);
        initialization();
        return view;
    }

    private void initialization() {
        pDialog = new ProgressDialog(getContext());
        pDialog.setCancelable(false);
        edit_addres=view.findViewById(R.id.user_address_edit);
        edit_name=view.findViewById(R.id.user_name_edit);
        edit_mobile=view.findViewById(R.id.user_mobile_number_edit);
        edit_email=view.findViewById(R.id.user_email_edit);
        edit_current_pass=view.findViewById(R.id.user_password_edit);
        new_pass=view.findViewById(R.id.user_confirm_password_edit);
        edit_photo=view.findViewById(R.id.iv_profile_photo_edit);
        edit_imageupload=view.findViewById(R.id.tv_UploadProfilePhoto_edit);
        edit_info_btn=view.findViewById(R.id.edit_btn_user);
        edit_info_btn.setOnClickListener(this);
        edit_imageupload.setOnClickListener(this);
        edit_photo.setOnClickListener(this);
        BindData();
    }
    private void BindData() {
        UserModelClass userModelClass= SharedPrefManager.getInstance(getContext()).getUser();
        if (userModelClass!=null){
            String number=userModelClass.getUser_mobile();
            number=number.substring(3);
            edit_name.setText(userModelClass.getUser_name());
            edit_email.setText(userModelClass.getUser_email());
            edit_mobile.setText(number);
            edit_addres.setText(userModelClass.getUser_address());
            Glide.with(getContext()).load(userModelClass.getUser_photo()).dontAnimate().fitCenter().placeholder(R.drawable.ic_profile_pic).into(edit_photo);
        }
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        getActivity().setTitle("Edit Profile");
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.edit_btn_user:
                if (validate()){
                    UserUpdate(UserId,edit_user_name,edit_user_email,edit_user_mobile,edit_user_address,edit_user_newPass,edit_user_photo);
                }
                break;
            case R.id.tv_UploadProfilePhoto_edit:
                selectImage();
                break;
            case R.id.iv_profile_photo_edit:
                selectImage();
                break;
        }
    }


    private void UserUpdate(final String userId, final String edit_user_name, final String edit_user_email, final String edit_user_mobile, final String edit_user_address, final String edit_user_newPass, final String edit_user_photo) {
        pDialog.setMessage("Updating....");
        pDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, updateProfile, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                pDialog.hide();
                Log.d("EditCompany Fragment","editCompany method call"+response.toString());
                try {
                    JSONArray jsonArray=new JSONArray(response);
                    for (int i = 0; i < jsonArray.length(); i++) {

                        JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                        if (jsonObject.getString("status").equals("true")) {
                            pDialog.dismiss();
                            Toast.makeText(getContext(), "Info Updated Successfully", Toast.LENGTH_SHORT).show();
                            SharedPrefManager.getInstance(getContext()).logOut();
                            // Goto Login Page
                            Intent intent=new Intent(getContext(), LoginSignUpActivity.class);
                            startActivity(intent);
                            getActivity().finish();
                        } else {
                            pDialog.dismiss();
                            Toast.makeText(getContext(), " Sorry try Again", Toast.LENGTH_SHORT).show();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    pDialog.dismiss();
                    Toast.makeText(getContext(), "edit error catch"+e.toString(), Toast.LENGTH_SHORT).show();
                }
                /*if (TextUtils.equals(response.toString(),"user registered")){
                    pDialog.dismiss();
                    Toast.makeText(getContext(), "Registered Successfully", Toast.LENGTH_SHORT).show();
                    // Goto Login Page
                    LoginFragment loginFragment = new LoginFragment();
                    FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                    getActivity().getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                    fragmentTransaction.replace(R.id.fragment_container, loginFragment);
                }else {
                    pDialog.dismiss();
                    Toast.makeText(getContext(), " Sorry try Again", Toast.LENGTH_SHORT).show();
                }*/
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pDialog.dismiss();
                Log.d("Edit company","Volley response errror is"+error.getMessage());
                Toast.makeText(getActivity(), "Error Please tyr again "+error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("username", edit_user_name);
                params.put("mobile", edit_user_mobile);
                params.put("email", edit_user_email);
                params.put("address", edit_user_address);
                params.put("user_id", userId);
                params.put("image", edit_user_photo);
                params.put("password", edit_user_newPass);
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
                    edit_photo.setImageBitmap(bitmap);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            if (requestCode == RESULT_LOAD_IMAGE && null != data) {

                try {
                    bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), data.getData());
                    //           bitmap = AppUtils.getResizedBitmap(bitmap, 100);
                    Log.d("PHOTO", "YES" + bitmap);
                    edit_photo.setImageBitmap(bitmap);
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

    //Validating data
    private boolean validate() {
        boolean valid = true;
        edit_user_name = edit_name.getText().toString();
        edit_user_email = edit_email.getText().toString();
        edit_user_mobile = "+92"+edit_mobile.getText().toString();
        edit_user_address = edit_addres.getText().toString();
        edit_user_currentPass = edit_current_pass.getText().toString();
        edit_user_newPass = new_pass.getText().toString();
        UserModelClass userModelClass = SharedPrefManager.getInstance(getContext()).getUser();
        UserId=userModelClass.getUser_id();
        if (bitmap!=null){
            edit_user_photo = getStringImage(bitmap);
        }else {
            edit_user_photo=userModelClass.getUser_photo();
        }

        Log.d("IMG", "THIS is image" + edit_user_photo);

        if (edit_user_name.isEmpty()){
            edit_name.setError("Plaese enter name");
            valid=false;
        }else {
            edit_name.setError(null);
        }
        if (UserId.isEmpty()){
            Toast.makeText(getContext(), "Error reload the activity", Toast.LENGTH_SHORT).show();
            valid=false;
        }
        if (edit_user_email.isEmpty()) {
            edit_email.setError("Pleaase enter email");
            valid = false;
        } else {
            edit_email.setError(null);
        }
        if (edit_user_mobile.isEmpty()) {
            edit_mobile.setError("Please enter mobile");
            valid = false;
        } else {
            edit_mobile.setError(null);
        }
        if (edit_user_address.isEmpty()) {
            edit_addres.setError("Please enter address");
            valid = false;
        } else {
            edit_addres.setError(null);
        }
        if (edit_user_photo.isEmpty()) {
            Toast.makeText(getContext(), "Please select a photo", Toast.LENGTH_SHORT).show();
            valid = false;
        }
        if (edit_user_currentPass.isEmpty()  || !edit_user_currentPass.equals(userModelClass.getUser_password())) {
            edit_current_pass.setError("Password don't Match");
            valid = false;
        } else {
            edit_current_pass.setError(null);
        }
        if (edit_user_newPass.isEmpty()){
            new_pass.setError("please enter password");
            valid=false;
        }else {
            new_pass.setError(null);
        }
        if (!isValidPassword(edit_user_newPass)) {
            new_pass.setError("Please Enter Correct Password");
            valid = false;
        } else {
            new_pass.setError(null);
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
}
