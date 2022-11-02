package com.kapruka;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kapruka.Adapters.ProductivityAdapter;
import com.kapruka.Models.ProductivityModel;
import com.kapruka.databinding.ActivityPodactivityBinding;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PodactivityActivity extends AppCompatActivity {
    ActivityPodactivityBinding binding;
    List<ProductivityModel> productivityModelList;
    ProductivityAdapter productivityAdapter;
    private FirebaseAuth firebaseAuth;
    private String TreeId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPodactivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        firebaseAuth = FirebaseAuth.getInstance();
        Intent intent = getIntent();
        String moonth = intent.getStringExtra("Month");

        loadePoudact(moonth);

        binding.addpodact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(PodactivityActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.dilog_add_podactivity);

                EditText count = dialog.findViewById(R.id.countpodactEt);
                Spinner selectid = dialog.findViewById(R.id.addtreeid);
                Button addbtn = dialog.findViewById(R.id.addproductivityBtn);

                loadMyTree(selectid);

                addbtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String countp = count.getText().toString();
                        if (countp.isEmpty()) {
                            Toast.makeText(getApplicationContext(), "Enter Count Productivity", Toast.LENGTH_SHORT).show();
                        } else {
                            HashMap<String, Object> hashMap = new HashMap<>();
                            hashMap.put("TreeID", "" + TreeId);
                            hashMap.put("UserID", "" + firebaseAuth.getUid());
                            hashMap.put("Months", "" + moonth);
                            hashMap.put("ProductCout", "" + countp);
                            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
                            ref.child(firebaseAuth.getUid()).child("Productivity").child(moonth).child(TreeId)
                                    .setValue(hashMap)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            //startActivity(new Intent(getApplicationContext(), PodactivityActivity.class));
                                            Toast.makeText(getApplicationContext(), "Tree Updated", Toast.LENGTH_SHORT).show();
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(getApplicationContext(), "Not Tree Updated", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        }
                    }
                });


                dialog.show();
                dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
                dialog.getWindow().setGravity(Gravity.CENTER);
            }
        });

    }

    private void loadePoudact(String moonth) {
        productivityModelList = new ArrayList<>();
        DatabaseReference mytreeRf = FirebaseDatabase.getInstance().getReference("Users");
        mytreeRf.child(firebaseAuth.getUid())
                .child("Productivity")
                .child(moonth).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        productivityModelList.clear();
                        for (DataSnapshot ds : snapshot.getChildren()) {
                            ProductivityModel monthModel = ds.getValue(ProductivityModel.class);
                            if (monthModel != null) {
                                productivityModelList.add(monthModel);
                                productivityAdapter = new ProductivityAdapter(productivityModelList, PodactivityActivity.this);
                                binding.podactivtyRecyclerView.setAdapter(productivityAdapter);
                                binding.nolottie.setVisibility(View.GONE);
                            } else {
                                binding.nolottie.setVisibility(View.VISIBLE);
                            }

                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    private void loadMyTree(Spinner spinner) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
        ref.child(firebaseAuth.getUid()).child("MyTree").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                final List<String> nomeConsulta = new ArrayList<String>();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    String categoryTitle = "" + ds.child("TreeID").getValue();
                    nomeConsulta.add(categoryTitle);

                    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(PodactivityActivity.this, android.R.layout.simple_spinner_item, nomeConsulta);
                    arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner.setAdapter(arrayAdapter);
                    spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            TreeId = spinner.getSelectedItem().toString();

                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

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