package com.ncm.btl_android.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ncm.btl_android.R;
import com.ncm.btl_android.lists.User;

import java.util.List;

public class DataAdapter extends RecyclerView.Adapter<DataAdapter.DataViewHolder>{

    private List<User> mListUser;

    public DataAdapter(List<User> mListUser) {
        this.mListUser = mListUser;
    }

    @NonNull
    @Override
    public DataViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_data, parent, false);
        return new DataViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DataViewHolder holder, int position) {
        User user = mListUser.get(position);
        if(user == null){
            return;
        }

        holder.tvID.setText("ID: " + user.getId());
        holder.tvName.setText("Name: " + user.getName());
    }

    @Override
    public int getItemCount() {
        if(mListUser != null){
            return mListUser.size();
        }
        return 0;
    }

    public class DataViewHolder extends RecyclerView.ViewHolder{

        private TextView tvID, tvName;

        public DataViewHolder(@NonNull View itemView) {
            super(itemView);
            initUI();
        }

        private void initUI() {
            tvID = itemView.findViewById(R.id.tv_id);
            tvName = itemView.findViewById(R.id.tv_name);
        }
    }
}
