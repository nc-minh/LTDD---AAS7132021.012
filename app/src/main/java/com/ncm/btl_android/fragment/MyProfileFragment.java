package com.ncm.btl_android.fragment;

import static com.ncm.btl_android.MainActivity.MY_REQUEST_CODE;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.ncm.btl_android.MainActivity;
import com.ncm.btl_android.R;

public class MyProfileFragment extends Fragment {

    private View mView;
    private ImageView imgAvatar;
    private EditText edtFullName, edtEmail;
    private Button btn_update_profile, btn_update_email;

    private Uri mUri;
    private MainActivity mainActivity;
    private ProgressDialog progressDialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_my_profile,container, false);

        initUI();
        mainActivity = (MainActivity) getActivity();
        progressDialog = new ProgressDialog(getActivity());
        setUserInfomation();
        initListener();
        

        return mView;
    }

    private void initUI(){
        imgAvatar = mView.findViewById(R.id.img_avatar);
        edtFullName = mView.findViewById(R.id.edt_full_name);
        edtEmail = mView.findViewById(R.id.edt_email);
        btn_update_profile = mView.findViewById(R.id.btn_update_profile);
        btn_update_email = mView.findViewById(R.id.btn_update_email);
    }

    private void setUserInfomation() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user == null){
            return;
        }

        edtFullName.setText(user.getDisplayName());
        edtEmail.setText(user.getEmail());
        Uri photoUrl = user.getPhotoUrl();
        //Glide.with(getActivity()).load(photoUrl).error(R.drawable.ic_user).into(imgAvatar);
        Glide.with(this).load(photoUrl).error(R.drawable.user).into(imgAvatar);
    }

    private void initListener() {
        imgAvatar.setOnClickListener(v -> {
            onClickRequestPermission();
        });

        btn_update_profile.setOnClickListener(v -> {
            onClickUpdateProfile();
        });

        btn_update_email.setOnClickListener(v -> {
            onClickUpdateEmail();
        });
    }

    private void onClickRequestPermission() {

        if(mainActivity == null){
            return;
        }

        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.M){
            mainActivity.openGallery();
            return;
        }

        if(getActivity().checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
            mainActivity.openGallery();
        }else{
            String [] permission = {Manifest.permission.READ_EXTERNAL_STORAGE};
            getActivity().requestPermissions(permission, MY_REQUEST_CODE);
        }
    }

    public void setBitMapImageView(Bitmap bitMapImageView){
        imgAvatar.setImageBitmap(bitMapImageView);
    }

    public void setUri(Uri mUri) {
        this.mUri = mUri;
    }

    private void onClickUpdateProfile(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user == null){
            return;
        }

        progressDialog.show();
        String strFullName = edtFullName.getText().toString().trim();


        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(strFullName)
                .setPhotoUri(mUri)
                .build();

        user.updateProfile(profileUpdates)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        progressDialog.dismiss();
                        if (task.isSuccessful()) {
                            Toast.makeText(getActivity(), "User profile updated", Toast.LENGTH_SHORT).show();
                            mainActivity.showUserInformation();
                        }
                    }
                });
    }

    private void onClickUpdateEmail(){
        String strNewEmail = edtEmail.getText().toString().trim();
        progressDialog.show();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        user.updateEmail(strNewEmail)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        progressDialog.dismiss();
                        if (task.isSuccessful()) {
                            Toast.makeText(getActivity(), "User email updated", Toast.LENGTH_SHORT).show();
                            mainActivity.showUserInformation();
                        }else{
                            Toast.makeText(getActivity(), "User email failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

}
