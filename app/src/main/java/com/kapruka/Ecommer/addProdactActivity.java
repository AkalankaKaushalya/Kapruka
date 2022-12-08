package com.kapruka.Ecommer;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.exoplayer2.offline.ProgressiveDownloader;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.kapruka.AdminFragments.AddDiseaseActivity;
import com.kapruka.R;
import com.kapruka.databinding.ActivityAddDiseaseBinding;
import com.kapruka.databinding.ActivityAddProdactBinding;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import java.util.HashMap;

import io.github.mthli.knife.KnifeText;

public class addProdactActivity extends AppCompatActivity {
    private KnifeText Discription;
    private ArrayAdapter<CharSequence> City_adapter;
    private SearchableSpinner CitySpinner;
    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;
    ActivityAddProdactBinding binding;
    String City;

    private Uri Image1;
    private final ActivityResultLauncher<Intent> galleryActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        Image1 = data.getData();

                        binding.image1.setImageURI(Image1);
                    } else {
                        Toast.makeText(getApplicationContext(), "Cacelled", Toast.LENGTH_SHORT).show();
                    }
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddProdactBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Discription = findViewById(R.id.discriptionEt);
        CitySpinner = findViewById(R.id.city);
        Discription.setSelection(Discription.getEditableText().length());
        discriptionTools();

        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setTitle("Uplodaing...");

        City_adapter = ArrayAdapter.createFromResource(this, R.array.srilanka_city, R.layout.spinner_layout);
        City_adapter.setDropDownViewResource(com.airbnb.lottie.R.layout.support_simple_spinner_dropdown_item);
        CitySpinner.setAdapter(City_adapter);
        CitySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                City = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        findViewById(R.id.image1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                galleryActivityResultLauncher.launch(intent);
            }
        });



        findViewById(R.id.postBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = binding.titleEt.getText().toString();
                String price = binding.priceEt.getText().toString();

                if (title.isEmpty() || price.isEmpty() ||  Discription.getText().toString().isEmpty()) {
                    Toast.makeText(addProdactActivity.this, "Cheak Your Title, Price & Disease Discription\n is not filled", Toast.LENGTH_SHORT).show();
                } else if (Image1 == null) {
                    Toast.makeText(addProdactActivity.this, "important! \n First Image not add", Toast.LENGTH_SHORT).show();
                } else if (City == "Select Your City") {
                    Toast.makeText(addProdactActivity.this, "Select City", Toast.LENGTH_SHORT).show();
                }
                else {
                    progressDialog.setMessage("Uploading your Product Image");
                    progressDialog.show();
                    String filePathAndName = "Shop/" + firebaseAuth.getUid() + title;
                    StorageReference ref = FirebaseStorage.getInstance().getReference(filePathAndName);
                    ref.putFile(Image1)
                            .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    //progressDialog.dismiss();
                                    Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                                    while (!uriTask.isSuccessful()) ;
                                    String uploadedImageUrl = "" + uriTask.getResult();

                                    progressDialog.setMessage("Uploading your Disease Please Wait");
                                    progressDialog.show();
                                    String timestamp = "" + System.currentTimeMillis();
                                    HashMap<String, Object> hashMap = new HashMap<>();
                                    hashMap.put("ItmeID", "" + timestamp);
                                    hashMap.put("UserID", "" + firebaseAuth.getUid());
                                    hashMap.put("ItmeTitle", "" + title);
                                    hashMap.put("ItmePrice", "" + price);
                                    hashMap.put("ItmeCity", "" + City);
                                    hashMap.put("ItmeDescription", "" + Discription.toHtml());
                                    hashMap.put("DiseaseImage", "" + uploadedImageUrl);
                                    hashMap.put("ViewCount", "" + "0");
                                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Items");
                                    ref.child(timestamp)
                                            .setValue(hashMap)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    progressDialog.dismiss();
                                                    Toast.makeText(getApplicationContext(), "Item Publish", Toast.LENGTH_SHORT).show();
                                                    startActivity(new Intent(getApplicationContext(), addProdactActivity.class));
                                                    finish();
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    progressDialog.dismiss();
                                                    Toast.makeText(getApplicationContext(), "Not item Publish", Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    progressDialog.dismiss();
                                    Toast.makeText(getApplicationContext(), "Failed Upload Image", Toast.LENGTH_SHORT).show();
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