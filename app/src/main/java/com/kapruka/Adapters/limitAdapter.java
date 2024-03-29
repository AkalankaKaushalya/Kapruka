package com.kapruka.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kapruka.Models.ProductivityModel;
import com.kapruka.databinding.RowLimitBinding;

import java.util.List;

public class limitAdapter extends RecyclerView.Adapter<limitAdapter.LimitHolder> {

    public List<ProductivityModel> diseaseModelList;
    RowLimitBinding binding;
    Context context;

    public limitAdapter(List<ProductivityModel> diseaseModelList, Context context) {
        this.diseaseModelList = diseaseModelList;
        this.context = context;
    }

    @NonNull
    @Override
    public LimitHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = RowLimitBinding.inflate(LayoutInflater.from(context), parent, false);
        return new LimitHolder(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull LimitHolder holder, int position) {
        ProductivityModel diseaseModel = diseaseModelList.get(position);
        String pcount = diseaseModel.getProductCout();

        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) holder.LimitLL.getLayoutParams();
        params.height = Integer.parseInt(pcount);

    }

    @Override
    public int getItemCount() {
        return diseaseModelList.size();
    }


    class LimitHolder extends RecyclerView.ViewHolder {
        //TextView Title, ViewCount, Date;
        LinearLayout LimitLL;

        public LimitHolder(@NonNull View itemView) {
            super(itemView);
            LimitLL = binding.limitLL;
        }

    }
}
