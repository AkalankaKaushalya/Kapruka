package com.kapruka.Adapters;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.kapruka.Models.MonthModel;
import com.kapruka.PodactivityActivity;
import com.kapruka.databinding.RowMothBinding;

import java.util.List;

public class MonthAdapter extends RecyclerView.Adapter<MonthAdapter.MonthHolder> {

    public List<MonthModel> monthModelList;
    RowMothBinding binding;
    Context context;
    FirebaseAuth firebaseAuth;

    public MonthAdapter(List<MonthModel> monthModelList, Context context) {
        this.monthModelList = monthModelList;
        this.context = context;
        firebaseAuth = FirebaseAuth.getInstance();
    }

    @NonNull
    @Override
    public MonthHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = RowMothBinding.inflate(LayoutInflater.from(context), parent, false);
        return new MonthHolder(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull MonthHolder holder, int position) {
        MonthModel ytVideoModel = monthModelList.get(position);
        String id = ytVideoModel.getMonth();
        String u_id = ytVideoModel.getUserID();
        

        holder.Date.setText(id);
        holder.Delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (u_id == firebaseAuth.getUid()){
                    final ProgressDialog progressDialog = new ProgressDialog(context);
                    progressDialog.setMessage("Deleting...");
                    progressDialog.show();
                    Query fquery = FirebaseDatabase.getInstance().getReference("Users")
                            .child(firebaseAuth.getUid())
                            .child("Months")
                            .orderByChild("Month")
                            .equalTo(id);
                    fquery.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                ds.getRef().removeValue();
                                Toast.makeText(context, "Deleted", Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            progressDialog.dismiss();
                            Toast.makeText(context, "" + error.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }); 
                }else {
                    Toast.makeText(context, "You Cant Delete \n This UserMonth.", Toast.LENGTH_SHORT).show();
                }
                

            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, PodactivityActivity.class);
                intent.putExtra("Month", id);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return monthModelList.size();
    }

    class MonthHolder extends RecyclerView.ViewHolder {
        TextView Date;
        ImageButton Delete;

        public MonthHolder(@NonNull View itemView) {
            super(itemView);
            Date = binding.dateTv;
            Delete = binding.deleteBtn;
        }
    }
}
