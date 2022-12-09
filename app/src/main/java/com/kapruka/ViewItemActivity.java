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
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.kapruka.databinding.ActivityViewItemBinding;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.stripe.android.ApiResultCallback;
import com.stripe.android.PaymentIntentResult;
import com.stripe.android.Stripe;
import com.stripe.android.model.PaymentIntent;
import com.stripe.android.model.PaymentMethodCreateParams;
import com.stripe.android.view.CardInputWidget;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ViewItemActivity extends AppCompatActivity {
    private ActivityViewItemBinding binding;
    private static final String BACKEND_URL = "https://obscure-headland-61818.herokuapp.com/";
    private OkHttpClient httpClient = new OkHttpClient();
    private String paymentIntentClientSecret;
    private Stripe stripe;
    private TextView amountTextView;

    String Amount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityViewItemBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent = getIntent();
        String itemid = intent.getStringExtra("ItemID");
        String userid = intent.getStringExtra("UserID");

        loadeItem(itemid);

        binding.payBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog = new Dialog(v.getContext());
                dialog.requestWindowFeature(Window.FEATURE_ACTION_BAR);
                dialog.setContentView(R.layout.dialog_pay);

                TextView price = dialog.findViewById(R.id.amountTextView);
                price.setText("Rs " + Amount);

                // Configure the SDK with your Stripe publishable key so it can make requests to Stripe
                stripe = new Stripe(getApplicationContext(), Objects.requireNonNull("pk_test_51Hy95sBTObBVmV25jlX3wuOElrRTW7ib7LKWFTvqoFf58L2Szj39Nmbzfd6MLXRzZXfytbKtDxpxO93i71sJfhIt00VpBaEbZQ"));
                // Create a PaymentIntent by calling the server's endpoint.
                MediaType mediaType = MediaType.get("application/json; charset=utf-8");

                double amount = Double.valueOf(Amount.toString()) * 100;

                Map<String, Object> payMap = new HashMap<>();
                Map<String, Object> itemMap = new HashMap<>();
                List<Map<String, Object>> itemList = new ArrayList<>();
                payMap.put("currency", "usd"); //dont change currency in testing phase otherwise it won't work
                itemMap.put("id", "photo_subscription");
                itemMap.put("amount", amount);
                itemList.add(itemMap);
                payMap.put("items", itemList);
                String json = new Gson().toJson(payMap);

                RequestBody body = RequestBody.create(json, mediaType);
                Request request = new Request.Builder()
                        .url(BACKEND_URL + "create-payment-intent")
                        .post(body)
                        .build();
                httpClient.newCall(request)
                        .enqueue(new PayCallback(ViewItemActivity.this));
                // Hook up the pay button to the card widget and stripe instance
                Button payButton = dialog.findViewById(R.id.payButton);
                payButton.setOnClickListener((View view) -> {
                    CardInputWidget cardInputWidget = dialog.findViewById(R.id.cardInputWidget);
                    PaymentMethodCreateParams params = cardInputWidget.getPaymentMethodCreateParams();
                    if (params != null) {
//                        ConfirmPaymentIntentParams confirmParams = ConfirmPaymentIntentParams
//                                .createWithPaymentMethodCreateParams(params, paymentIntentClientSecret);
//                        stripe.confirmPayment(ViewItemActivity.this, confirmParams);
                        Toast.makeText(ViewItemActivity.this, "Payment completed..", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(ViewItemActivity.this, "Enter Card Ditales", Toast.LENGTH_SHORT).show();
                    }
                });
                dialog.show();
                dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
                dialog.getWindow().setGravity(Gravity.BOTTOM);
            }
        });
    }

    private void loadeItem(String itemid) {
        DatabaseReference Diseaseref = FirebaseDatabase.getInstance().getReference("Items");
        Diseaseref.child(itemid)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String titel = "" + snapshot.child("ItmeTitle").getValue();
                        String price = "" + snapshot.child("ItmePrice").getValue();
                        String disease = "" + snapshot.child("ItmeDescription").getValue();
                        String city = "" + snapshot.child("ItmeCity").getValue();
                        String image = "" + snapshot.child("DiseaseImage").getValue();
                        binding.itemnameTv.setText(titel);
                        binding.itemDescriptionTv.fromHtml(disease);
                        binding.itemcityTv.setText(city);
                        binding.itemPriceTv.setText("Rs " + price);
                        Amount = price;

                        if (image.equals("img")) {
                            binding.image1.setImageDrawable(getResources().getDrawable(R.drawable.noimag, null));
                        } else {
                            Picasso.get().load(image).into(binding.image1, new Callback() {
                                @Override
                                public void onSuccess() {
                                }

                                @Override
                                public void onError(Exception e) {
                                }
                            });
                        }

                        binding.payBtn.setText("Pay" + price);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
    }


    private void displayAlert(@NonNull String title, @Nullable String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(message);
        builder.setPositiveButton("Ok", null);
        builder.create().show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Handle the result of stripe.confirmPayment
        stripe.onPaymentResult(requestCode, data, new PaymentResultCallback(this));
    }

    private void onPaymentSuccess(@NonNull final Response response) throws IOException {
        Gson gson = new Gson();
        Type type = new TypeToken<Map<String, String>>() {
        }.getType();
        Map<String, String> responseMap = gson.fromJson(
                Objects.requireNonNull(response.body()).string(),
                type
        );
        paymentIntentClientSecret = responseMap.get("clientSecret");
    }

    private static final class PayCallback implements okhttp3.Callback {
        @NonNull
        private final WeakReference<ViewItemActivity> activityRef;

        PayCallback(@NonNull ViewItemActivity activity) {
            activityRef = new WeakReference<>(activity);
        }

        @Override
        public void onFailure(@NonNull Call call, @NonNull IOException e) {
            final ViewItemActivity activity = activityRef.get();
            if (activity == null) {
                return;
            }
//            activity.runOnUiThread(() ->
//                    Toast.makeText(activity, "Error: " + e.toString(), Toast.LENGTH_LONG).show()
//            );
        }

        @Override
        public void onResponse(@NonNull Call call, @NonNull final Response response)
                throws IOException {
            final ViewItemActivity activity = activityRef.get();
            if (activity == null) {
                return;
            }
            if (!response.isSuccessful()) {
//                activity.runOnUiThread(() ->
//                        Toast.makeText( activity, "Error: " + response.toString(), Toast.LENGTH_LONG).show());
            } else {
                activity.onPaymentSuccess(response);
            }
        }
    }

    private static final class PaymentResultCallback implements ApiResultCallback<PaymentIntentResult> {
        @NonNull
        private final WeakReference<ViewItemActivity> activityRef;

        PaymentResultCallback(@NonNull ViewItemActivity activity) {
            activityRef = new WeakReference<>(activity);
        }

        @Override
        public void onSuccess(@NonNull PaymentIntentResult result) {
            final ViewItemActivity activity = activityRef.get();
            if (activity == null) {
                return;
            }
            PaymentIntent paymentIntent = result.getIntent();
            PaymentIntent.Status status = paymentIntent.getStatus();
            if (status == PaymentIntent.Status.Succeeded) {
                // Payment completed successfully
                Gson gson = new GsonBuilder().setPrettyPrinting().create();
                activity.displayAlert(
                        "Payment completed",
                        gson.toJson(paymentIntent)
                );
            } else if (status == PaymentIntent.Status.RequiresPaymentMethod) {
                // Payment failed – allow retrying using a different payment method
                activity.displayAlert(
                        "Payment failed",
                        Objects.requireNonNull(paymentIntent.getLastPaymentError()).getMessage()
                );
            }
        }

        @Override
        public void onError(@NonNull Exception e) {
            final ViewItemActivity activity = activityRef.get();
            if (activity == null) {
                return;
            }
            // Payment request failed – allow retrying using the same payment method
            activity.displayAlert("Error", e.toString());
        }
    }
}