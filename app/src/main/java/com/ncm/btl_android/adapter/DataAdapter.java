package com.ncm.btl_android.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ncm.btl_android.R;
import com.ncm.btl_android.lists.Data;

import java.util.List;

public class DataAdapter extends RecyclerView.Adapter<DataAdapter.DataViewHolder>{

    private List<Data> mListData;
    private IClickListener iClickListener;

    public DataAdapter(List<Data> mListData) {

    }

    public interface IClickListener{
        void onClickUpdateItem(Data user);
        void onClickDeleteItem(Data user);
    }

    public DataAdapter(List<Data> mListData, IClickListener iClickListener) {
        this.mListData = mListData;
        this.iClickListener = iClickListener;
    }

    @NonNull
    @Override
    public DataViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_data, parent, false);
        return new DataViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DataViewHolder holder, int position) {
        Data user = mListData.get(position);
        if(user == null){
            return;
        }

        holder.tvID.setText("ID: " + user.getId());
        holder.tvName.setText("Work: " + user.getName());

        holder.btnUpdate.setOnClickListener(v -> {
            iClickListener.onClickUpdateItem(user);
        });

        holder.btnDelete.setOnClickListener(v -> {
            iClickListener.onClickDeleteItem(user);
        });
    }

    @Override
    public int getItemCount() {
        if(mListData != null){
            return mListData.size();
        }
        return 0;
    }

    public class DataViewHolder extends RecyclerView.ViewHolder{

        private TextView tvID, tvName;
        private Button btnUpdate, btnDelete;

        public DataViewHolder(@NonNull View itemView) {
            super(itemView);
            initUI();
        }

        private void initUI() {
            tvID = itemView.findViewById(R.id.tv_id);
            tvName = itemView.findViewById(R.id.tv_name);
            btnUpdate = itemView.findViewById(R.id.btn_update);
            btnDelete = itemView.findViewById(R.id.btn_delete);
        }
    }
}
