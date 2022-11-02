package com.kapruka;

import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kapruka.databinding.ActivityDiseaseViewBinding;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

public class DiseaseViewActivity extends AppCompatActivity {
    ActivityDiseaseViewBinding binding;
    String image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDiseaseViewBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent = getIntent();
        String diseaseid = intent.getStringExtra("DiseaseID");
        String userid = intent.getStringExtra("UserID");

        loadeDiseases(diseaseid);
        loadeUser(userid);
        viewCountAdd(diseaseid);

        binding.backBtnIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        binding.diseaseIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SingelImageViewActivity.class);
                intent.putExtra("ImageUrl", image);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getApplicationContext().startActivity(intent);
            }
        });
    }

    private void viewCountAdd(String diseaseid) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Disease");
        ref.child(diseaseid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String viewContent = "" + snapshot.child("ViewCount").getValue();
                if (viewContent.equals("") || viewContent.equals("null")) {
                    viewContent = "0";
                }

                long newViewCount = Long.parseLong(viewContent) + 1;

                HashMap<String, Object> hashMap = new HashMap<>();
                hashMap.put("ViewCount", "" + newViewCount);

                DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Disease");
                ref.child(diseaseid).updateChildren(hashMap);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void loadeUser(String userid) {
        DatabaseReference userdataref = FirebaseDatabase.getInstance().getReference("Users");
        userdataref.child(userid)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String username = "" + snapshot.child("Name").getValue();
                        String ProfilePic = "" + snapshot.child("Image").getValue();
                        String desicnation = "" + snapshot.child("Designation").getValue();
                        binding.usernameTv.setText(username);
                        binding.designtionTv.setText(desicnation);
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

    private void loadeDiseases(String diseaseid) {
        DatabaseReference Diseaseref = FirebaseDatabase.getInstance().getReference("Disease");
        Diseaseref.child(diseaseid)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String titel = "" + snapshot.child("DiseaseTitle").getValue();
                        image = "" + snapshot.child("DiseaseImage").getValue();
                        String disease = "" + snapshot.child("DiseaseDescription").getValue();
                        String viewcount = "" + snapshot.child("ViewCount").getValue();
                        binding.title.setText(titel);
                        binding.diseaseDescriptionTv.fromHtml(disease);
                        binding.viewcountTv.setText(viewcount);

                        Calendar calendar = Calendar.getInstance(Locale.ENGLISH);
                        calendar.setTimeInMillis(Long.parseLong(diseaseid));
                        String data = DateFormat.format("dd/MM/yyyy", calendar).toString();
                        binding.diseaseDateTv.setText(data);

                        if (image.equals("img")) {
                            binding.diseaseIv.setImageDrawable(getResources().getDrawable(R.drawable.ic_add_image, null));
                        } else {
                            Picasso.get().load(image).into(binding.diseaseIv, new Callback() {
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