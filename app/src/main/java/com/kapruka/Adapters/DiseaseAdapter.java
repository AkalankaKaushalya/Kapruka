package com.kapruka.Adapters;

import android.content.Context;
import android.content.Intent;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kapruka.DiseaseViewActivity;
import com.kapruka.Models.DiseaseModel;
import com.kapruka.databinding.RowDiseaseBinding;
import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import io.github.mthli.knife.KnifeText;

public class DiseaseAdapter extends RecyclerView.Adapter<DiseaseAdapter.DiseaseHolder> {

    public List<DiseaseModel> diseaseModelList;
    RowDiseaseBinding binding;
    Context context;

    public DiseaseAdapter(List<DiseaseModel> diseaseModelList, Context context) {
        this.diseaseModelList = diseaseModelList;
        this.context = context;
    }

    @NonNull
    @Override
    public DiseaseHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = RowDiseaseBinding.inflate(LayoutInflater.from(context), parent, false);
        return new DiseaseHolder(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull DiseaseHolder holder, int position) {
        DiseaseModel diseaseModel = diseaseModelList.get(position);
        String diseaseid = diseaseModel.getDiseaseID();
        String userid = diseaseModel.getUserID();
        String title = diseaseModel.getDiseaseTitle();
        String discription = diseaseModel.getDiseaseDescription();
        String image = diseaseModel.getDiseaseImage();
        String viewcount = diseaseModel.getViewCount();

        holder.Title.setText(title);
        holder.Discription.fromHtml(discription);
        holder.ViewCount.setText(viewcount);
        Picasso.get().load(image).into(holder.Iamge);

        Calendar calendar = Calendar.getInstance(Locale.ENGLISH);
        calendar.setTimeInMillis(Long.parseLong(diseaseid));
        String data = DateFormat.format("dd/MM/yyyy", calendar).toString();
        holder.Date.setText(data);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DiseaseViewActivity.class);
                intent.putExtra("DiseaseID", diseaseid);
                intent.putExtra("UserID", userid);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return diseaseModelList.size();
    }


    class DiseaseHolder extends RecyclerView.ViewHolder {
        TextView Title, ViewCount, Date;
        KnifeText Discription;
        ImageView Iamge;

        public DiseaseHolder(@NonNull View itemView) {
            super(itemView);
            Title = binding.diseaseTitleTv;
            Discription = binding.diseaseDescriptionTv;
            ViewCount = binding.viewcountTv;
            Date = binding.diseaseDateTv;
            Iamge = binding.diseaseIv;

        }

    }
}
