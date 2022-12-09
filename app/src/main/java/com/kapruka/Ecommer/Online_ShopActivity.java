package com.kapruka.Ecommer;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kapruka.Ecommer.Adapter.ItemAdapter;
import com.kapruka.Ecommer.Model.ItemModel;
import com.kapruka.R;

import java.util.ArrayList;
import java.util.List;

public class Online_ShopActivity extends AppCompatActivity {
    List<ItemModel> itemModelList;
    ItemAdapter itemAdapter;
    RecyclerView ItemRv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_online_shop);
        ItemRv = findViewById(R.id.item_recycler_view);
        lodeItem();
        findViewById(R.id.additemBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), addProdactActivity.class));
            }
        });


    }

    private void lodeItem() {
        itemModelList = new ArrayList<>();
        DatabaseReference diseaseRf = FirebaseDatabase.getInstance().getReference("Items");
        diseaseRf.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                itemModelList.clear();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    ItemModel itemModel = ds.getValue(ItemModel.class);
                    if (itemModel != null) {
                        itemModelList.add(itemModel);
                        itemAdapter = new ItemAdapter(itemModelList, getApplicationContext());
                        ItemRv.setAdapter(itemAdapter);
                        //lottieAnimationView.setVisibility(View.GONE);
                    } else {
                        //lottieAnimationView.setVisibility(View.VISIBLE);
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}