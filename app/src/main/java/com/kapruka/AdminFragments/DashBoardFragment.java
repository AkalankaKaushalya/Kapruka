package com.kapruka.AdminFragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kapruka.Adapters.AskAdminAdapter;
import com.kapruka.Ecommer.Online_ShopActivity;
import com.kapruka.Models.AskAdminModel;
import com.kapruka.databinding.FragmentDashBoardBinding;

import java.util.ArrayList;
import java.util.List;

public class DashBoardFragment extends Fragment {


    List<AskAdminModel> askAdminModelList;
    AskAdminAdapter askAdminAdapter;
    FirebaseAuth firebaseAuth;
    private FragmentDashBoardBinding binding;

    public DashBoardFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentDashBoardBinding.inflate(LayoutInflater.from(getContext()), container, false);
        firebaseAuth = FirebaseAuth.getInstance();


        askAdminModelList = new ArrayList<>();
        DatabaseReference askAdmin = FirebaseDatabase.getInstance().getReference("AskAdmin");
        askAdmin.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                askAdminModelList.clear();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    AskAdminModel qusetionModel = ds.getValue(AskAdminModel.class);
                    if (qusetionModel != null) {
                        askAdminModelList.add(qusetionModel);
                        askAdminAdapter = new AskAdminAdapter(askAdminModelList, getContext());
                        binding.askyourqRecyclerView.setAdapter(askAdminAdapter);
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

        binding.fabAdddisease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), AddDiseaseActivity.class));
            }
        });
        binding.fabAddvideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), AddVideoActivity.class));
            }
        });

        binding.fabAddproduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), Online_ShopActivity.class));
            }
        });

        return binding.getRoot();
    }
}