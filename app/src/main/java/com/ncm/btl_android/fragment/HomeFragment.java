package com.ncm.btl_android.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ncm.btl_android.R;
import com.ncm.btl_android.actions.AddActivity;
import com.ncm.btl_android.lists.User;
import com.sa90.materialarcmenu.ArcMenu;



public class HomeFragment extends Fragment {

    private View mView;

    private EditText edtData;
    private Button btnPushData, btnGetData, btnDeleteData, btnUpdateData;
    private TextView getTvData;

    private ArcMenu arcMenu;
    private ImageView okok;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_home,container, false);
//
        initUI();


        btnPushData.setOnClickListener(v -> {
            onClickPushData();
        });

        btnGetData.setOnClickListener(v -> {
            onClickReadDataBase();
        });

        btnDeleteData.setOnClickListener(v -> {
            onClickDeleteDataBase();
        });

        btnUpdateData.setOnClickListener(v -> {
            onClickUpdateDataBase();
        });

        okok.setOnClickListener(v -> {
            Toast.makeText(getActivity(), "đây là trang sửa ahihi!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getActivity(), AddActivity.class);
            startActivity(intent);

        });
//
        return mView;
    }
//
    private void initUI(){
        edtData = mView.findViewById(R.id.edt_data);
        btnPushData = mView.findViewById(R.id.btn_push_data);
        getTvData = mView.findViewById(R.id.tv_get_data);
        btnGetData = mView.findViewById(R.id.btn_get_data);
        btnDeleteData = mView.findViewById(R.id.btn_delete_data);
        btnUpdateData = mView.findViewById(R.id.btn_update_data);

        arcMenu = mView.findViewById(R.id.arcMenu);

        //test
        okok = mView.findViewById(R.id.fab_add);
    }
//
    private void onClickPushData(){
//        edtData.getText().toString().trim()
//        Write a message to the database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("user_infor");

        User user = new User(1, "Minh");

        myRef.setValue(user);
//        myRef.setValue(edtData.getText().toString().trim(), new DatabaseReference.CompletionListener() {
//            @Override
//            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
//                Toast.makeText(getActivity(), "Push data successfully!", Toast.LENGTH_SHORT).show();
//            }
//        });
    }
//
    private void onClickReadDataBase(){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("user_infor");
        // Read from the database
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                User user = dataSnapshot.getValue(User.class);
                getTvData.setText(user.toString());

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value

            }
        });
    }

    private void onClickDeleteDataBase(){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("user_infor");

        myRef.removeValue(new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                Toast.makeText(getActivity(), "Delete successfully!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void onClickUpdateDataBase(){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("user_infor");

        User user = new User(2, "đẹp trai");

        myRef.setValue(user);


    }

}
