package com.kapruka.AdminFragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.kapruka.R;
import com.kapruka.databinding.ActivityAddDiseaseBinding;

import java.util.HashMap;

import io.github.mthli.knife.KnifeText;

public class AddDiseaseActivity extends AppCompatActivity {
    ActivityAddDiseaseBinding binding;
    private KnifeText Discription;
    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;

    private Uri Image1;
    private final ActivityResultLauncher<Intent> galleryActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        Image1 = data.getData();

                        binding.addImage.setImageURI(Image1);
                    } else {
                        Toast.makeText(getApplicationContext(), "Cacelled", Toast.LENGTH_SHORT).show();
                    }
                }
            });


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddDiseaseBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setTitle("Uplodaing...");

        Discription = findViewById(R.id.discriptionEt);
        Discription.setSelection(Discription.getEditableText().length());
        discriptionTools();

        binding.addImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                galleryActivityResultLauncher.launch(intent);
            }
        });

        binding.addDiseaseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = binding.titleEt.getText().toString();
                if (title.isEmpty() || Discription.getText().toString().isEmpty()) {
                    Toast.makeText(AddDiseaseActivity.this, "Cheak Your Title & Disease Discription\n is not filled", Toast.LENGTH_SHORT).show();
                } else if (Image1 == null) {
                    Toast.makeText(AddDiseaseActivity.this, "Image not add", Toast.LENGTH_SHORT).show();
                } else {
                    progressDialog.setMessage("Uploading your Disease Image");
                    progressDialog.show();
                    String filePathAndName = "Disease/" + firebaseAuth.getUid() + title;
                    StorageReference ref = FirebaseStorage.getInstance().getReference(filePathAndName);
                    ref.putFile(Image1)
                            .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    progressDialog.dismiss();
                                    Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                                    while (!uriTask.isSuccessful()) ;
                                    String uploadedImageUrl = "" + uriTask.getResult();

                                    progressDialog.setMessage("Uploading your Disease Please Wait");
                                    progressDialog.show();
                                    String timestamp = "" + System.currentTimeMillis();
                                    HashMap<String, Object> hashMap = new HashMap<>();
                                    hashMap.put("DiseaseID", "" + timestamp);
                                    hashMap.put("UserID", "" + firebaseAuth.getUid());
                                    hashMap.put("DiseaseTitle", "" + title);
                                    hashMap.put("DiseaseDescription", "" + Discription.toHtml());
                                    hashMap.put("DiseaseImage", "" + uploadedImageUrl);
                                    hashMap.put("ViewCount", "" + "0");
                                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Disease");
                                    ref.child(timestamp)
                                            .setValue(hashMap)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    progressDialog.dismiss();
                                                    Toast.makeText(getApplicationContext(), "Disease Updated", Toast.LENGTH_SHORT).show();
                                                    startActivity(new Intent(getApplicationContext(), AddDiseaseActivity.class));
                                                    finish();
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    progressDialog.dismiss();
                                                    Toast.makeText(getApplicationContext(), "Not Disease Updated", Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    progressDialog.dismiss();
                                    Toast.makeText(AddDiseaseActivity.this, "Failed Upload Image", Toast.LENGTH_SHORT).show();
                                }
                            });
                }
            }
        });

    }


    //Discription Part //
    private void discriptionTools() {
        ImageButton bold = findViewById(R.id.bold);
        ImageButton italic = findViewById(R.id.italic);
        ImageButton underline = findViewById(R.id.underline);
        ImageButton strikethrough = findViewById(R.id.strikethrough);
        ImageButton bullet = findViewById(R.id.bullet);
        ImageButton quote = findViewById(R.id.quote);
        ImageButton link = findViewById(R.id.link);
        ImageButton clear = findViewById(R.id.clear);

        bold.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Discription.bold(!Discription.contains(KnifeText.FORMAT_BOLD));
            }
        });

        italic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Discription.italic(!Discription.contains(KnifeText.FORMAT_ITALIC));
            }
        });

        underline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Discription.underline(!Discription.contains(KnifeText.FORMAT_UNDERLINED));
            }
        });

        strikethrough.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Discription.strikethrough(!Discription.contains(KnifeText.FORMAT_STRIKETHROUGH));
            }
        });

        bullet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Discription.bullet(!Discription.contains(KnifeText.FORMAT_BULLET));
            }
        });

        quote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Discription.quote(!Discription.contains(KnifeText.FORMAT_QUOTE));
            }
        });

        link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLinkDialog();
            }
        });

        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Discription.clearFormats();
            }
        });
    }

    private void showLinkDialog() {
        final int start = Discription.getSelectionStart();
        final int end = Discription.getSelectionEnd();

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);

        View view = getLayoutInflater().inflate(R.layout.dialog_link, null, false);
        final EditText editText = view.findViewById(R.id.edit);
        builder.setView(view);
        builder.setTitle("Add link");

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String link = editText.getText().toString().trim();
                if (TextUtils.isEmpty(link)) {
                    return;
                }

                // When KnifeText lose focus, use this method
                Discription.link(link, start, end);
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // DO NOTHING HERE
            }
        });

        builder.create().show();
    }
}