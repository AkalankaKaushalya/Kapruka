package com.kapruka.AdminFragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.kapruka.MainActivity;
import com.kapruka.databinding.ActivityAddVideoBinding;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AddVideoActivity extends AppCompatActivity {
    String VideoTumbel = null;
    ActivityAddVideoBinding binding;
    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddVideoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please wait");
        progressDialog.setCanceledOnTouchOutside(false);

        firebaseAuth = FirebaseAuth.getInstance();

        binding.videoUrlEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String youTubeUrl = binding.videoUrlEt.getText().toString();
                if (youTubeUrl.isEmpty()) {

                } else {
                    String video_id = "";

                    String expression = "^.*((youtu.be" + "\\/)" + "|(v\\/)|(\\/u\\/w\\/)|(embed\\/)|(watch\\?))\\??v?=?([^#\\&\\?]*).*"; // var regExp = /^.*((youtu.be\/)|(v\/)|(\/u\/\w\/)|(embed\/)|(watch\?))\??v?=?([^#\&\?]*).*/;
                    CharSequence input = youTubeUrl;
                    Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
                    Matcher matcher = pattern.matcher(input);
                    if (matcher.matches()) {
                        String groupIndex1 = matcher.group(7);
                        if (groupIndex1 != null && groupIndex1.length() == 11)
                            video_id = groupIndex1;

                        String igm = video_id;
                        VideoTumbel = "https://img.youtube.com/vi/" + igm + "/mqdefault.jpg";
                        //Uid.setText(imgurl);
                        Picasso.get().load(VideoTumbel).into(binding.imgThumnail, new Callback() {
                            @Override
                            public void onSuccess() {
                                binding.addVideoBtn.setVisibility(View.VISIBLE);
                            }

                            @Override
                            public void onError(Exception e) {
                                binding.addVideoBtn.setVisibility(View.GONE);
                            }
                        });
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        binding.addVideoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String VideoUrl = binding.videoUrlEt.getText().toString();
                String VideoTitle = binding.videotitleEt.getText().toString();

                if (VideoUrl.isEmpty() || VideoTitle.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Filed Empty", Toast.LENGTH_SHORT).show();
                } else {
                    progressDialog.setMessage("Updating Video..");
                    progressDialog.show();
                    long timestamp = System.currentTimeMillis();

                    HashMap<String, Object> hashMap = new HashMap<>();
                    hashMap.put("VideoID", "" + timestamp);
                    hashMap.put("UserID", "" + firebaseAuth.getUid());
                    hashMap.put("VideoUrl", "" + VideoUrl);
                    hashMap.put("VideoTitle", "" + VideoTitle);
                    hashMap.put("VideoImge", "" + VideoTumbel);
                    hashMap.put("ViewCount", "" + "0");

                    DatabaseReference rfe = FirebaseDatabase.getInstance().getReference("YtVideos");
                    rfe.child("" + timestamp)
                            .setValue(hashMap)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    progressDialog.dismiss();
                                    Toast.makeText(getApplicationContext(), "Add Video Successfuly...", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                    finish();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    progressDialog.dismiss();
                                    Toast.makeText(getApplicationContext(), "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                }
            }
        });
    }
}