package com.softsolstudio.superfarming.fragments;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
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
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.softsolstudio.superfarming.R;
import com.softsolstudio.superfarming.models.UserModelClass;
import com.softsolstudio.superfarming.utils.AppUtils;
import com.softsolstudio.superfarming.utils.SharedPrefManager;
import com.softsolstudio.superfarming.utils.VolleyRequestsent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class EditUserServiceFragment extends Fragment implements View.OnClickListener {
    View view;
    private static final int RESULT_LOAD_IMAGE = 2;
    String updateService = "https://houseofsoftwares.com/android-api-2/Api.php?action=editService";
    static final int REQUEST_IMAGE_CAPTURE = 7;
    Boolean imgClicked = false;
    Bitmap bitmap;
    private ProgressDialog pDialog;
    String userId = "", serviceId = "", servcieCategory = "", serviceSubCategory = "", serviceDesc = "", servicePrice = "", servicePhoto = "";
    ImageView service_image;
    Button update_service_btn;
    TextView upload_new_image;
    EditText service_sub_title, service_desc, service_price;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.edit_user_service_fragment, container, false);
        if (getArguments() != null) {
            serviceId = getArguments().getString("service_id");
            servcieCategory = getArguments().getString("service_category");
            serviceSubCategory = getArguments().getString("service_sub");
            serviceDesc = getArguments().getString("service_desc");
            servicePrice = getArguments().getString("service_price");
            servicePhoto = getArguments().getString("service_image");
            Log.d("FarmerServiceView", "image" + servicePhoto + " " + serviceId + " " + serviceSubCategory + " " + serviceDesc + " " + serviceSubCategory);
        }
        initialization();
        return view;
    }

    private void initialization() {
        pDialog = new ProgressDialog(getContext());
        pDialog.setCancelable(false);
        service_image = view.findViewById(R.id.edit_iv_service_photo);
        upload_new_image = view.findViewById(R.id.edit_tv_Upload_image);
        update_service_btn = view.findViewById(R.id.edit_service_btn);
        service_sub_title = view.findViewById(R.id.edit_service_title);
        service_desc = view.findViewById(R.id.edit_service_desc);
        service_price = view.findViewById(R.id.edit_service_price);
        service_sub_title.setText(serviceSubCategory);
        service_price.setText(servicePrice);
        service_desc.setText(serviceDesc);
        if (bitmap==null){
            //Glide.with(getContext()).load(servicePhoto).dontAnimate().fitCenter().into(service_image);
        }
        upload_new_image.setOnClickListener(this);
        update_service_btn.setOnClickListener(this);
        service_image.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.edit_service_btn:
                if (validate()) {
                    EditService(userId,serviceId,servcieCategory,serviceSubCategory,serviceDesc,servicePrice,servicePhoto);
                }
                break;
            case R.id.edit_iv_service_photo:
                showImage(servicePhoto);
                break;
            case R.id.edit_tv_Upload_image:
                selectImage();
                break;
        }
    }

    private void EditService(final String userId, final String serviceId, final String servcieCategory, final String serviceSubCategory, final String serviceDesc, final String servicePrice, final String servicePhoto) {
        pDialog.setMessage("please Wait....");
        pDialog.show();
        Log.d("Response is", "CHECK RESPONSE before" + servicePhoto);
        StringRequest stringRequest = new StringRequest(Request.Method.POST,updateService, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Response is", "CHECK RESPONSE" + response.toString());
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                        Log.d("status", "CHECK" + jsonObject.getString("status"));
                        if (jsonObject.getString("status").equals("true")) {
                            Toast.makeText(getContext(), "updated", Toast.LENGTH_SHORT).show();
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
                params.put("user_id", userId);
                params.put("service_category", servcieCategory);
                params.put("sub_category", serviceSubCategory);
                params.put("price", servicePrice);
                params.put("image", servicePhoto);
                params.put("description", serviceDesc);
                return params;
            }
        };
        VolleyRequestsent.getInstance().addRequestQueue(stringRequest);
    }

    public void showImage(String imageUri) {
        Dialog builder = new Dialog(getContext());
        builder.requestWindowFeature(Window.FEATURE_NO_TITLE);
        builder.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));
        builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                //nothing;
            }
        });

        ImageView imageView = new ImageView(getContext());
        //imageView.setImageURI(imageUri);
        if (imageUri != null) {
            Glide.with(getContext()).load(imageUri).dontAnimate().fitCenter().placeholder(R.drawable.applogo).into(imageView);
        }
        //Picasso.get().load(imageUri).placeholder(R.drawable.logo).into(imageView);
        builder.addContentView(imageView, new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        builder.show();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        getActivity().setTitle(servcieCategory);
        super.onViewCreated(view, savedInstanceState);
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
        serviceSubCategory = service_sub_title.getText().toString();
        serviceDesc = service_desc.getText().toString();
        servicePrice = service_price.getText().toString();
        UserModelClass userModelClass = SharedPrefManager.getInstance(getContext()).getUser();
        userId = userModelClass.getUser_id();
        if (bitmap != null) {
            Log.d("IMG", "not null is" + bitmap);
            servicePhoto="";
            servicePhoto = getStringImage(bitmap);
            Log.d("IMG", "THIS is image if" + servicePhoto);
        } else {
           // servicePhoto = userModelClass.getUser_photo();
            Log.d("IMG", "THIS is image else" + servicePhoto);
        }

        Log.d("IMG", "THIS is image" + servicePhoto);

        if (serviceSubCategory.isEmpty()) {
            service_sub_title.setError("Plaese enter title");
            valid = false;
        } else {
            service_sub_title.setError(null);
        }
        if (userId.isEmpty()) {
            Toast.makeText(getContext(), "Error reload the activity", Toast.LENGTH_SHORT).show();
            valid = false;
        }
        if (serviceDesc.isEmpty()) {
            service_desc.setError("Pleaase enter description");
            valid = false;
        } else {
            service_desc.setError(null);
        }
        if (servicePrice.isEmpty()) {
            service_price.setError("Please enter price");
            valid = false;
        } else {
            service_price.setError(null);
        }
        return valid;
    }

    public String getStringImage(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 10, baos);
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
                    service_image.setImageBitmap(bitmap);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            if (requestCode == RESULT_LOAD_IMAGE && null != data) {

                try {
                    bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), data.getData());
                    //           bitmap = AppUtils.getResizedBitmap(bitmap, 100);
                    Log.d("PHOTO", "YES" + bitmap);
                    service_image.setImageBitmap(bitmap);

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
