package com.kapruka;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import java.util.HashMap;

public class AuthActivity extends AppCompatActivity {
    private TextInputLayout userNameLL;
    private EditText UserName, Email, Password;
    private Button Loging, Register;
    private TextView Have, New, LoginTitle;
    private SearchableSpinner CitySpinner;
    private MaterialCardView CityCard;
    private String Username = "", password = "", email = "", City;
    private FirebaseAuth firebaseAuth;
    private ArrayAdapter<CharSequence> City_adapter;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        userNameLL = findViewById(R.id.register_username_edit_textLL);
        UserName = findViewById(R.id.register_username_edit_text);
        CitySpinner = findViewById(R.id.city);
        CityCard = findViewById(R.id.city_card);
        Email = findViewById(R.id.register_email_edit_text);
        Password = findViewById(R.id.register_password_edit_text);
        Loging = findViewById(R.id.login_button);
        Register = findViewById(R.id.register_button);
        Have = findViewById(R.id.alredy_memberTv);
        New = findViewById(R.id.new_memberTv);
        LoginTitle = findViewById(R.id.loginTv);

        firebaseAuth = FirebaseAuth.getInstance();

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setTitle("Please Wait");


        New.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userNameLL.setVisibility(View.VISIBLE);
                CityCard.setVisibility(View.VISIBLE);
                Register.setVisibility(View.VISIBLE);
                Have.setVisibility(View.VISIBLE);
                New.setVisibility(View.GONE);
                Loging.setVisibility(View.GONE);
                LoginTitle.setText("Create Account!");
            }
        });

        Have.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userNameLL.setVisibility(View.GONE);
                CityCard.setVisibility(View.GONE);
                Register.setVisibility(View.GONE);
                Have.setVisibility(View.GONE);
                New.setVisibility(View.VISIBLE);
                Loging.setVisibility(View.VISIBLE);
                LoginTitle.setText("Login Now!");
            }
        });

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

        Register.setOnClickListener(new View.OnClickListener() {
            @Override
            // Registation Button Clcik Ivent Eka
            public void onClick(View view) {
                registerUser();
            }
        });

        Loging.setOnClickListener(new View.OnClickListener() {
            @Override                                         // Login Button Clcik Ivent Eka
            public void onClick(View view) {
                loginUser();
            }
        });


    }

    private void registerUser() {
        Username = UserName.getText().toString();
        email = Email.getText().toString();
        password = Password.getText().toString();
        if (Username.isEmpty()) {
            Toast.makeText(this, "UserName is Empty", Toast.LENGTH_SHORT).show();
        } else if (password.isEmpty()) {
            Toast.makeText(this, "Password is Empty", Toast.LENGTH_SHORT).show();
        } else if (email.isEmpty()) {
            Toast.makeText(this, "Email is Empty", Toast.LENGTH_SHORT).show();
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Email Not Valided", Toast.LENGTH_SHORT).show();
        } else if (City.equals("Select Your City")) {
            Toast.makeText(this, "Select Your City", Toast.LENGTH_SHORT).show();
        } else {
            firebaseAuth.createUserWithEmailAndPassword(email, password)
                    .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            progressDialog.setMessage("We were registering you.");
                            progressDialog.show();
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            long timestamp = System.currentTimeMillis();

                            assert user != null;
                            String email = user.getEmail();
                            String uid = user.getUid();

                            HashMap<Object, String> hashMap = new HashMap<>();
                            hashMap.put("Email", email);
                            hashMap.put("UserID", uid);
                            hashMap.put("Name", "" + Username);
                            hashMap.put("City", "" + City);
                            hashMap.put("UserType", "user");
                            hashMap.put("Image", "img");
                            hashMap.put("timesTamp", String.valueOf(timestamp));
                            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
                            reference.child(uid)
                                    .setValue(hashMap)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            progressDialog.dismiss();
                                            Toast.makeText(AuthActivity.this, "" + user.getEmail(), Toast.LENGTH_SHORT).show();
                                            startActivity(new Intent(AuthActivity.this, SplashActivity.class));
                                            finish();
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            progressDialog.dismiss();
                                            Toast.makeText(AuthActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            //progressDialog.dismiss();
                            Toast.makeText(AuthActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    } //User Registation Quari Eka saha Validation Part Eka

    private void loginUser() {
        email = Email.getText().toString();
        password = Password.getText().toString();
        if (password.isEmpty()) {
            Toast.makeText(this, "Password is Empty", Toast.LENGTH_SHORT).show();
        } else if (email.isEmpty()) {
            Toast.makeText(this, "Email is Empty", Toast.LENGTH_SHORT).show();
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Email Not Valided", Toast.LENGTH_SHORT).show();
        } else {
            progressDialog.show();
            firebaseAuth.signInWithEmailAndPassword(email, password)
                    .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            progressDialog.dismiss();
                            Toast.makeText(AuthActivity.this, "Welcome Back \n" + email, Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(AuthActivity.this, SplashActivity.class));
                            finish();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(AuthActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }  //User Login Quari Eka saha Validation Part Eka


    @Override
    protected void onStart() {
        super.onStart();

    }
}