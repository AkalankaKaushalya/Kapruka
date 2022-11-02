package com.kapruka;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.MediaController;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.kapruka.databinding.ActivityAddQusetionBinding;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class AddQusetionActivity extends AppCompatActivity {
    ActivityAddQusetionBinding binding;
    MediaController mediaController;
    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;
    private String City;

    private Uri ImageUrl;
    private final ActivityResultLauncher<Intent> galleryActivityResultLauncherImage = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        ImageUrl = data.getData();

                        binding.captionsimageIv.setImageURI(ImageUrl);
                        binding.captionsimageIv.setVisibility(View.VISIBLE);
                    } else {
                        binding.captionsimageIv.setVisibility(View.GONE);
                        Toast.makeText(getApplicationContext(), "Cacelled", Toast.LENGTH_SHORT).show();
                    }
                }
            });

    private Uri VideoUrl;
    private final ActivityResultLauncher<Intent> galleryActivityResultLauncherVideo = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        VideoUrl = data.getData();

                        binding.videoVv.setVisibility(View.VISIBLE);
                        binding.videoVv.setVideoURI(VideoUrl);

                    } else {
                        binding.videoVv.setVisibility(View.GONE);
                        Toast.makeText(getApplicationContext(), "Cacelled", Toast.LENGTH_SHORT).show();
                    }
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddQusetionBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setTitle("Uplodaing...");

        mediaController = new MediaController(this);
        binding.videoVv.setMediaController(mediaController);
        binding.videoVv.start();

        loadeUser();

        binding.addImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                galleryActivityResultLauncherImage.launch(intent);
            }
        });

        binding.addVideoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("video/*");
                galleryActivityResultLauncherVideo.launch(intent);
            }
        });

        binding.addsendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = binding.problemEt.getText().toString();
                if (text.isEmpty()) {
                    Toast.makeText(AddQusetionActivity.this, "Enter Discription", Toast.LENGTH_SHORT).show();
                } else {
                    if (ImageUrl != null && VideoUrl != null) {
                        loadeVideoandImage(text);
                    } else if (VideoUrl != null) {
                        loadeVideo(text);
                    } else if (ImageUrl != null) {
                        loadeImage(text);
                    } else {
                        loadeText(text);
                    }
                }
            }
        });
    }

    private void loadeUser() {
        DatabaseReference userdataref = FirebaseDatabase.getInstance().getReference("Users");
        userdataref.child(firebaseAuth.getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String username = "" + snapshot.child("Name").getValue();
                        String ProfilePic = "" + snapshot.child("Image").getValue();
                        City = "" + snapshot.child("City").getValue();
                        binding.usernameTv.setText("Hello " + username);
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

    private void loadeText(String text) {
        String timestamp = "" + System.currentTimeMillis();
        progressDialog.setMessage("Uploading your Qusetion Please Wait");
        progressDialog.show();

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("QusetionID", "" + timestamp);
        hashMap.put("UserID", "" + firebaseAuth.getUid());
        hashMap.put("City", "" + City);
        hashMap.put("QusetionTitle", "" + text);
        hashMap.put("QusetionImage", "" + "img");
        hashMap.put("QusetionVideo", "" + "video");
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Qusetion");
        ref.child(timestamp)
                .setValue(hashMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), "Qusetion Updated", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(), AddQusetionActivity.class));
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), "Not Qusetion Updated", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void loadeImage(String text) {
        String timestamp = "" + System.currentTimeMillis();
        progressDialog.setMessage("Uploading your Qusetion Image");
        progressDialog.show();
        String filePathAndName = "Qusetion/" + timestamp + "/" + "Image/" + "img";  //Qusetion/0112514525/Image/
        StorageReference ref = FirebaseStorage.getInstance().getReference(filePathAndName);
        ref.putFile(ImageUrl)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                        while (!uriTask.isSuccessful()) ;
                        String uploadedImageUrl = "" + uriTask.getResult();

                        progressDialog.setMessage("Uploading your Qusetion Please Wait");
                        progressDialog.show();

                        HashMap<String, Object> hashMap = new HashMap<>();
                        hashMap.put("QusetionID", "" + timestamp);
                        hashMap.put("UserID", "" + firebaseAuth.getUid());
                        hashMap.put("City", "" + City);
                        hashMap.put("QusetionTitle", "" + text);
                        hashMap.put("QusetionImage", "" + uploadedImageUrl);
                        hashMap.put("QusetionVideo", "" + "video");
                        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Qusetion");
                        ref.child(timestamp)
                                .setValue(hashMap)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        progressDialog.dismiss();
                                        Toast.makeText(getApplicationContext(), "Qusetion Updated", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(getApplicationContext(), AddQusetionActivity.class));
                                        finish();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        progressDialog.dismiss();
                                        Toast.makeText(getApplicationContext(), "Not Qusetion Updated", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(AddQusetionActivity.this, "Failed Upload Image", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void loadeVideo(String text) {
        String timestamp = "" + System.currentTimeMillis();
        progressDialog.setMessage("Uploading your Qusetion Video");
        progressDialog.show();
        String filePathAndName = "Qusetion/" + timestamp + "/" + "Video/" + "video";  //Qusetion/0112514525/Image/
        StorageReference ref = FirebaseStorage.getInstance().getReference(filePathAndName);
        ref.putFile(VideoUrl)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                        while (!uriTask.isSuccessful()) ;
                        String uploadedVideoUrl = "" + uriTask.getResult();

                        progressDialog.setMessage("Uploading your Qusetion Please Wait");
                        progressDialog.show();

                        HashMap<String, Object> hashMap = new HashMap<>();
                        hashMap.put("QusetionID", "" + timestamp);
                        hashMap.put("UserID", "" + firebaseAuth.getUid());
                        hashMap.put("City", "" + City);
                        hashMap.put("QusetionTitle", "" + text);
                        hashMap.put("QusetionImage", "" + "img");
                        hashMap.put("QusetionVideo", "" + uploadedVideoUrl);
                        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Qusetion");
                        ref.child(timestamp)
                                .setValue(hashMap)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        progressDialog.dismiss();
                                        Toast.makeText(getApplicationContext(), "Qusetion Updated", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(getApplicationContext(), AddQusetionActivity.class));
                                        finish();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        progressDialog.dismiss();
                                        Toast.makeText(getApplicationContext(), "Not Qusetion Updated", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(AddQusetionActivity.this, "Failed Upload Image", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void loadeVideoandImage(String text) {
        String timestamp = "" + System.currentTimeMillis();
        progressDialog.setMessage("Uploading your Qusetion Image");
        progressDialog.show();
        String filePathAndName = "Qusetion/" + timestamp + "/" + "Image/" + "img";  //Qusetion/0112514525/Image/
        StorageReference imgref = FirebaseStorage.getInstance().getReference(filePathAndName);
        imgref.putFile(ImageUrl)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                        while (!uriTask.isSuccessful()) ;
                        String uploadedImageUrl = "" + uriTask.getResult();
                        progressDialog.setMessage("Uploading your Qusetion Video");
                        progressDialog.show();
                        String filePathAndName = "Qusetion/" + timestamp + "/" + "Video/" + "video";  //Qusetion/0112514525/Image/
                        StorageReference ref = FirebaseStorage.getInstance().getReference(filePathAndName);
                        ref.putFile(VideoUrl)
                                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                        Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                                        while (!uriTask.isSuccessful()) ;
                                        String uploadedVideoUrl = "" + uriTask.getResult();
                                        progressDialog.setMessage("Uploading your Qusetion Please Wait");
                                        progressDialog.show();
                                        HashMap<String, Object> hashMap = new HashMap<>();
                                        hashMap.put("QusetionID", "" + timestamp);
                                        hashMap.put("UserID", "" + firebaseAuth.getUid());
                                        hashMap.put("City", "" + City);
                                        hashMap.put("QusetionTitle", "" + text);
                                        hashMap.put("QusetionImage", "" + uploadedImageUrl);
                                        hashMap.put("QusetionVideo", "" + uploadedVideoUrl);
                                        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Qusetion");
                                        ref.child(timestamp)
                                                .setValue(hashMap)
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        progressDialog.dismiss();
                                                        Toast.makeText(getApplicationContext(), "Qusetion Updated", Toast.LENGTH_SHORT).show();
                                                        startActivity(new Intent(getApplicationContext(), AddQusetionActivity.class));
                                                        finish();
                                                    }
                                                })
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        progressDialog.dismiss();
                                                        Toast.makeText(getApplicationContext(), "Not Qusetion Updated", Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        progressDialog.dismiss();
                                        Toast.makeText(AddQusetionActivity.this, "Failed Upload Image", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(AddQusetionActivity.this, "Failed Upload Image", Toast.LENGTH_SHORT).show();
                    }
                });

    }
}

