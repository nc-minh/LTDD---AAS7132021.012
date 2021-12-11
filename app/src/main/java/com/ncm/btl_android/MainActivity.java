package com.ncm.btl_android;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.ncm.btl_android.fragment.ChangePassWordFragment;
import com.ncm.btl_android.fragment.FavoriteFragment;
import com.ncm.btl_android.fragment.HistoryFragment;
import com.ncm.btl_android.fragment.HomeFragment;
import com.ncm.btl_android.fragment.MyProfileFragment;

import java.io.IOException;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    public static final int MY_REQUEST_CODE = 10;

    private static final int FRAGMENT_HOME = 0;
    private static final int FRAGMENT_FAVORITE = 1;
    private static final int FRAGMENT_HISTORY = 2;
    private static final int FRAGMENT_MY_PROFILE = 3;
    private static final int FRAGMENT_CHANGE_PASSWORD = 4;

    private int mCurrentFragment = FRAGMENT_HOME;

    private DrawerLayout mDrawerLayout;

    private ImageView imgAvatar;
    private TextView tvName, tvEmail;

    private NavigationView navigationView;

    final private MyProfileFragment myProfileFragment = new MyProfileFragment();


    final private ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
        @Override
                public void onActivityResult(ActivityResult result) {
                    if(result.getResultCode() == RESULT_OK){
                        Intent intent = result.getData();
                        if(intent == null){
                            return;
                        }

                        Uri uri = intent.getData();
                        myProfileFragment.setUri(uri);
                        try {
                            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                            myProfileFragment.setBitMapImageView(bitmap);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.tool_bar);

        initUI();
        //(new FavoriteFragment());

        mDrawerLayout = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                toolbar, R.string.nav_drawer_open, R.string.nav_drawer_close);

        mDrawerLayout.addDrawerListener(toggle);

        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);

        replaceFragment(new HomeFragment());
        navigationView.getMenu().findItem(R.id.navigation_home).setChecked(true);

        showDataInformation();
    }

    private void initUI(){
        navigationView = findViewById(R.id.navigation_view);

        imgAvatar = navigationView.getHeaderView(0).findViewById(R.id.img_avatar);
        tvName = navigationView.getHeaderView(0).findViewById(R.id.tv_name);
        tvEmail = navigationView.getHeaderView(0).findViewById(R.id.tv_email);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.navigation_home){
            if(mCurrentFragment != FRAGMENT_HOME){
                replaceFragment(new HomeFragment());
                mCurrentFragment = FRAGMENT_HOME;
            }
        }else if(id == R.id.navigation_favorite){
            if(mCurrentFragment != FRAGMENT_FAVORITE){
                replaceFragment(new FavoriteFragment());
                mCurrentFragment = FRAGMENT_FAVORITE;
            }

        }else if(id == R.id.navigation_history){
            if(mCurrentFragment != FRAGMENT_HISTORY){
                replaceFragment(new HistoryFragment());
                mCurrentFragment = FRAGMENT_HISTORY;
            }
        }else if(id == R.id.navigation_sign_out){
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(this, SignInActivity.class);
            startActivity(intent);
            finish();
        }else if(id == R.id.navigation_my_profile){
            if(mCurrentFragment != FRAGMENT_MY_PROFILE){
                replaceFragment(myProfileFragment);
                mCurrentFragment = FRAGMENT_MY_PROFILE;
            }
        }else if(id == R.id.navigation_change_password){
            if(mCurrentFragment != FRAGMENT_CHANGE_PASSWORD){
                replaceFragment(new ChangePassWordFragment());
                mCurrentFragment = FRAGMENT_CHANGE_PASSWORD;
            }
        }

        mDrawerLayout.closeDrawer(GravityCompat.START);

        return true;
    }

    @Override
    public void onBackPressed() {
        if(mDrawerLayout.isDrawerOpen(GravityCompat.START)){
            mDrawerLayout.closeDrawer(GravityCompat.START);
        }else{
            super.onBackPressed();
        }
    }

    private void replaceFragment(Fragment fragment){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.content_frame, fragment);
        transaction.commit();
    }

    public void showDataInformation(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user == null){
            return;
        }
        String name = user.getDisplayName();
        String email = user.getEmail();
        Uri photoUrl = user.getPhotoUrl();

        if(name == null){
            tvName.setVisibility(View.GONE);
        }else{
            tvName.setVisibility(View.VISIBLE);
        }

        tvName.setText(name);
        tvEmail.setText(email);
        Glide.with(this).load(photoUrl).error(R.drawable.user).into(imgAvatar);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == MY_REQUEST_CODE){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                openGallery();
            }else{
                Toast.makeText(this, "Vui lòng cho phép truy cập vào mục lưu trữ ảnh!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    //open gallery in your device
    public void openGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);

        activityResultLauncher.launch(Intent.createChooser(intent, "Select Picture!"));
    }
}