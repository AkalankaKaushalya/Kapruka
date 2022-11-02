package com.kapruka.UserFragments;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kapruka.Adapters.QusetionsAdapter;
import com.kapruka.AddQusetionActivity;
import com.kapruka.Models.QusetionModel;
import com.kapruka.R;

import java.util.ArrayList;
import java.util.List;

public class QuestionsFragment extends Fragment {

    ExtendedFloatingActionButton AddQuationBtn;
    List<QusetionModel> qusetionModelList;
    QusetionsAdapter qusetionsAdapter;
    private RecyclerView QusetionsRv;
    private EditText SearchEt;
    private LottieAnimationView lottieAnimationView, lottieAnimationView2;

    public QuestionsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_questions, container, false);
        QusetionsRv = view.findViewById(R.id.qusetion_recycler_view);
        AddQuationBtn = view.findViewById(R.id.askqusetionBtn);
        lottieAnimationView2 = view.findViewById(R.id.nolottie1);
        SearchEt = view.findViewById(R.id.searchEt);

        lodeQusetion();

        AddQuationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), AddQusetionActivity.class));
            }
        });

        SearchEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                String sed = SearchEt.getText().toString();
                if (!sed.isEmpty()) {
                    lodeQusetion(sed);
                } else {
                    lodeQusetion();
                }


            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        return view;
    }

    private void lodeQusetion(String sed) {
        qusetionModelList = new ArrayList<>();
        DatabaseReference diseaseRf = FirebaseDatabase.getInstance().getReference("Qusetion");
        diseaseRf.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                qusetionModelList.clear();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    QusetionModel qusetionModel = ds.getValue(QusetionModel.class);
                    if (qusetionModel != null) {
                        if (qusetionModel.getQusetionTitle().toLowerCase().contains(sed.toLowerCase())) {
                            qusetionModelList.add(qusetionModel);
                            lottieAnimationView2.setVisibility(View.GONE);
                        }
                    } else {
                        lottieAnimationView2.setVisibility(View.VISIBLE);
                    }
                    qusetionsAdapter = new QusetionsAdapter(qusetionModelList, getActivity());
                    QusetionsRv.setAdapter(qusetionsAdapter);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void lodeQusetion() {
        qusetionModelList = new ArrayList<>();
        DatabaseReference diseaseRf = FirebaseDatabase.getInstance().getReference("Qusetion");
        diseaseRf.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                qusetionModelList.clear();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    QusetionModel qusetionModel = ds.getValue(QusetionModel.class);
                    if (qusetionModel != null) {
                        qusetionModelList.add(qusetionModel);
                        qusetionsAdapter = new QusetionsAdapter(qusetionModelList, getActivity());
                        QusetionsRv.setAdapter(qusetionsAdapter);
                        lottieAnimationView2.setVisibility(View.GONE);
                    } else {
                        lottieAnimationView2.setVisibility(View.VISIBLE);
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}