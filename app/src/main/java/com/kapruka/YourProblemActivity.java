package com.kapruka;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kapruka.Adapters.AskAdminAdapter;
import com.kapruka.Models.AskAdminModel;
import com.kapruka.databinding.ActivityYourProblemBinding;

import java.util.ArrayList;
import java.util.List;

public class YourProblemActivity extends AppCompatActivity {
    ActivityYourProblemBinding binding;
    List<AskAdminModel> askAdminModelList;
    AskAdminAdapter askAdminAdapter;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityYourProblemBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        firebaseAuth = FirebaseAuth.getInstance();

        askAdminModelList = new ArrayList<>();
        DatabaseReference askAdmin = FirebaseDatabase.getInstance().getReference("AskAdmin");
        askAdmin.orderByChild("UserID").equalTo(firebaseAuth.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                askAdminModelList.clear();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    AskAdminModel qusetionModel = ds.getValue(AskAdminModel.class);
                    if (qusetionModel != null) {
                        askAdminModelList.add(qusetionModel);
                        askAdminAdapter = new AskAdminAdapter(askAdminModelList, YourProblemActivity.this);
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

        binding.addmycocount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), AdminAskActivity.class));
            }
        });
    }
}