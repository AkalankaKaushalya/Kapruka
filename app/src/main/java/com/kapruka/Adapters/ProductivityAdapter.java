package com.kapruka.Adapters;

import android.content.Context;
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
import com.kapruka.Models.ProductivityModel;
import com.kapruka.databinding.RowProductivityBinding;

import java.util.List;

public class ProductivityAdapter extends RecyclerView.Adapter<ProductivityAdapter.ProdactHolder> {

    public List<ProductivityModel> productivityModelList;
    RowProductivityBinding binding;
    Context context;
    FirebaseAuth firebaseAuth;

    public ProductivityAdapter(List<ProductivityModel> productivityModelList, Context context) {
        this.productivityModelList = productivityModelList;
        this.context = context;
        firebaseAuth = FirebaseAuth.getInstance();
    }

    @NonNull
    @Override
    public ProdactHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = RowProductivityBinding.inflate(LayoutInflater.from(context), parent, false);
        return new ProdactHolder(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull ProdactHolder holder, int position) {
        ProductivityModel ytVideoModel = productivityModelList.get(position);
        String id = ytVideoModel.getTreeID();
        String count = ytVideoModel.getProductCout();
        String month = ytVideoModel.getMonths();
        String uid = ytVideoModel.getUserID();

        holder.TreeNo.setText(id);
        holder.ProdactCount.setText(count);


        holder.DelteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (uid == firebaseAuth.getUid()) {
                    Query fquery = FirebaseDatabase.getInstance().getReference("Users")
                            .child(firebaseAuth.getUid())
                            .child("Productivity")
                            .child(month).orderByChild("TreeID").equalTo(id);
                    fquery.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                ds.getRef().removeValue();
                            }
                            Toast.makeText(context, "Deleted", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                } else {
                    Toast.makeText(context, "Your Can't Delete \nThis User Product", Toast.LENGTH_SHORT).show();
                }


            }
        });

//        holder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                final Dialog dialog = new Dialog(context);
//                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//                dialog.setContentView(R.layout.dilog_info_tree);
//
//                TextView treeid = dialog.findViewById(R.id.treeidTv);
//                TextView treehigte = dialog.findViewById(R.id.treeheightTv);
//                TextView treeage = dialog.findViewById(R.id.treeageTv);
//                Button delete = dialog.findViewById(R.id.deleteBtn);
//
//                treehigte.setText(heigt);
//                treeage.setText(age);
//                treeid.setText(id);
//
//                delete.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        final ProgressDialog progressDialog = new ProgressDialog(context);
//                        progressDialog.setMessage("Deleting...");
//                        progressDialog.show();
//                        Query fquery = FirebaseDatabase.getInstance().getReference("Users")
//                                .child(firebaseAuth.getUid())
//                                .child("MyTree").orderByChild("TreeID").equalTo(id);
//                        fquery.addListenerForSingleValueEvent(new ValueEventListener() {
//                            @Override
//                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                                for (DataSnapshot ds: dataSnapshot.getChildren()){
//                                    ds.getRef().removeValue();
//                                }
//                                Toast.makeText(context, "Deleted", Toast.LENGTH_SHORT).show();
//                                progressDialog.dismiss();
//                            }
//                            @Override
//                            public void onCancelled(@NonNull DatabaseError error) {
//
//                            }
//                        });
//                    }
//                });
//
//                dialog.show();
//                dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
//                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//                dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
//                dialog.getWindow().setGravity(Gravity.BOTTOM);
//            }
//        });

    }

    @Override
    public int getItemCount() {
        return productivityModelList.size();
    }

    class ProdactHolder extends RecyclerView.ViewHolder {
        TextView TreeNo, ProdactCount;
        ImageButton DelteBtn;

        public ProdactHolder(@NonNull View itemView) {
            super(itemView);
            TreeNo = binding.treeIdTv;
            ProdactCount = binding.prdactcountTv;
            DelteBtn = binding.deleteBtn;
        }
    }
}
