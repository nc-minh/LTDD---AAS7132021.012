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
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ncm.btl_android.R;
import com.ncm.btl_android.actions.AddActivity;
import com.ncm.btl_android.adapter.DataAdapter;
import com.ncm.btl_android.lists.User;
import com.sa90.materialarcmenu.ArcMenu;

import java.util.ArrayList;
import java.util.List;


public class HomeFragment extends Fragment {

    private View mView;

    private ArcMenu arcMenu;
    private ImageView viewAddData;

    private RecyclerView recyclerView;
    private DataAdapter dataAdapter;

    private List<User> mListUser;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_home,container, false);
        initUI();


        viewAddData.setOnClickListener(v -> {
            Toast.makeText(getActivity(), "đây là trang thêm data!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getActivity(), AddActivity.class);
            startActivity(intent);

            mListUser.clear();
        });

        getListUsersFromDB();
        return mView;
    }

    private void initUI(){

        arcMenu = mView.findViewById(R.id.arcMenu);

        //
        viewAddData = mView.findViewById(R.id.fab_add);
        //
        recyclerView = mView.findViewById(R.id.rcv_data);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);

        //phân cách các item trong item_data
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);

        mListUser = new ArrayList<>();
        dataAdapter = new DataAdapter(mListUser);

        recyclerView.setAdapter(dataAdapter);
    }

    private void getListUsersFromDB(){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("list_users");

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    User user = dataSnapshot.getValue(User.class);
                    mListUser.add(user);
                }

                dataAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getActivity(), "Get data failed!", Toast.LENGTH_SHORT).show();
            }
        });

    }

}
