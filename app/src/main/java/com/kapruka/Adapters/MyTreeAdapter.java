package com.kapruka.Adapters;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.kapruka.Models.MyTreeModel;
import com.kapruka.Models.ProductivityModel;
import com.kapruka.PodactivityActivity;
import com.kapruka.R;
import com.kapruka.databinding.RowTreeBinding;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class MyTreeAdapter extends RecyclerView.Adapter<MyTreeAdapter.MyTreeHolder> {

    public List<MyTreeModel> myTreeModelList;
    RowTreeBinding binding;
    Context context;
    ArrayList barArraylist;
    ArrayList<String> a1, a2;
    FirebaseAuth firebaseAuth;
    List<ProductivityModel> productivityModelList;
    limitAdapter limitAdapter;

    public MyTreeAdapter(List<MyTreeModel> myTreeModelList, Context context) {
        this.myTreeModelList = myTreeModelList;
        this.context = context;
        firebaseAuth = FirebaseAuth.getInstance();
    }

    @NonNull
    @Override
    public MyTreeHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = RowTreeBinding.inflate(LayoutInflater.from(context), parent, false);
        return new MyTreeHolder(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull MyTreeHolder holder, int position) {
        MyTreeModel ytVideoModel = myTreeModelList.get(position);
        String id = ytVideoModel.getTreeID();
        String userid = ytVideoModel.getUserID();
        String heigt = ytVideoModel.getTreeHeight();
        String age = ytVideoModel.getTreeAge();


        DatabaseReference userdattaref = FirebaseDatabase.getInstance().getReference("Users");
        userdattaref.child(firebaseAuth.getUid()).child("Months")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String month = "" + snapshot.child("Month").getValue();
                        DatabaseReference ssa = FirebaseDatabase.getInstance().getReference("Users");
                        ssa.child(firebaseAuth.getUid()).child("Productivity").child(month)
                                .addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        String gg = "" + snapshot.child("UserType").getValue();
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {
                                    }
                                });
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });


        holder.TreeNo.setText(id);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(v.getContext());
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.dilog_info_tree);

                TextView treeid = dialog.findViewById(R.id.treeidTv);
                TextView treehigte = dialog.findViewById(R.id.treeheightTv);
                TextView treeage = dialog.findViewById(R.id.treeageTv);
                Button delete = dialog.findViewById(R.id.deleteBtn);
                BarChart barChart = dialog.findViewById(R.id.barchart);

//                a1 = new ArrayList<>();
//                a2 = new ArrayList<>();
//                DatabaseReference mytreeRf = FirebaseDatabase.getInstance().getReference("gg");
//                mytreeRf.child("cbhdcbdch").child("TreeID").equalTo()
//                        .addValueEventListener(new ValueEventListener() {
//                            @Override
//                            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                                //a1.clear();
//                                //a2.clear();
//                                for (DataSnapshot ds : snapshot.getChildren()) {
//                                    String categoryId = ""+ ds.child("ProductCout").getValue();
//                                    //int categoryTitle = Integer.parseInt("" + ds.child("Months").getValue());
//
//                                    //a1.add(categoryTitle);
//                                    a2.add(categoryId);
//
//                                    Toast.makeText(context, ""+ , Toast.LENGTH_SHORT).show();
//                                }
////                                BarEntry[] dp = new BarEntry[(int) snapshot.getChildrenCount()];
////                                int index = 0;
////
////                                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
////                                    ProductivityModel productivityModel = dataSnapshot.getValue(ProductivityModel.class);
////                                    Toast.makeText(context, ""+productivityModel, Toast.LENGTH_SHORT).show();
////                                }
//
//                            }
//
//                            @Override
//                            public void onCancelled(@NonNull DatabaseError error) {
//
//                            }
//                        });


//                productivityModelList = new ArrayList<>();
//                DatabaseReference mytreeRf = FirebaseDatabase.getInstance().getReference("Users");
//                mytreeRf.child(firebaseAuth.getUid()).child("Productivity").child("2022-10").orderByChild("TreeID").equalTo(id)
//                        .addValueEventListener(new ValueEventListener() {
//                            @Override
//                            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                                productivityModelList.clear();
//                                for (DataSnapshot ds : snapshot.getChildren()) {
//                                    ProductivityModel monthModel = ds.getValue(ProductivityModel.class);
//                                    if (monthModel != null) {
//                                        productivityModelList.add(monthModel);
//                                        limitAdapter = new limitAdapter(productivityModelList, context);
//                                        barChart.setAdapter(limitAdapter);
//
//                                    } else {
//
//                                    }
//
//                                }
//                            }
//
//                            @Override
//                            public void onCancelled(@NonNull DatabaseError error) {
//
//                            }
//                        });




                treehigte.setText(heigt);
                treeage.setText(age);
                treeid.setText(id);

                delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (userid == firebaseAuth.getUid()){
                            Query fquery = FirebaseDatabase.getInstance().getReference("Users")
                                    .child(firebaseAuth.getUid())
                                    .child("MyTree").orderByChild("TreeID").equalTo(id);
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
                        }else {
                            Toast.makeText(context, "You Cant Delete\n Other User Data", Toast.LENGTH_SHORT).show();
                        }

                    }
                });

                dialog.show();
                dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
                dialog.getWindow().setGravity(Gravity.BOTTOM);
            }
        });

    }


    @Override
    public int getItemCount() {
        return myTreeModelList.size();
    }

    class MyTreeHolder extends RecyclerView.ViewHolder {
        TextView TreeNo;

        public MyTreeHolder(@NonNull View itemView) {
            super(itemView);
            TreeNo = binding.treeidTv;
        }
    }
}
