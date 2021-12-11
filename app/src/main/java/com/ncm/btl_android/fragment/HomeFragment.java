package com.ncm.btl_android.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.ncm.btl_android.R;
import com.ncm.btl_android.actions.AddActivity;
import com.ncm.btl_android.adapter.DataAdapter;
import com.ncm.btl_android.lists.Data;
import com.sa90.materialarcmenu.ArcMenu;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

//danh sách mới nhất lên đầu
//nhạc nền
public class HomeFragment extends Fragment {

    private View mView;

    private ArcMenu arcMenu;
    private ImageView viewAddData, sortData;

    private RecyclerView recyclerView;
    private DataAdapter dataAdapter;

    private List<Data> mListData;

    private EditText search;
    private Button search_btn;


    FirebaseUser userCurrent = FirebaseAuth.getInstance().getCurrentUser();
    String UserID = userCurrent.getUid();

    private static final int GET = 0;
    private int CURRENT = 0;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_home,container, false);
        initUI();



        viewAddData.setOnClickListener(v -> {
            Toast.makeText(getActivity(), "đây là trang thêm data!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getActivity(), AddActivity.class);
            startActivity(intent);

//            mListData.clear();
        });

        sortData.setOnClickListener(v -> {
            mListData.clear();
            sortListDataAZ();
            CURRENT = 1;
        });

        if(GET == CURRENT){
            getListDataFromDB();
        }

        search_btn.setOnClickListener(v -> {
            mListData.clear();
            String name = search.getText().toString().trim();
            searchData(name.toLowerCase(Locale.ROOT));
        });


//        mListData.clear();

        return mView;
    }

    private void initUI(){

        arcMenu = mView.findViewById(R.id.arcMenu);

        //
        viewAddData = mView.findViewById(R.id.fab_add);
        sortData = mView.findViewById(R.id.fab_sort);
        //
        recyclerView = mView.findViewById(R.id.rcv_data);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);

        search = mView.findViewById(R.id.search);
        search_btn = mView.findViewById(R.id.search_btn);

        //phân cách các item trong item_data
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);


        mListData = new ArrayList<>();
        dataAdapter = new DataAdapter(mListData, new DataAdapter.IClickListener() {
            @Override
            public void onClickUpdateItem(Data user) {
                openDialogUpdateItem(user);
            }

            @Override
            public void onClickDeleteItem(Data user) {
                openDialogDeleteItem(user);
            }
        });

        recyclerView.setAdapter(dataAdapter);


    }

    private void sortListDataAZ(){

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference(UserID);

        //sort data
        //orderByChild() -> id
        //orderByKey()
        //orderByValue()
        Query query = myRef.orderByChild("id");
        query.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Data user = snapshot.getValue(Data.class);
                if(user != null){
                    mListData.add(0,user);
                    dataAdapter.notifyDataSetChanged();
                }


            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Data user = snapshot.getValue(Data.class);
                if(user == null || mListData == null || mListData.isEmpty()){
                    return;
                }

                for(int i = 0; i< mListData.size(); i++){
                    if(user.getId() == mListData.get(i).getId()){
                        mListData.set(i, user);
                        break;
                    }
                }

                dataAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                Data user = snapshot.getValue(Data.class);
                if(user == null || mListData == null || mListData.isEmpty()){
                    return;
                }

                for(int i = 0; i< mListData.size(); i++){
                    if(user.getId() == mListData.get(i).getId()){
                        mListData.remove(mListData.get(i));
                        break;
                    }
                }

                dataAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    //funcion filtering data
    private void filterData(){

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference(UserID);

        Query query = myRef.orderByChild("id").limitToFirst(3);
        query.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Data user = snapshot.getValue(Data.class);
                if(user != null){
                    mListData.add(user);
                    dataAdapter.notifyDataSetChanged();
                }


            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Data user = snapshot.getValue(Data.class);
                if(user == null || mListData == null || mListData.isEmpty()){
                    return;
                }

                for(int i = 0; i< mListData.size(); i++){
                    if(user.getId() == mListData.get(i).getId()){
                        mListData.set(i, user);
                        break;
                    }
                }

                dataAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                Data user = snapshot.getValue(Data.class);
                if(user == null || mListData == null || mListData.isEmpty()){
                    return;
                }

                for(int i = 0; i< mListData.size(); i++){
                    if(user.getId() == mListData.get(i).getId()){
                        mListData.remove(mListData.get(i));
                        break;
                    }
                }

                dataAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void searchData(String name){

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference(UserID);

        Query query = myRef.orderByChild("id").limitToFirst(3);
        query.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Data user = snapshot.getValue(Data.class);
                if(user != null){
                    if(user.getName().toLowerCase(Locale.ROOT).contains(name)){
                        mListData.add(user);
                    }
                    dataAdapter.notifyDataSetChanged();
                }


            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Data user = snapshot.getValue(Data.class);
                if(user == null || mListData == null || mListData.isEmpty()){
                    return;
                }

                for(int i = 0; i< mListData.size(); i++){
                    if(user.getId() == mListData.get(i).getId()){
                        mListData.set(i, user);
                        break;
                    }
                }

                dataAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                Data user = snapshot.getValue(Data.class);
                if(user == null || mListData == null || mListData.isEmpty()){
                    return;
                }

                for(int i = 0; i< mListData.size(); i++){
                    if(user.getId() == mListData.get(i).getId()){
                        mListData.remove(mListData.get(i));
                        break;
                    }
                }

                dataAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }



//    Filtering data
//    limitToFirst()
//    limitToLast()
//    startAt()
//    startAfter()
//    endAt()
//    endBefore()
//    equalTo()
    private void getListDataFromDB(){

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference(UserID);


        //get data
        //cách 1 không tối ưu UX
//        myRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                if(mListData != null){
//                    mListData.clear();
//                }
//                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
//                    Data user = dataSnapshot.getValue(Data.class);
//                    mListData.add(user);
//                }
//
//                dataAdapter.notifyDataSetChanged();
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//                Toast.makeText(getActivity(), "Get data failed!", Toast.LENGTH_SHORT).show();
//            }
//        });
        //Cách 2
//        Query query = myRef.orderByChild("rate");
        myRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Data user = snapshot.getValue(Data.class);
                if(user != null){

                    mListData.add(user);
                    dataAdapter.notifyDataSetChanged();


                }


            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Data user = snapshot.getValue(Data.class);
                if(user == null || mListData == null || mListData.isEmpty()){
                    return;
                }

                for(int i = 0; i< mListData.size(); i++){
                    if(user.getId() == mListData.get(i).getId()){
                        mListData.set(i, user);
                        break;
                    }
                }

                dataAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                Data user = snapshot.getValue(Data.class);
                if(user == null || mListData == null || mListData.isEmpty()){
                    return;
                }

                for(int i = 0; i< mListData.size(); i++){
                    if(user.getId() == mListData.get(i).getId()){
                        mListData.remove(mListData.get(i));
                        break;
                    }
                }

                dataAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void openDialogUpdateItem(Data user){
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window. FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.layout_dialog_update);
        Window window = dialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(false);


        EditText edtUpdateContent = dialog.findViewById(R.id.edt_update_content);
        Button btnCancelUD = dialog.findViewById(R.id.btn_cancel_UDcontent);
        Button btnUpdateContent = dialog.findViewById(R.id.btn_update_content);

        edtUpdateContent.setText(user.getName());

        btnCancelUD.setOnClickListener(v -> {
            dialog.dismiss();
        });

        btnUpdateContent.setOnClickListener(v -> {

            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference myRef = database.getReference(UserID);

            String newContent = edtUpdateContent.getText().toString().trim();

            user.setName(newContent);
            myRef.child(String.valueOf(user.getId())).updateChildren(user.toMap(), new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                    Toast.makeText(getActivity(), "Update data success!", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }
            });
        });

        dialog.show();
    }

    private void openDialogDeleteItem(Data user){
        new AlertDialog.Builder(getActivity())
                .setTitle(getString(R.string.app_name))
                .setMessage("Bạn có chắc chắn muốn xóa không?")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                        DatabaseReference myRef = database.getReference(UserID);

                        myRef.child(String.valueOf(user.getId())).removeValue(new DatabaseReference.CompletionListener() {
                            @Override
                            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                                Toast.makeText(getActivity(), "Delete data success!", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                    }
                    )
                .setNegativeButton("Cancel", null)
                .show();
    }

}
