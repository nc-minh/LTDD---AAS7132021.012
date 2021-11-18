package com.ncm.btl_android.actions;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.ncm.btl_android.R;
import com.ncm.btl_android.SignInActivity;
import com.ncm.btl_android.fragment.ChangePassWordFragment;
import com.ncm.btl_android.fragment.FavoriteFragment;
import com.ncm.btl_android.fragment.HistoryFragment;
import com.ncm.btl_android.fragment.HomeFragment;
import com.ncm.btl_android.lists.Data;

public class AddActivity extends AppCompatActivity{

    private EditText edtID, edtName;
    private Button btnAddData;

    FirebaseUser userCurrent = FirebaseAuth.getInstance().getCurrentUser();
    String UserID = userCurrent.getUid();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        initUI();

        btnAddData.setOnClickListener(v -> {
            int id = Integer.parseInt(edtID.getText().toString().trim());
            String name = edtName.getText().toString().trim();
            Data user = new Data(id, name);
            onClickAddData(user);
        });
    }

    private void initUI(){
        edtID = findViewById(R.id.edt_add_id);
        edtName = findViewById(R.id.edt_add_name);
        btnAddData = findViewById(R.id.btn_add_data);
    }

    private void onClickAddData(Data user){


        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference(UserID);

        String pathObject = String.valueOf(user.getId());
        myRef.child(pathObject).setValue(user, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                Toast.makeText(AddActivity.this, "Add user success!", Toast.LENGTH_SHORT).show();
            }
        });
    }

}