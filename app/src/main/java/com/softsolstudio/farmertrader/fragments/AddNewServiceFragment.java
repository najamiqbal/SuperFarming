package com.softsolstudio.farmertrader.fragments;

import android.app.Activity;
import android.app.FragmentManager;
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
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.softsolstudio.farmertrader.R;
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

public class AddNewServiceFragment extends Fragment implements View.OnClickListener{
    View view;
    String service_category="",service_sub_category="",service_desc="",service_price="",service_photo="",user_id="";
    EditText et_service_title,et_service_description,et_service_price;
    ImageView imageView;
    TextView uploadImage;
    Button add_btn;
    private static final int RESULT_LOAD_IMAGE = 2;
    String addService = "https://houseofsoftwares.com/android-api-2/Api.php?action=addService";
    static final int REQUEST_IMAGE_CAPTURE = 7;
    Boolean imgClicked = false;
    Bitmap bitmap;
    private ProgressDialog pDialog;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.add_new_service_fragment,container,false);
        if (getArguments()!=null){
            service_category=getArguments().getString("Type");
            service_sub_category=getArguments().getString("SubType");
            Log.d("NEW ADD","LOVE LATTER"+service_category);
        }else {
            Toast.makeText(getActivity(), "Basic info not save", Toast.LENGTH_SHORT).show();
        }
        initialization();
        return  view;
    }

    private void initialization() {
        pDialog = new ProgressDialog(getContext());
        pDialog.setCancelable(false);
        et_service_title=view.findViewById(R.id.service_title);
        et_service_description=view.findViewById(R.id.service_desc);
        et_service_price=view.findViewById(R.id.service_price);
        imageView=view.findViewById(R.id.iv_service_photo);
        uploadImage=view.findViewById(R.id.tv_Upload_image);
        add_btn=view.findViewById(R.id.add_service_btn);
        et_service_title.setText(service_sub_category);
        imageView.setOnClickListener(this);
        uploadImage.setOnClickListener(this);
        add_btn.setOnClickListener(this);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        getActivity().setTitle(service_category);
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.iv_service_photo:
                selectImage();
                break;
            case R.id.tv_Upload_image:
                selectImage();
                break;
            case R.id.add_service_btn:
                if (validate()){
                    AddService(user_id,service_category,service_sub_category,service_desc,service_price,service_photo);
                }
                break;
        }
    }

    private void AddService(final String user_id, final String service_category, final String service_sub_category, final String service_desc, final String service_price, final String service_photo) {
        pDialog.setMessage("Adding Service....");
        pDialog.show();

        StringRequest stringRequest2 = new StringRequest(Request.Method.POST, addService, new Response.Listener<String>() {
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
                            Toast.makeText(getContext(), "Add Successfully", Toast.LENGTH_SHORT).show();
                            // Goto Login Page
                            getActivity().onBackPressed();
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
                params.put("user_id", user_id);
                params.put("service_category", service_category);
                params.put("sub_category", service_sub_category);
                params.put("description", service_desc);
                params.put("price", service_price);
                params.put("image", service_photo);
                return params;

            }
        };
        VolleyRequestsent.getInstance().addRequestQueue(stringRequest2);
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

    //Validating data
    private boolean validate() {
        boolean valid = true;
        service_sub_category = et_service_title.getText().toString();
        service_desc = et_service_description.getText().toString();
        service_price = et_service_price.getText().toString();
        UserModelClass userModelClass= SharedPrefManager.getInstance(getContext()).getUser();
        if (userModelClass!=null){
            user_id=userModelClass.getUser_id();
        }
        if (user_id.isEmpty()){
            Toast.makeText(getContext(), "User id is Missing", Toast.LENGTH_SHORT).show();
            valid=false;
        }
        if (bitmap!=null){
            service_photo = getStringImage(bitmap);
        }

        Log.d("IMG", "THIS is image" + service_photo);

        if (service_sub_category.isEmpty()) {
            et_service_title.setError("Pleaase enter title");
            valid = false;
        } else {
            et_service_title.setError(null);
        }
        if (service_desc.isEmpty()) {
            et_service_description.setError("Please enter desc");
            valid = false;
        } else {
            et_service_description.setError(null);
        }
        if (service_price.isEmpty()) {
            et_service_price.setError("Please enter price");
            valid = false;
        } else {
            et_service_price.setError(null);
        }
        if (service_photo.isEmpty()) {
            Toast.makeText(getContext(), "Please select image", Toast.LENGTH_SHORT).show();
            valid = false;
        }
        return valid;
    }

    public String getStringImage(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 10, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
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
                    imageView.setImageBitmap(bitmap);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            if (requestCode == RESULT_LOAD_IMAGE && null != data) {

                try {
                    bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), data.getData());
                    //           bitmap = AppUtils.getResizedBitmap(bitmap, 100);
                    Log.d("PHOTO", "YES" + bitmap);
                    imageView.setImageBitmap(bitmap);
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
