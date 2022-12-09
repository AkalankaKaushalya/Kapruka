package com.kapruka.UserFragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
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

public class ShopFragment extends Fragment {

    List<ItemModel> itemModelList;
    ItemAdapter itemAdapter;
    RecyclerView ItemRv;

    public ShopFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_shop, container, false);
        ItemRv = view.findViewById(R.id.item_recycler_view);
        lodeItem();


        return view;
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
                        itemAdapter = new ItemAdapter(itemModelList, getContext());
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