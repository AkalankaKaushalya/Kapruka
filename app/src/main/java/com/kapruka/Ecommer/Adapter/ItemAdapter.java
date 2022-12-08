package com.kapruka.Ecommer.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kapruka.Adapters.DiseaseAdapter;
import com.kapruka.Ecommer.Model.ItemModel;
import com.kapruka.Models.DiseaseModel;
import com.kapruka.databinding.RowDiseaseBinding;
import com.kapruka.databinding.RowItemBinding;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.List;

import io.github.mthli.knife.KnifeText;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemHolder> {
    RowItemBinding binding;
    public List<ItemModel> itemModelList;
    Context context;

    public ItemAdapter(List<ItemModel> itemModelList, Context context) {
        this.itemModelList = itemModelList;
        this.context = context;
    }

    @NonNull
    @Override
    public ItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = RowItemBinding.inflate(LayoutInflater.from(context), parent, false);
        return new ItemHolder(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull ItemHolder holder, int position) {
        ItemModel itemModel = itemModelList.get(position);
        String itemid = itemModel.getItmeID();
        String userid = itemModel.getUserID();
        String title = itemModel.getItmeTitle();
        String price = itemModel.getItmePrice();
        String image = itemModel.getDiseaseImage();
        String city = itemModel.getItmeCity();
        String disecription = itemModel.getItmeDescription();

        holder.Title.setText(title);
        DecimalFormat f = new DecimalFormat("#,###");
        holder.Prise.setText("Rs:" + f.format(Double.parseDouble(price)));
        holder.City.setText(city);

        Picasso.get().load(image).into(holder.Iamge);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }

    @Override
    public int getItemCount() {
        return itemModelList.size();
    }

    class ItemHolder extends RecyclerView.ViewHolder {
        TextView Title, Prise, City;
        //KnifeText Discription;
        ImageView Iamge;

        public ItemHolder(@NonNull View itemView) {
            super(itemView);
            Title = binding.itemnameTv;
            Prise = binding.priceTv;
            City = binding.locationTv;
            Iamge = binding.itemimageIV;

        }

    }
}
