package com.kapruka;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kapruka.Adapters.QusetionsAdapter;
import com.kapruka.Models.QusetionModel;

import java.util.ArrayList;
import java.util.List;

public class ShowCityActivity extends AppCompatActivity {
    List<QusetionModel> qusetionModelList;
    QusetionsAdapter qusetionsAdapter;
    private RecyclerView QusetionsRv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_city);

        QusetionsRv = findViewById(R.id.qusetion_city_recycler_view);

        Intent intent = getIntent();
        String qcity = intent.getStringExtra("QCity");
        lodeQusetion(qcity);
    }

    private void lodeQusetion(String qcity) {
        qusetionModelList = new ArrayList<>();
        DatabaseReference diseaseRf = FirebaseDatabase.getInstance().getReference("Qusetion");
        diseaseRf.orderByChild("City").equalTo(qcity).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                qusetionModelList.clear();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    QusetionModel qusetionModel = ds.getValue(QusetionModel.class);
                    if (qusetionModel != null) {
                        qusetionModelList.add(qusetionModel);
                        qusetionsAdapter = new QusetionsAdapter(qusetionModelList, ShowCityActivity.this);
                        QusetionsRv.setAdapter(qusetionsAdapter);
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
    }
}