package com.kapruka;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kapruka.Adapters.MonthAdapter;
import com.kapruka.Adapters.MyTreeAdapter;
import com.kapruka.Models.MonthModel;
import com.kapruka.Models.MyTreeModel;
import com.kapruka.databinding.ActivityProfileViewBinding;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ProfileViewActivity extends AppCompatActivity {
    ActivityProfileViewBinding binding;
    List<MyTreeModel> myTreeModelList;
    MyTreeAdapter myTreeAdapter;

    List<MonthModel> monthModelList;
    MonthAdapter monthAdapter;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProfileViewBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        Intent intent = getIntent();
        String uid = intent.getStringExtra("UID");

        lodeMyTree(uid);
        lodeMonth(uid);
        loadeUser(uid);


        binding.saleCv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.treeRR.setVisibility(View.VISIBLE);
                binding.monthRR.setVisibility(View.GONE);
                binding.sr.setBackground(getResources().getDrawable(R.drawable.shape_home_catogry, null));
                binding.rr.setBackgroundColor(getResources().getColor(R.color.white));
                binding.saleTv.setTextColor(getResources().getColor(R.color.white));
                binding.rentTv.setTextColor(getResources().getColor(R.color.purple_500));
            }
        });

        binding.rentCv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.treeRR.setVisibility(View.GONE);
                binding.monthRR.setVisibility(View.VISIBLE);
                binding.rr.setBackground(getResources().getDrawable(R.drawable.shape_home_catogry, null));
                binding.sr.setBackgroundColor(getResources().getColor(R.color.white));
                binding.rentTv.setTextColor(getResources().getColor(R.color.white));
                binding.saleTv.setTextColor(getResources().getColor(R.color.purple_500));
            }
        });


    }

    private void lodeMyTree(String uid) {
        myTreeModelList = new ArrayList<>();
        DatabaseReference mytreeRf = FirebaseDatabase.getInstance().getReference("Users");
        mytreeRf.child(uid).child("MyTree").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                myTreeModelList.clear();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    MyTreeModel ytVideoModel = ds.getValue(MyTreeModel.class);
                    if (ytVideoModel != null) {
                        myTreeModelList.add(ytVideoModel);
                        myTreeAdapter = new MyTreeAdapter(myTreeModelList, ProfileViewActivity.this);
                        binding.treeRecyclerView.setAdapter(myTreeAdapter);
                        //NotYourCity.setVisibility(View.GONE);
                    } else {
                        //NotYourCity.setVisibility(View.VISIBLE);
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void lodeMonth(String uid) {
        monthModelList = new ArrayList<>();
        DatabaseReference mytreeRf = FirebaseDatabase.getInstance().getReference("Users");
        mytreeRf.child(uid).child("Months").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                monthModelList.clear();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    MonthModel monthModel = ds.getValue(MonthModel.class);
                    if (monthModel != null) {
                        monthModelList.add(monthModel);
                        monthAdapter = new MonthAdapter(monthModelList, ProfileViewActivity.this);
                        binding.monthRecyclerView.setAdapter(monthAdapter);
                        //NotYourCity.setVisibility(View.GONE);
                    } else {
                        //NotYourCity.setVisibility(View.VISIBLE);
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void loadeUser(String uid) {
        DatabaseReference userdataref = FirebaseDatabase.getInstance().getReference("Users");
        userdataref.child(uid)
                .addValueEventListener(new ValueEventListener() {
                    @SuppressLint("UseCompatLoadingForDrawables")
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String username = "" + snapshot.child("Name").getValue();
                        String email = "" + snapshot.child("Email").getValue();
                        String city = "" + snapshot.child("City").getValue();
                        String ProfilePic = "" + snapshot.child("Image").getValue();
                        String userType = "" + snapshot.child("UserType").getValue();
                        binding.usernameTv.setText(username);
                        binding.emailTv.setText(email);
                        //binding.usertypeTv.setText(userType);
                        binding.cityTv.setText(city);
                        if (ProfilePic.equals("img")) {
                            binding.profileIv.setImageDrawable(getResources().getDrawable(R.drawable.img_profile, null));
                        } else {
                            Picasso.get().load(ProfilePic).into(binding.profileIv, new Callback() {
                                @Override
                                public void onSuccess() {
                                }

                                @Override
                                public void onError(Exception e) {
                                }
                            });
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
    }
}